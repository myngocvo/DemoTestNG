package com.myngoc.demotestng.testcases.customer_testcases;

import com.myngoc.demotestng.common.BaseSetUp;
import com.myngoc.demotestng.common.ExcelHelper;
import com.myngoc.demotestng.pages.customer.HomePage;
import com.myngoc.demotestng.pages.customer.RegisterPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.*;

public class RegisterTest {
    public HomePage homePage;
    public RegisterPage registerPage;
    private WebDriver driver;
    private ExcelHelper excel;

    @BeforeClass
    public void init() {
        excel = new ExcelHelper();
    }

    @BeforeMethod
    public void setUp() {
        driver = new BaseSetUp().setupDriver("chrome", "customer");
        homePage = new HomePage(driver);
        registerPage = new RegisterPage(driver);
        PageFactory.initElements(driver, this);
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @DataProvider
    public Object[][] registerData() throws Exception {
        excel.setExcelFile("src/test/resources/excelData/customerRegisterData.xlsx", "registerData");
        int row = excel.getMaxRow();
        int col = excel.getMaxCol();
        Object[][] data = new Object[row][col];
        for (int i = 0; i < row; i++) {
            data[i][0] = excel.getCellStringData("phoneNumber", i + 1);
            data[i][1] = excel.getCellStringData("username", i + 1);
            data[i][2] = excel.getCellStringData("password", i + 1);
            data[i][3] = excel.getCellStringData("confirmPassword", i + 1);
            data[i][4] = excel.getCellBooleanData("status", i + 1);
        }
        return data;
    }

    @Test(dataProvider = "registerData")
    public void testRegister(String phoneNumber, String username, String password, String confirmPassword, boolean status) {
        boolean result = registerPage.isRegisterSuccess(phoneNumber, username, password, confirmPassword, status);
        if (result) {
            System.out.println("Test register with phone number: " + phoneNumber + ", Password: " + password + " Passed!");
        } else {
            System.out.println("Test register with phone number: " + phoneNumber + ", Password: " + password + " Failed!");
        }
    }
}
