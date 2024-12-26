package com.myngoc.demotestng.testcases.customer_testcases;

import com.myngoc.demotestng.common.BaseSetUp;
import com.myngoc.demotestng.common.ValidateHelper;
import com.myngoc.demotestng.pages.customer.HomePage;
import com.myngoc.demotestng.pages.customer.LoginPage;
import com.myngoc.demotestng.pages.customer.ProductDetailPage;
import com.myngoc.demotestng.pages.customer.ShoppingCartPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ShoppingCartTest extends BaseSetUp {
    private WebDriver driver;
    private LoginPage loginPage;
    private HomePage homePage;
    private ShoppingCartPage shoppingCartPage;
    private ProductDetailPage productDetailPage;

    @BeforeClass
    public void setUp() {
        driver = new BaseSetUp().setupDriver("chrome", "customer");
        homePage = new HomePage(driver);
        loginPage = new LoginPage(driver);
        ValidateHelper validateHelper = new ValidateHelper(driver);
        shoppingCartPage = new ShoppingCartPage(driver);
        productDetailPage = new ProductDetailPage(driver);
        PageFactory.initElements(driver, this);
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @BeforeMethod
    public void login() {
        loginPage.login("0932877995", "MyNgoc@(2207)");
    }

    @Test(priority = 2)
    public void testDisplayEmptyShoppingCart() {
        homePage.openShoppingCartPage();
        shoppingCartPage.checkDisplayEmptyShoppingCart();
    }

    @Test(priority = 1)
    public void testDeleteSingleBookFromCart() {
        String bookName = "Tuyển Tập Truyện Ngắn Hay Nhất Của Nguyễn Minh Châu";
        homePage.openShoppingCartPage();
        if (!productDetailPage.isBookInCart(bookName)) {
            productDetailPage.addToCart(bookName, true);
        }
        homePage.openShoppingCartPage();
        boolean isDeleted = shoppingCartPage.deleteBookFromCart(bookName);
        Assert.assertTrue(isDeleted, "Sản phẩm không được xóa khỏi giỏ hàng.");
    }

    @Test(priority = 3)
    public void testDeleteAllBooksFromCart() throws InterruptedException {
        String[] books = {
                "CÁCH NỀN KINH TẾ VẬN HÀNH Niềm tin, sự sụp đổ và những lời tiên tri tự đúng",
                "Tuyển tập Nam Cao",
                "Tuyển tập Vũ Trọng Phụng",
                "Tuyển Tập Truyện Ngắn Hay Nhất Của Nguyễn Minh Châu",
                "Văn Học Trong Nhà Trường: Thơ Nguyễn Khuyến"};
        for (String book : books) {
            productDetailPage.addToCart(book, true);
        }
        homePage.openShoppingCartPage();
        shoppingCartPage.deleteAllBooksFromCart();
        shoppingCartPage.checkDisplayEmptyShoppingCart();
    }
}
