package com.myngoc.demotestng.common;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExcelHelper {

    private static final Logger LOGGER = Logger.getLogger(ExcelHelper.class.getName());

    private Workbook workbook;
    private Sheet sheet;
    private String excelFilePath;
    private final Map<String, Integer> columns = new HashMap<>();

    public void setExcelFile(String excelPath, String sheetName) {
        try (FileInputStream fis = new FileInputStream(new File(excelPath))) {
            File file = new File(excelPath);

            if (!file.exists()) {
                if (file.createNewFile()) {
                    LOGGER.info("File doesn't exist, created a new one.");
                }
            }

            workbook = WorkbookFactory.create(fis);
            sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                sheet = workbook.createSheet(sheetName);
                LOGGER.info("Sheet doesn't exist, created a new one.");
            }

            this.excelFilePath = excelPath;

            if (sheet.getRow(0) != null) {
                sheet.getRow(0).forEach(cell -> columns.put(cell.getStringCellValue(), cell.getColumnIndex()));
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error while setting Excel file: {0}", e.getMessage());
        }
    }

    public int getMaxRow() {
        return (sheet != null) ? sheet.getLastRowNum() : 0;
    }

    public int getMaxCol() {
        Row firstRow = (sheet != null) ? sheet.getRow(0) : null;
        return (firstRow != null) ? firstRow.getPhysicalNumberOfCells() : 0;
    }

    public String getCellData(int rownum, int colnum) {
        try {
            Row row = sheet.getRow(rownum);
            if (row == null) return "";

            Cell cell = row.getCell(colnum);
            if (cell == null) return "";

            return switch (cell.getCellType()) {
                case STRING -> cell.getStringCellValue();
                case NUMERIC ->
                        DateUtil.isCellDateFormatted(cell) ? cell.getDateCellValue().toString() : String.valueOf((long) cell.getNumericCellValue());
                case BOOLEAN -> Boolean.toString(cell.getBooleanCellValue());
                case BLANK -> "";
                default -> "";
            };
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error getting cell data: {0}", e.getMessage());
            return "";
        }
    }

    public String getCellStringData(String columnName, int rownum) {
        Integer colIndex = columns.get(columnName);
        if (colIndex == null) {
            LOGGER.warning("Column name not found: " + columnName);
            return "";
        }
        return getCellData(rownum, colIndex);
    }

    public boolean getCellBooleanData(String columnName, int rownum) {
        return Boolean.parseBoolean(getCellStringData(columnName, rownum));
    }

    public void setCellData(String data, int rownum, int colnum) {
        try {
            Row row = sheet.getRow(rownum);
            if (row == null) {
                row = sheet.createRow(rownum);
            }

            Cell cell = row.getCell(colnum);
            if (cell == null) {
                cell = row.createCell(colnum);
            }

            cell.setCellValue(data);
            saveChanges();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error setting cell data: {0}", e.getMessage());
        }
    }

    public void setCellDataByColumn(String columnName, int rownum, String data) {
        Integer colIndex = columns.get(columnName);
        if (colIndex == null) {
            LOGGER.warning("Column name not found: " + columnName);
            return;
        }
        setCellData(data, rownum, colIndex);
    }

    private void saveChanges() throws IOException {
        try (FileOutputStream fos = new FileOutputStream(excelFilePath)) {
            workbook.write(fos);
        }
    }

    public void closeWorkbook() {
        try {
            if (workbook != null) {
                workbook.close();
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error closing workbook: {0}", e.getMessage());
        }
    }
}
