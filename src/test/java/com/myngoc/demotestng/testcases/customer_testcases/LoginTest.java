package com.myngoc.demotestng.testcases.customer_testcases;

import com.myngoc.demotestng.common.BaseSetUp;
import com.myngoc.demotestng.common.ExcelHelper;
import com.myngoc.demotestng.pages.customer.HomePage;
import com.myngoc.demotestng.pages.customer.LoginPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.*;


public class LoginTest {
    public HomePage homePage;
    public LoginPage loginPage;
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
        loginPage = new LoginPage(driver);
        PageFactory.initElements(driver, this);
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @DataProvider(name = "dataLogin", parallel = false)
    public Object[][] dataLogin() throws Exception {
        excel.setExcelFile("src/test/resources/excelData/customerLoginData.xlsx", "dataLogin");
        int row = excel.getMaxRow();
        int col = excel.getMaxCol();
        Object[][] data = new Object[row][col];
        for (int i = 0; i < row; i++) {
            data[i][0] = excel.getCellStringData("username", i + 1);
            data[i][1] = excel.getCellStringData("password", i + 1);
            data[i][2] = excel.getCellBooleanData("status", i + 1);
        }
        return data;
    }

    @Test(dataProvider = "dataLogin")
    public void login(String username, String password, boolean expectedResult) {
        boolean result = loginPage.isLoginSuccessful(username, password, expectedResult);
        if (result) {
            System.out.println("Test Login with Username: " + username + ", Password: " + password + " Passed!");
        } else {
            System.out.println("Test Login with Username: " + username + ", Password: " + password + " Failed!");
        }
    }
}