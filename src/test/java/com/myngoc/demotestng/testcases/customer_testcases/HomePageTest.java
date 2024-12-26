package com.myngoc.demotestng.testcases.customer_testcases;

import com.myngoc.demotestng.common.BaseSetUp;
import com.myngoc.demotestng.common.ValidateHelper;
import com.myngoc.demotestng.pages.customer.HomePage;
import com.myngoc.demotestng.pages.customer.LoginPage;
import com.myngoc.demotestng.pages.customer.ShoppingCartPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class HomePageTest {
    private WebDriver driver;
    private LoginPage loginPage;
    private HomePage homePage;
    private ValidateHelper validateHelper;
    private ShoppingCartPage shoppingCartPage;

    @BeforeClass
    public void setUp() {
        driver = new BaseSetUp().setupDriver("chrome", "customer");
        homePage = new HomePage(driver);
        loginPage = new LoginPage(driver);
        validateHelper = new ValidateHelper(driver);
        shoppingCartPage = new ShoppingCartPage(driver);
        PageFactory.initElements(driver, this);
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test()
    public void openShoppingCartPage_login() {
        loginPage.login("0932877994", "MyNgoc@(2207)");
        validateHelper.waitForPageLoaded();
        homePage.openShoppingCartPage();
        homePage.openShoppingCartsuccessfully();
    }

    @Test()
    public void openShoppingCartPage_nologin() {
        validateHelper.waitForPageLoaded();
        homePage.openShoppingCartPage();
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
