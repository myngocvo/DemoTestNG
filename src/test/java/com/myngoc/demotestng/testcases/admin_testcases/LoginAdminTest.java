package com.myngoc.demotestng.testcases.admin_testcases;

// Import các thư viện cần thiết

import com.myngoc.demotestng.common.BaseSetUp;
import com.myngoc.demotestng.common.ExcelHelper;
import com.myngoc.demotestng.pages.admin.LoginPageAdmin;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.*;

public class LoginAdminTest extends BaseSetUp {
    private WebDriver driver; // Đối tượng WebDriver để điều khiển trình duyệt
    private LoginPageAdmin loginPageAdmin; // Trang đăng nhập của Admin
    private ExcelHelper excel; // Đối tượng hỗ trợ thao tác với file Excel

    // Khởi tạo đối tượng ExcelHelper
    @BeforeClass
    public void initialize() {
        excel = new ExcelHelper();
    }

    // Thiết lập WebDriver và khởi tạo các trang cần thiết
    @BeforeMethod
    public void setUp() {
        driver = new BaseSetUp().setupDriver("admin");
        loginPageAdmin = new LoginPageAdmin(driver);
        PageFactory.initElements(driver, this); // Khởi tạo các phần tử của trang bằng PageFactory
    }

    @AfterClass
    public void tearDown() {
        // Đóng trình duyệt và giải phóng tài nguyên
        if (driver != null) {
            driver.quit(); // Đóng trình duyệt
        }
        if (excel != null) {
            excel.closeWorkbook(); // Đóng file Excel
        }
    }

    @DataProvider(name = "dataLoginAdmin", parallel = true)
    public Object[][] dataLoginAdmin() {
        // Đọc dữ liệu từ file Excel để cung cấp cho các test case
        excel.setExcelFile("src/test/resources/excelData/loginData.xlsx", "admin");
        int row = excel.getMaxRow(); // Số hàng dữ liệu trong file Excel
        int col = excel.getMaxCol(); // Số cột dữ liệu trong file Excel
        Object[][] data = new Object[row][col];
        // Lấy dữ liệu từng hàng và lưu vào mảng 2 chiều
        for (int i = 0; i < row; i++) {
            data[i][0] = excel.getCellStringData("email", i + 1); // Lấy email
            data[i][1] = excel.getCellStringData("password", i + 1); // Lấy password
            data[i][2] = excel.getCellBooleanData("status", i + 1); // Lấy trạng thái (true/false)
        }
        return data;
    }

    @Test(dataProvider = "dataLoginAdmin")
    public void loginAdmin(String email, String password, boolean expectedResult) {
        // Kiểm tra đăng nhập admin với các thông tin từ file Excel
        loginPageAdmin.verifyLoginAdmin(email, password, expectedResult);
    }
}
