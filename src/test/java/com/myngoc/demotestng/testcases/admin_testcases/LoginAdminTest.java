package com.myngoc.demotestng.testcases.admin_testcases;

import com.myngoc.demotestng.common.BaseSetUp;
import com.myngoc.demotestng.common.ExcelHelper;
import com.myngoc.demotestng.pages.admin.LoginAdminPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.*;

public class LoginAdminTest extends BaseSetUp {
    private WebDriver driver;
    private LoginAdminPage loginAdminPage;
    private ExcelHelper excel;

    @BeforeClass
    public void initialize() {
        excel = new ExcelHelper();
    }

    @BeforeMethod
    public void setUp() {
        driver = new BaseSetUp().setupDriver("chrome", "admin");
        loginAdminPage = new LoginAdminPage(driver);
        PageFactory.initElements(driver, this);
    }

    @DataProvider(name = "dataLoginAdmin", parallel = true)
    public Object[][] dataLoginAdmin() throws Exception {
        excel.setExcelFile("src/test/resources/excelData/adminLoginData.xlsx", "adminLoginData");
        int row = excel.getMaxRow();
        int col = excel.getMaxCol();
        Object[][] data = new Object[row][col];
        for (int i = 0; i < row; i++) {
            data[i][0] = excel.getCellStringData("email", i + 1);
            data[i][1] = excel.getCellStringData("password", i + 1);
            data[i][2] = excel.getCellBooleanData("status", i + 1);
        }
        return data;
    }

    @Test(dataProvider = "dataLoginAdmin")
    public void loginAdmin(String email, String password, boolean expectedResult) {
        loginAdminPage.verifyLoginAdmin(email, password, expectedResult);
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
        if (excel != null) {
            excel.closeWorkbook();
        }
    }
}