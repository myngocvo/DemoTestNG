package com.myngoc.demotestng.testcases.customer_testcases;

import com.myngoc.demotestng.common.BaseSetUp;
import com.myngoc.demotestng.common.ExcelHelper;
import com.myngoc.demotestng.pages.customer.HomePage;
import com.myngoc.demotestng.pages.customer.RegisterPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.*;

public class RegisterTest {
    private WebDriver driver;
    public HomePage homePage;
    public RegisterPage registerPage;
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

    @DataProvider(name = "registerData")
    public Object[][] registerData() throws Exception {
        excel.setExcelFile("src/test/resources/excelData/customerRegisterData.xlsx", "registerData");
        int row = excel.getMaxRow();
        int col = excel.getMaxCol();
        Object[][] data = new Object[row][col];
        for (int i = 0; i < row; i++) {

        }
        return data;
    }

    @Test(dataProvider = "registerData")
    public void testRegister(String phoneNumber, String username, String password, String confirmPassword, boolean status) {
        registerPage.isRegisterSuccess(phoneNumber, username, password, confirmPassword, status);
    }
}
