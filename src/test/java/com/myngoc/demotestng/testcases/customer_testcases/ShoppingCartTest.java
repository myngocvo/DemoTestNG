package com.myngoc.demotestng.testcases.customer_testcases;

import com.myngoc.demotestng.common.BaseSetUp;
import com.myngoc.demotestng.common.ExcelHelper;
import com.myngoc.demotestng.common.ValidateHelper;
import com.myngoc.demotestng.pages.customer.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ShoppingCartTest extends BaseSetUp {
    private WebDriver driver;
    private LoginPage loginPage;
    private HomePage homePage;
    private ShoppingCartPage shoppingCartPage;
    private ProductDetailPage productDetailPage;
    private ExcelHelper excelHelper;
    private MyProfilePage myProfilePage;

    @BeforeMethod
    public void setUp() {
        driver = new BaseSetUp().setupDriver("chrome", "customer");
        homePage = new HomePage(driver);
        loginPage = new LoginPage(driver);
        ValidateHelper validateHelper = new ValidateHelper(driver);
        shoppingCartPage = new ShoppingCartPage(driver);
        productDetailPage = new ProductDetailPage(driver);
        excelHelper = new ExcelHelper();
        myProfilePage = new MyProfilePage(driver);
        PageFactory.initElements(driver, this);
        excelHelper.setExcelFile("src/test/resources/excelData/customerLoginData.xlsx", "dataLogin");
        loginPage.login(excelHelper.getCellStringData("username", 1), excelHelper.getCellStringData("password", 1));
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testDisplayEmptyShoppingCart() {
        homePage.openShoppingCart();
        shoppingCartPage.deleteAllBooksFromCart();
        shoppingCartPage.checkDisplayEmptyShoppingCart();
        System.out.println("Test Display Empty ShoppingCart passed");
    }

    @Test
    public void testDeleteSingleBookFromCart() {
        String bookName = "Tuyển Tập Truyện Ngắn Hay Nhất Của Nguyễn Minh Châu";
        homePage.openShoppingCart();
        if (!productDetailPage.isBookInCart(bookName)) {
            productDetailPage.addToCart(bookName, true);
        }
        homePage.openShoppingCart();
        boolean isDeleted = shoppingCartPage.deleteBookFromCart(bookName);
        Assert.assertTrue(isDeleted, "Sản phẩm không được xóa khỏi giỏ hàng.");
        System.out.println("Test Delete A Book From Cart passed");
    }

    @Test
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
        homePage.openShoppingCart();
        shoppingCartPage.deleteAllBooksFromCart();
        shoppingCartPage.checkDisplayEmptyShoppingCart();
        System.out.println("Test Delete All Books From Cart passed");
    }
}
