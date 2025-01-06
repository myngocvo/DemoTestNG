package com.myngoc.demotestng.testcases.customer_testcases;

import com.myngoc.demotestng.common.BaseSetUp;
import com.myngoc.demotestng.common.ExcelHelper;
import com.myngoc.demotestng.common.ValidateHelper;
import com.myngoc.demotestng.pages.customer.HomePage;
import com.myngoc.demotestng.pages.customer.LoginPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class HomePageTest {
    private WebDriver driver;
    private LoginPage loginPage;
    private HomePage homePage;
    private ValidateHelper validateHelper;
    private ExcelHelper excelHelper;

    // Phương thức thiết lập trước mỗi test case
    @BeforeMethod
    public void setUp() {
        // Khởi tạo trình duyệt cho người dùng "customer"
        driver = new BaseSetUp().setupDriver("customer");

        // Khởi tạo các trang liên quan và công cụ hỗ trợ
        homePage = new HomePage(driver);
        loginPage = new LoginPage(driver);
        validateHelper = new ValidateHelper(driver);
        excelHelper = new ExcelHelper();

        // Khởi tạo các thành phần của trang
        PageFactory.initElements(driver, this);
    }

    // Phương thức dọn dẹp sau mỗi test case
    @AfterMethod
    public void tearDown() {
        // Đóng trình duyệt nếu nó còn mở
        if (driver != null) {
            driver.quit();
        }
    }

//    // Test case: Mở giỏ hàng khi đã đăng nhập
//    @Test()
//    public void openShoppingCartPage_login() {
//        // Đọc thông tin đăng nhập từ file Excel
//        excelHelper.setExcelFile("src/test/resources/excelData/loginData.xlsx", "customer");
//        loginPage.login(excelHelper.getCellStringData("username", 1), excelHelper.getCellStringData("password", 1));
//
//        // Đợi trang tải hoàn toàn
//        validateHelper.waitForPageLoaded();
//
//        // Mở giỏ hàng và kiểm tra
//        homePage.openShoppingCart();
//        homePage.verifyopenShoppingCart();
//    }
//
//    // Test case: Mở giỏ hàng khi chưa đăng nhập
//    @Test()
//    public void openShoppingCartPage_nologin() throws InterruptedException {
//        // Đợi trang tải hoàn toàn
//        validateHelper.waitForPageLoaded();
//
//        // Thử mở giỏ hàng và kiểm tra thông báo lỗi
//        homePage.openShoppingCart();
//        String snackbarMessage = validateHelper.getSnackbarMessage();
//        Assert.assertEquals(snackbarMessage, "Vui lòng đăng nhập để xem giỏ hàng", "Notification is incorrect!");
//    }

    // Test case: Tìm kiếm sách và có kết quả khớp chính xác
    @Test()
    public void searchBookMatchingResults() {
        String bookName = "Giải Thích Ngữ Pháp Tiếng Anh (Tái Bản 2022)";
        homePage.searchBook(bookName, true);
    }

    // Test case: Tìm kiếm sách và có kết quả khớp một phần
    @Test()
    public void searchBookMatchingPartialResults() {
        String bookName = "Tuyển tập Vũ ";
        homePage.searchBook(bookName, true);
    }

    // Test case: Tìm kiếm sách không có kết quả
    @Test()
    public void searchBookNoResults() {
        String bookName = "Nonexistent";
        homePage.searchBook(bookName, false);
    }
}
