package com.myngoc.demotestng.testcases.customer_testcases;

// Import các thư viện cần thiết

import com.myngoc.demotestng.common.BaseSetUp;
import com.myngoc.demotestng.common.ExcelHelper;
import com.myngoc.demotestng.pages.customer.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.*;

public class AddBookInCartTest {
    private WebDriver driver;
    private ProductDetailPage productDetailPage;
    private LoginPage loginPage;
    private MyProfilePage myProfilePage;
    private ExcelHelper excelHelper;
    private ShoppingCartPage shoppingCartPage;
    private HomePage homePage;

    @BeforeClass
    public void setUp() {
        // Thiết lập trình duyệt và khởi tạo các trang
        driver = new BaseSetUp().setupDriver("customer");
        productDetailPage = new ProductDetailPage(driver);
        loginPage = new LoginPage(driver);
        myProfilePage = new MyProfilePage(driver);
        excelHelper = new ExcelHelper();
        shoppingCartPage = new ShoppingCartPage(driver);
        homePage = new HomePage(driver);
        PageFactory.initElements(driver, this); // Khởi tạo các phần tử của trang
    }

    @AfterClass
    public void tearDown() {
        // Đóng trình duyệt khi hoàn tất tất cả các test
        if (driver != null) {
            driver.quit();
        }
    }

    @BeforeMethod(onlyForGroups = "loginRequired")
    public void loginBeforeTests() {
        // Đăng nhập trước khi chạy các test yêu cầu đăng nhập
        excelHelper.setExcelFile("src/test/resources/excelData/loginData.xlsx", "customer");
        loginPage.login(
                excelHelper.getCellStringData("username", 1),
                excelHelper.getCellStringData("password", 1)
        );
    }

    @AfterMethod(onlyForGroups = "loginRequired")
    public void logoutAfterTests() {
        // Đăng xuất sau khi chạy các test yêu cầu đăng nhập
        myProfilePage.logout();
    }

    // Test thêm sách vào giỏ khi sách chưa có trong giỏ
    @Test(groups = "loginRequired", priority = 1)
    public void addToCartWhenNotInCart() throws InterruptedException {
        String bookName = "Tuyển Tập Truyện Ngắn Hay Nhất Của Nguyễn Minh Châu";
        // Mở giỏ hàng và xóa toàn bộ sách (nếu có)
        homePage.openShoppingCart();
        shoppingCartPage.deleteAllBooksFromCart();
        // Thêm sách vào giỏ và xác minh thông báo
        productDetailPage.addToCart(bookName, true);
        productDetailPage.verifyAddToCartMessage(false);
        // Xóa tất cả sách sau khi kiểm tra
        shoppingCartPage.deleteAllBooksFromCart();
    }

    // Test thêm sách vào giỏ khi sách đã có trong giỏ
    @Test(groups = "loginRequired", priority = 2)
    public void addToCartWhenAlreadyInCart() throws InterruptedException {
        Thread.sleep(1000);
        String bookName = "Nhật Ký Trong Tù";
        // Kiểm tra nếu sách chưa có, thêm sách vào giỏ
        boolean isBookExisted = productDetailPage.isBookInCart(bookName);
        if (!isBookExisted) {
            productDetailPage.addToCart(bookName, true);
        }
        // Thêm lại sách vào giỏ và xác minh thông báo
        productDetailPage.addToCart(bookName, true);
        productDetailPage.verifyAddToCartMessage(true);
        // Xóa tất cả sách sau khi kiểm tra
        shoppingCartPage.deleteAllBooksFromCart();
    }

    // Test thêm sách vào giỏ khi chưa đăng nhập
    @Test(groups = "noLogin", priority = 3)
    public void addToCartWhenNotInCart_noLogin() throws InterruptedException {
        productDetailPage.verifyAddToCartMessage_nologin("Nhật Ký Trong Tù");
        // Xóa tất cả sách sau khi kiểm tra
        shoppingCartPage.deleteAllBooksFromCart();
    }
}
