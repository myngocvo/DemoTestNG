package com.myngoc.demotestng.testcases.customer_testcases;

import com.myngoc.demotestng.common.BaseSetUp;
import com.myngoc.demotestng.common.ExcelHelper;
import com.myngoc.demotestng.pages.customer.HomePage;
import com.myngoc.demotestng.pages.customer.LoginPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.*;


public class LoginTest {
    private WebDriver driver;
    public HomePage homePage;
    public LoginPage loginPage;
    private ExcelHelper excel;

    @BeforeClass
    public void initialize() {
        excel = new ExcelHelper();
    }

    @BeforeMethod
    public void setUp() {
        driver = new BaseSetUp().setupDriver("chrome", "customer");
        homePage = new HomePage(driver);
        loginPage = new LoginPage(driver);
        PageFactory.initElements(driver, this);
    }

    @DataProvider(name = "dataLogin", parallel = true)
    public Object[][] dataLogin() throws Exception {
        excel.setExcelFile("src/test/resources/customerLoginData.xlsx", "dataLogin");
        Object[][] data = new Object[6][3];
        for (int i = 0; i < 6; i++) {
            data[i][0] = excel.getCellStringData("username", i + 1);
            data[i][1] = excel.getCellStringData("password", i + 1);
            data[i][2] = excel.getCellBooleanData("status", i + 1);
        }
        return data;
    }

//    @DataProvider(name = "dataLogin")
//    public Object[][] dataLogin() throws Exception {
//        excel.setExcelFile("src/test/resources/customerLoginData.xlsx", "dataLogin");
//        Object[][] data = new Object[6][3];
//        for (int i = 0; i < 6; i++) {
//            data[i][0] = excel.getCellStringData("username", i + 1);
//            data[i][1] = excel.getCellStringData("password", i + 1);
//            data[i][2] = excel.getCellBooleanData("status", i + 1);
//        }
//        return data;
//    }

    @Test(dataProvider = "dataLogin")
    public void login(String username, String password, boolean expectedResult) {
        boolean result = loginPage.isLoginSuccessful(username, password, expectedResult);
        if (result) {
            System.out.println("Test Login with Username: " + username + ", Password: " + password + " Passed!");
        } else {
            System.out.println("Test Login with Username: " + username + ", Password: " + password + " Failed!");
        }
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}