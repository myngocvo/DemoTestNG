package com.myngoc.demotestng.common;

import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExcelHelper {

    private static final Logger LOGGER = Logger.getLogger(ExcelHelper.class.getName());
    private final Map<String, Integer> columns = new HashMap<>();
    private Workbook workbook;
    private Sheet sheet;
    private String excelFilePath;

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

    public String getCellStringData(String columnName, int rownum) {
        try {
            if (sheet == null || columns == null) {
                LOGGER.warning("Sheet or column mapping is null.");
                return "";
            }

            Integer colIndex = columns.get(columnName);
            if (colIndex == null) {
                LOGGER.warning("Column name not found: " + columnName);
                return "";
            }

            if (rownum < 0 || rownum >= sheet.getPhysicalNumberOfRows()) {
                LOGGER.warning("Invalid row number: " + rownum);
                return "";
            }

            Row row = sheet.getRow(rownum);
            if (row == null) return "";

            if (colIndex < 0 || colIndex >= row.getLastCellNum()) {
                LOGGER.warning("Invalid column number: " + colIndex);
                return "";
            }

            Cell cell = row.getCell(colIndex);
            if (cell == null) return "";

            switch (cell.getCellType()) {
                case STRING:
                    return cell.getStringCellValue();
                case BOOLEAN:
                    return Boolean.toString(cell.getBooleanCellValue());
                case NUMERIC:
                    return DateUtil.isCellDateFormatted(cell) ? cell.getDateCellValue().toString() : String.valueOf(cell.getNumericCellValue());
                default:
                    return "";
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error getting cell data: {0}", e.getMessage());
            return "";
        }
    }

    public double getCellNumericData(String columnName, int rownum) {
        try {
            if (sheet == null || columns == null) {
                LOGGER.warning("Sheet or column mapping is null.");
                return 0.0;
            }

            Integer colIndex = columns.get(columnName);
            if (colIndex == null) {
                LOGGER.warning("Column name not found: " + columnName);
                return 0.0;
            }

            if (rownum < 0 || rownum >= sheet.getPhysicalNumberOfRows()) {
                LOGGER.warning("Invalid row number: " + rownum);
                return 0.0;
            }

            Row row = sheet.getRow(rownum);
            if (row == null) return 0.0;

            if (colIndex < 0 || colIndex >= row.getLastCellNum()) {
                LOGGER.warning("Invalid column number: " + colIndex);
                return 0.0;
            }

            Cell cell = row.getCell(colIndex);
            if (cell == null) return 0.0;

            if (cell.getCellType() == CellType.NUMERIC) {
                return cell.getNumericCellValue();
            } else {
                LOGGER.warning(String.format("Cell at row %d, column %s is not numeric.", rownum, columnName));
                return 0.0;
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error getting numeric cell data: {0}", e.getMessage());
            return 0.0;
        }
    }

    public boolean getCellBooleanData(String columnName, int rownum) {
        try {
            String cellValue = getCellStringData(columnName, rownum).trim().toLowerCase();

            if ("true".equals(cellValue) || "1".equals(cellValue) || "yes".equals(cellValue)) {
                return true;
            }
            if ("false".equals(cellValue) || "0".equals(cellValue) || "no".equals(cellValue)) {
                return false;
            }

            LOGGER.warning(String.format("Invalid boolean value at row %d, column %s: %s", rownum, columnName, cellValue));
            return false;
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error getting boolean cell data: {0}", e.getMessage());
            throw new RuntimeException("Failed to get boolean value from cell", e);
        }
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
