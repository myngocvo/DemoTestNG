package com.myngoc.demotestng.testcases.admin_testcases;

// Import các thư viện cần thiết

import com.myngoc.demotestng.common.BaseSetUp;
import com.myngoc.demotestng.common.ExcelHelper;
import com.myngoc.demotestng.pages.admin.LoginPageAdmin;
import com.myngoc.demotestng.pages.admin.UserPageAdmin;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.*;

public class UserAdminTest {
    private WebDriver driver;
    private UserPageAdmin userPage;
    private ExcelHelper excel;

    // Khởi tạo đối tượng ExcelHelper để dùng trong các test
    @BeforeClass
    public void init() {
        excel = new ExcelHelper();
    }

    @BeforeMethod
    public void setUp() {
        // Thiết lập trình duyệt và đăng nhập vào trang Admin
        driver = new BaseSetUp().setupDriver("admin");
        userPage = new UserPageAdmin(driver); // Khởi tạo trang quản lý người dùng
        LoginPageAdmin loginPageAdmin = new LoginPageAdmin(driver);
        PageFactory.initElements(driver, this); // Khởi tạo các phần tử trang bằng PageFactory
        // Đọc thông tin đăng nhập từ file Excel và thực hiện đăng nhập
        excel.setExcelFile("src/test/resources/excelData/loginData.xlsx", "admin");
        loginPageAdmin.loginAdmin(
                excel.getCellStringData("email", 1),
                excel.getCellStringData("password", 1)
        );
    }

    // Đóng trình duyệt sau khi hoàn tất test
    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @DataProvider
    public Object[][] getUserData() {
        // Đọc dữ liệu người dùng từ file Excel để cung cấp cho các test
        excel.setExcelFile("src/test/resources/excelData/registerData.xlsx", "customer");
        int row = excel.getMaxRow(); // Số lượng hàng dữ liệu trong file Excel
        int col = 2; // Chỉ lấy 2 cột: phoneNumber và username
        Object[][] data = new Object[row][col];
        // Lặp qua các hàng và lưu dữ liệu vào mảng 2 chiều
        for (int i = 0; i < row; i++) {
            data[i][0] = excel.getCellStringData("phoneNumber", i + 1); // Lấy số điện thoại
            data[i][1] = excel.getCellStringData("username", i + 1); // Lấy tên người dùng
        }
        return data;
    }

    @Test(dataProvider = "getUserData")
    public void testDeleteUser(String phoneNumber, String username) throws InterruptedException {
        // Kiểm tra nếu người dùng tồn tại, thực hiện xóa
        if (userPage.isUserExisted(phoneNumber, username) && !username.isEmpty()) {
            userPage.deleteUser(phoneNumber, username); // Xóa người dùng theo số điện thoại và tên
        }
    }
}
