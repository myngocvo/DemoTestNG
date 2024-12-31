package com.myngoc.demotestng.testcases.customer_testcases;

// Import các thư viện và lớp cần thiết

import com.myngoc.demotestng.common.BaseSetUp;
import com.myngoc.demotestng.common.ExcelHelper;
import com.myngoc.demotestng.pages.customer.HomePage;
import com.myngoc.demotestng.pages.customer.LoginPage;
import com.myngoc.demotestng.pages.customer.PaymentPage;
import com.myngoc.demotestng.pages.customer.ProductDetailPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

// Định nghĩa lớp kiểm thử cho chức năng thanh toán
public class PaymentTest {
    private WebDriver driver;
    private HomePage homePage;
    private ProductDetailPage productDetailPage;
    private PaymentPage paymentPage;

    // DataProvider cung cấp dữ liệu đầu vào cho test case
    @DataProvider
    public static Object[][] getPaymentOptions() {
        ExcelHelper excelHelper = new ExcelHelper();

        // Đọc dữ liệu từ file Excel
        excelHelper.setExcelFile("src/test/resources/excelData/paymentData.xlsx", "paymentOptions");
        int row = excelHelper.getMaxRow();
        int col = excelHelper.getMaxCol();

        // Tạo mảng 2 chiều để lưu dữ liệu
        Object[][] options = new Object[row][col];
        for (int i = 0; i < row; i++) {
            options[i][0] = excelHelper.getCellBooleanData("isHaveAddress", (i + 1));
            options[i][1] = excelHelper.getCellStringData("address", (i + 1));
            options[i][2] = excelHelper.getCellBooleanData("isHaveVoucher", (i + 1));
            options[i][3] = excelHelper.getCellNumericData("discountPercent", (i + 1));
            options[i][4] = excelHelper.getCellNumericData("maxValueDiscount", (i + 1));
        }
        return options; // Trả về dữ liệu cho test case
    }

    // Phương thức khởi tạo trước khi chạy từng test case
    @BeforeMethod
    public void setUp() {
        // Khởi tạo driver với trình duyệt Chrome và vai trò customer
        driver = new BaseSetUp().setupDriver("chrome", "customer");

        // Khởi tạo các trang cần dùng
        homePage = new HomePage(driver);
        LoginPage loginPage = new LoginPage(driver);
        productDetailPage = new ProductDetailPage(driver);
        ExcelHelper excelHelper = new ExcelHelper();
        paymentPage = new PaymentPage(driver);

        // Khởi tạo các phần tử trên trang với PageFactory
        PageFactory.initElements(driver, this);

        // Đọc dữ liệu đăng nhập từ file Excel
        excelHelper.setExcelFile("src/test/resources/excelData/customerLoginData.xlsx", "dataLogin");
        loginPage.login(excelHelper.getCellStringData("username", 1), excelHelper.getCellStringData("password", 1));
    }

    // Phương thức dọn dẹp tài nguyên sau khi chạy xong test case
    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit(); // Đóng driver nếu còn hoạt động
        }
    }

    // Test case kiểm thử quy trình thanh toán thành công
    @Test(dataProvider = "getPaymentOptions")
    public void testPaymentSuccess(boolean isHaveAddress, String address, boolean isHaveVoucher, double discountPercent, double maxValueDiscount) throws InterruptedException {
        // Danh sách các cuốn sách cần thêm vào giỏ hàng
        String[] books = {
                "CÁCH NỀN KINH TẾ VẬN HÀNH Niềm tin, sự sụp đổ và những lời tiên tri tự đúng",
                "Lời Thú Tội Của Một Sát Thủ Kinh Tế - Bìa Cứng (Tái Bản 2023)",
                "Tuyển tập Vũ Trọng Phụng",
                "Tuyển Tập Truyện Ngắn Hay Nhất Của Nguyễn Minh Châu",
                "Văn Học Trong Nhà Trường: Thơ Nguyễn Khuyến"};

        // Mở giỏ hàng trên trang chủ
        homePage.openShoppingCart();

        // Thêm từng cuốn sách vào giỏ hàng
        for (String book : books) {
            productDetailPage.addToCart(book, true);
        }

        // Mở lại giỏ hàng sau khi thêm sách
        homePage.openShoppingCart();

        // Lấy thông tin về các cuốn sách trong giỏ hàng
        Object[][] bookInfo = paymentPage.getBookInfo(books);

        // Tiến hành thanh toán với các tùy chọn đã cung cấp
        paymentPage.makeAPayment(isHaveAddress, address, isHaveVoucher, discountPercent, maxValueDiscount);

        // Xác minh thông tin thanh toán
        paymentPage.verifyPaymentInfo(bookInfo, isHaveVoucher, discountPercent, maxValueDiscount);

        // Nhấn nút đặt hàng
        paymentPage.clickOrder();
    }
}
