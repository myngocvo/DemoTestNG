package com.myngoc.demotestng.testcases.customer_testcases;

import com.myngoc.demotestng.common.BaseSetUp;
import com.myngoc.demotestng.common.ValidateHelper;
import com.myngoc.demotestng.pages.customer.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.*;


public class AddBookInCart {
    private WebDriver driver;
    private ProductDetailPage productDetailPage;
    private LoginPage loginPage;
    private MyProfilePage myProfilePage;

    @BeforeClass
    public void setUp() {
        driver = new BaseSetUp().setupDriver("chrome", "customer");
        productDetailPage = new ProductDetailPage(driver);
        loginPage = new LoginPage(driver);
        myProfilePage = new MyProfilePage(driver);
        PageFactory.initElements(driver, this);
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @BeforeMethod(onlyForGroups = "loginRequired")
    public void loginBeforeTests() {
        loginPage.login("0932877994", "MyNgoc@(2207)");
    }

    @AfterMethod(onlyForGroups = "loginRequired")
    public void logoutAfterTests() {
        myProfilePage.logout();
    }

    @Test(groups = "loginRequired", priority = 1)
    public void addToCartWhenNotInCart() {
        productDetailPage.verifyAddToCartMessage("Tuyển Tập Truyện Ngắn Hay Nhất Của Nguyễn Minh Châu");
    }

    @Test(groups = "loginRequired", priority = 2)
    public void addToCartWhenAlreadyInCart() {
        //loginPage.login("0932877995", "MyNgoc@(2207)");
        productDetailPage.verifyAddToCartMessage("Nhật Ký Trong Tù");
        productDetailPage.verifyAddToCartMessage("Nhật Ký Trong Tù");
    }

    @Test(groups = "noLogin", priority = 3)
    public void addToCartWhenNotInCart_noLogin() {
        productDetailPage.verifyAddToCartMessage_nologin("Nhật Ký Trong Tù");
    }
}
