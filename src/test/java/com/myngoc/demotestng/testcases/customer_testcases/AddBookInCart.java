package com.myngoc.demotestng.testcases.customer_testcases;

import com.myngoc.demotestng.common.BaseSetUp;
import com.myngoc.demotestng.common.ExcelHelper;
import com.myngoc.demotestng.pages.customer.LoginPage;
import com.myngoc.demotestng.pages.customer.MyProfilePage;
import com.myngoc.demotestng.pages.customer.ProductDetailPage;
import com.myngoc.demotestng.pages.customer.ShoppingCartPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.*;


public class AddBookInCart {
    private WebDriver driver;
    private ProductDetailPage productDetailPage;
    private LoginPage loginPage;
    private MyProfilePage myProfilePage;
    private ExcelHelper excelHelper;
    private ShoppingCartPage shoppingCartPage;

    @BeforeClass
    public void setUp() {
        driver = new BaseSetUp().setupDriver("chrome", "customer");
        productDetailPage = new ProductDetailPage(driver);
        loginPage = new LoginPage(driver);
        myProfilePage = new MyProfilePage(driver);
        excelHelper = new ExcelHelper();
        shoppingCartPage = new ShoppingCartPage(driver);
        PageFactory.initElements(driver, this);
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @BeforeMethod(onlyForGroups = "loginRequired")
    public void loginBeforeTests() throws Exception {
        excelHelper.setExcelFile("src/test/resources/excelData/customerLoginData.xlsx", "dataLogin");
        loginPage.login(excelHelper.getCellStringData("username", 1), excelHelper.getCellStringData("password", 1));
    }

    @AfterMethod(onlyForGroups = "loginRequired")
    public void logoutAfterTests() {
        myProfilePage.logout();
    }

    @Test(groups = "loginRequired", priority = 1)
    public void addToCartWhenNotInCart() throws InterruptedException {
        String bookName = "Tuyển Tập Truyện Ngắn Hay Nhất Của Nguyễn Minh Châu";
        boolean isBookExisted = productDetailPage.isBookInCart(bookName);
        if (isBookExisted) {
            shoppingCartPage.deleteAllBooksFromCart();
        }
        productDetailPage.addToCart(bookName, true);
        productDetailPage.verifyAddToCartMessage(false);
        shoppingCartPage.deleteAllBooksFromCart();
    }

    @Test(groups = "loginRequired", priority = 2)
    public void addToCartWhenAlreadyInCart() throws InterruptedException {
        Thread.sleep(1000);
        String bookName = "Nhật Ký Trong Tù";
        boolean isBookExisted = productDetailPage.isBookInCart(bookName);
        if (!isBookExisted) {
            productDetailPage.addToCart(bookName, true);
        }
        productDetailPage.addToCart(bookName, true);
        productDetailPage.verifyAddToCartMessage(true);
        shoppingCartPage.deleteAllBooksFromCart();
    }

    @Test(groups = "noLogin", priority = 3)
    public void addToCartWhenNotInCart_noLogin() throws InterruptedException {
        productDetailPage.verifyAddToCartMessage_nologin("Nhật Ký Trong Tù");
        shoppingCartPage.deleteAllBooksFromCart();
    }
}
