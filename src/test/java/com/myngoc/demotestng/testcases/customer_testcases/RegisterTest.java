package com.myngoc.demotestng.testcases.customer_testcases;

import com.myngoc.demotestng.common.BaseSetUp;
import com.myngoc.demotestng.common.ExcelHelper;
import com.myngoc.demotestng.pages.customer.RegisterPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.*;

public class RegisterTest {
    private RegisterPage registerPage;
    private WebDriver driver;
    private ExcelHelper excel;

    // Khởi tạo đối tượng ExcelHelper để xử lý file dữ liệu
    @BeforeClass
    public void init() {
        excel = new ExcelHelper();
    }

    // Khởi tạo trình duyệt WebDriver và thiết lập trang đăng ký
    @BeforeMethod
    public void setUp() {
        driver = new BaseSetUp().setupDriver("customer");
        registerPage = new RegisterPage(driver);
        PageFactory.initElements(driver, this);
    }

    // Đóng trình duyệt sau khi tất cả các test hoàn thành
    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @DataProvider()
    public Object[][] registerData() {
        // Đọc dữ liệu từ file Excel để cung cấp cho các test case
        excel.setExcelFile("src/test/resources/excelData/registerData.xlsx", "customer");
        int row = excel.getMaxRow(); // Số lượng hàng trong sheet Excel
        int col = excel.getMaxCol(); // Số lượng cột trong sheet Excel
        Object[][] data = new Object[row][col - 1];
        for (int i = 0; i < row; i++) {
            // Lấy dữ liệu từ từng cột và lưu vào mảng
            data[i][0] = excel.getCellStringData("phoneNumber", i + 1);
            data[i][1] = excel.getCellStringData("username", i + 1);
            data[i][2] = excel.getCellStringData("password", i + 1);
            data[i][3] = excel.getCellStringData("confirmPassword", i + 1);
            data[i][4] = excel.getCellStringData("message", i + 1);
        }
        return data;
    }

    @Test(dataProvider = "registerData")
    public void testRegister(String phoneNumber, String username, String password, String confirmPassword, String message) {
        // Gọi phương thức kiểm thử đăng ký trên trang RegisterPage
        boolean testResult = registerPage.testRegister(phoneNumber, username, password, confirmPassword, message);
        if (testResult) {
            // In kết quả kiểm thử thành công
            System.out.println("Test register with phone number: " + phoneNumber + ", Password: " + password + " Passed!");
        } else {
            // In kết quả kiểm thử thất bại
            System.out.println("Test register with phone number: " + phoneNumber + ", Password: " + password + " Failed!");
        }
    }
}
