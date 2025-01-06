package com.myngoc.demotestng.testcases.customer_testcases;

// Import các thư viện cần thiết

import com.myngoc.demotestng.common.BaseSetUp;
import com.myngoc.demotestng.common.ExcelHelper;
import com.myngoc.demotestng.pages.customer.HomePage;
import com.myngoc.demotestng.pages.customer.LoginPage;
import com.myngoc.demotestng.pages.customer.ProductDetailPage;
import com.myngoc.demotestng.pages.customer.ShoppingCartPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

// Lớp kiểm tra các chức năng của giỏ hàng trong ứng dụng khách hàng
public class ShoppingCartTest extends BaseSetUp {
    private WebDriver driver; // Đối tượng WebDriver để điều khiển trình duyệt
    private HomePage homePage; // Trang chủ
    private ShoppingCartPage shoppingCartPage; // Trang giỏ hàng
    private ProductDetailPage productDetailPage; // Trang chi tiết sản phẩm

    @BeforeMethod
    public void setUp() {
        // Khởi tạo driver và các trang cần thiết
        driver = new BaseSetUp().setupDriver("customer");
        homePage = new HomePage(driver);
        LoginPage loginPage = new LoginPage(driver);
        shoppingCartPage = new ShoppingCartPage(driver);
        productDetailPage = new ProductDetailPage(driver);

        // Đọc dữ liệu đăng nhập từ file Excel
        ExcelHelper excelHelper = new ExcelHelper();
        PageFactory.initElements(driver, this); // Khởi tạo các phần tử trang với PageFactory
        excelHelper.setExcelFile("src/test/resources/excelData/loginData.xlsx", "customer");
        loginPage.login(excelHelper.getCellStringData("username", 1), excelHelper.getCellStringData("password", 1));
    }

    @AfterMethod
    public void tearDown() {
        // Đóng trình duyệt sau khi chạy xong mỗi test
        if (driver != null) {
            driver.quit();
        }
    }

    @DataProvider
    public String[][] bookData() {
        // Lấy dữ liệu sách từ file Excel để sử dụng trong các test
        ExcelHelper excelHelper = new ExcelHelper();
        excelHelper.setExcelFile("src/test/resources/excelData/paymentData.xlsx", "bookNames");
        int row = excelHelper.getMaxRow();
        int col = excelHelper.getMaxCol();
        String[][] data = new String[col][row];
        for (int i = 0; i < row; i++) {
            data[0][i] = excelHelper.getCellStringData("bookName", (i + 1));
        }
        return data;
    }

    @Test
    public void testDisplayEmptyShoppingCart() {
        // Kiểm tra hiển thị thông báo giỏ hàng rỗng
        homePage.openShoppingCart(); // Mở trang giỏ hàng
        shoppingCartPage.deleteAllBooksFromCart(); // Xóa toàn bộ sách khỏi giỏ hàng
        shoppingCartPage.checkDisplayEmptyShoppingCart(); // Kiểm tra thông báo giỏ hàng rỗng
        System.out.println("Test Display Empty ShoppingCart passed");
    }

    @Test
    public void testDeleteSingleBookFromCart() {
        // Kiểm tra xóa một sách khỏi giỏ hàng
        String bookName = "Tuyển Tập Truyện Ngắn Hay Nhất Của Nguyễn Minh Châu";
        homePage.openShoppingCart(); // Mở trang giỏ hàng
        if (!productDetailPage.isBookInCart(bookName)) {
            // Thêm sách vào giỏ nếu chưa có
            productDetailPage.addToCart(bookName, true);
        }
        homePage.openShoppingCart(); // Mở lại giỏ hàng
        boolean isDeleted = shoppingCartPage.deleteBookFromCart(bookName); // Xóa sách
        Assert.assertTrue(isDeleted, "Sản phẩm không được xóa khỏi giỏ hàng.");
        System.out.println("Test Delete A Book From Cart passed");
    }

    @Test(dataProvider = "bookData")
    public void testDeleteAllBooksFromCart(String[] books) {
        // Kiểm tra xóa toàn bộ sách khỏi giỏ hàng
        for (String book : books) {
            productDetailPage.addToCart(book, true); // Thêm sách vào giỏ
        }
        homePage.openShoppingCart(); // Mở trang giỏ hàng
        shoppingCartPage.deleteAllBooksFromCart(); // Xóa toàn bộ sách
        shoppingCartPage.checkDisplayEmptyShoppingCart(); // Kiểm tra thông báo giỏ hàng rỗng
        System.out.println("Test Delete All Books From Cart passed");
    }

    @Test(dataProvider = "bookData")
    public void testGetTotalWhenCheckBook(String[] books) {
        // Kiểm tra tổng giá trị khi chọn sách trong giỏ hàng
        homePage.openShoppingCart(); // Mở trang giỏ hàng
        shoppingCartPage.deleteAllBooksFromCart(); // Xóa tất cả sách để bắt đầu
        for (String book : books) {
            productDetailPage.addToCart(book, true); // Thêm sách vào giỏ
            homePage.openShoppingCart(); // Mở lại giỏ hàng
            int total = shoppingCartPage.getTotalPriceOfAllBook(); // Lấy tổng giá trị
            shoppingCartPage.verifyTotalAfterCheckBook(total); // Kiểm tra tổng giá trị chính xác
        }
        System.out.println("Test Get Total When Check Book Passed!");
    }
}