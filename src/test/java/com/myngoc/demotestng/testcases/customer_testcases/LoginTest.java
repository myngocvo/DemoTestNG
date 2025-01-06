package com.myngoc.demotestng.testcases.customer_testcases;

import com.myngoc.demotestng.common.BaseSetUp;
import com.myngoc.demotestng.common.ExcelHelper;
import com.myngoc.demotestng.pages.customer.HomePage;
import com.myngoc.demotestng.pages.customer.LoginPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.*;

public class LoginTest {
    public HomePage homePage; // Trang chủ, nơi chuyển hướng sau khi đăng nhập
    public LoginPage loginPage; // Trang đăng nhập
    private WebDriver driver; // Trình điều khiển WebDriver
    private ExcelHelper excel; // Công cụ hỗ trợ xử lý file Excel

    @BeforeClass
    public void init() {
        // Khởi tạo đối tượng ExcelHelper để xử lý file dữ liệu đăng nhập
        excel = new ExcelHelper();
    }

    @BeforeMethod
    public void setUp() {
        // Khởi tạo trình duyệt WebDriver và khởi tạo các trang cần sử dụng
        driver = new BaseSetUp().setupDriver("customer");
        homePage = new HomePage(driver);
        loginPage = new LoginPage(driver);
        PageFactory.initElements(driver, this); // Khởi tạo các đối tượng PageFactory
    }

    @AfterClass
    public void tearDown() {
        // Đóng trình duyệt sau khi tất cả các test case hoàn thành
        if (driver != null) {
            driver.quit();
        }
    }

    @DataProvider(name = "dataLogin")
    public Object[][] dataLogin() {
        // Đọc dữ liệu từ file Excel để cung cấp dữ liệu đăng nhập cho các test case
        excel.setExcelFile("src/test/resources/excelData/loginData.xlsx", "customer");
        int row = excel.getMaxRow(); // Lấy số lượng hàng trong sheet Excel
        int col = excel.getMaxCol(); // Lấy số lượng cột trong sheet Excel
        Object[][] data = new Object[row][col]; // Khởi tạo mảng lưu dữ liệu
        for (int i = 0; i < row; i++) {
            // Lấy dữ liệu từng cột
            data[i][0] = excel.getCellStringData("username", i + 1);
            data[i][1] = excel.getCellStringData("password", i + 1);
            data[i][2] = excel.getCellBooleanData("status", i + 1); // Giá trị boolean cho kết quả mong đợi
        }
        return data;
    }

    @Test(dataProvider = "dataLogin")
    public void login(String username, String password, boolean expectedResult) {
        // Thực hiện kiểm thử đăng nhập
        boolean result = loginPage.isLoginSuccessful(username, password, expectedResult);
        if (result) {
            // Nếu kết quả đúng với mong đợi, in "Passed"
            System.out.println("Test Login with Username: " + username + ", Password: " + password + " Passed!");
        } else {
            // Nếu kết quả không đúng với mong đợi, in "Failed"
            System.out.println("Test Login with Username: " + username + ", Password: " + password + " Failed!");
        }
    }
}
