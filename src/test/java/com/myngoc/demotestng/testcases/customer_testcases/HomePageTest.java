package com.myngoc.demotestng.testcases.customer_testcases;

import com.myngoc.demotestng.common.BaseSetUp;
import com.myngoc.demotestng.common.ExcelHelper;
import com.myngoc.demotestng.common.ValidateHelper;
import com.myngoc.demotestng.pages.customer.HomePage;
import com.myngoc.demotestng.pages.customer.LoginPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class HomePageTest {
    private WebDriver driver;
    private LoginPage loginPage;
    private HomePage homePage;
    private ValidateHelper validateHelper;
    private ExcelHelper excelHelper;

    @BeforeMethod
    public void setUp() {
        driver = new BaseSetUp().setupDriver("chrome", "customer");
        homePage = new HomePage(driver);
        loginPage = new LoginPage(driver);
        validateHelper = new ValidateHelper(driver);
        excelHelper = new ExcelHelper();
        PageFactory.initElements(driver, this);
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test()
    public void openShoppingCartPage_login() throws Exception {
        excelHelper.setExcelFile("src/test/resources/excelData/customerLoginData.xlsx", "dataLogin");
        loginPage.login(excelHelper.getCellStringData("username", 1), excelHelper.getCellStringData("password", 1));
        validateHelper.waitForPageLoaded();
        homePage.openShoppingCart();
        homePage.verifyopenShoppingCart();
    }

    @Test()
    public void openShoppingCartPage_nologin() throws InterruptedException {
        validateHelper.waitForPageLoaded();
        homePage.openShoppingCart();
        String snackbarMessage = validateHelper.getSnackbarMessage();
        Assert.assertEquals(snackbarMessage, "Vui lòng đăng nhập để xem giỏ hàng", "Notification is incorrect!");
    }

    @Test()
    public void searchBookMatchingResults() {
        String bookName = "Giải Thích Ngữ Pháp Tiếng Anh (Tái Bản 2022)";
        homePage.searchBook(bookName, true);
    }

    @Test()
    public void searchBookMatchingPartialResults() {
        String bookName = "Tuyển tập Vũ ";
        homePage.searchBook(bookName, true);
    }

    @Test()
    public void searchBookNoResults() {
        String bookName = "Nonexistent";
        homePage.searchBook(bookName, false);
    }

}
