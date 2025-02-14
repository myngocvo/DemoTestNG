package com.myngoc.demotestng.testcases.customer_testcases;

import com.myngoc.demotestng.Address;
import com.myngoc.demotestng.Customer;
import com.myngoc.demotestng.common.BaseSetUp;
import com.myngoc.demotestng.common.ExcelHelper;
import com.myngoc.demotestng.common.JsonHelper;
import com.myngoc.demotestng.common.ValidateHelper;
import com.myngoc.demotestng.pages.customer.HomePage;
import com.myngoc.demotestng.pages.customer.LoginPage;
import com.myngoc.demotestng.pages.customer.MyProfilePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class MyProfileTest {
    // Khai báo các thành phần chính sử dụng trong các test case
    public HomePage homePage;
    public LoginPage loginPage;
    public MyProfilePage myProfilePage;
    public ValidateHelper validateHelper;
    private WebDriver driver;
    private ExcelHelper excelHelper;
    private JsonHelper json;

    @BeforeMethod
    public void setUp() throws Exception {
        // Thiết lập trình duyệt và các trang cần thiết trước khi chạy test
        driver = new BaseSetUp().setupDriver("customer");
        homePage = new HomePage(driver);
        loginPage = new LoginPage(driver);
        validateHelper = new ValidateHelper(driver);
        myProfilePage = new MyProfilePage(driver);
        excelHelper = new ExcelHelper();
        json = new JsonHelper();
        PageFactory.initElements(driver, this);

        // Cấu hình file Excel chứa dữ liệu đầu vào
        excelHelper.setExcelFile("src/test/resources/excelData/loginData.xlsx", "customer");

        // Thực hiện đăng nhập với thông tin từ file Excel
        loginPage.login(excelHelper.getCellStringData("username", 1), excelHelper.getCellStringData("password", 1));
    }

    @AfterMethod
    public void tearDown() {
        // Đóng trình duyệt sau khi hoàn thành test
        if (driver != null) {
            driver.quit();
        }
    }

    // Test cập nhật thông tin hồ sơ khách hàng
    @Test
    public void testUpdateProfile() throws InterruptedException {
        Customer cusInfor;
        if (myProfilePage.getCurrentUser().equals("Võ Thị Mỹ Ngọc")) {
            cusInfor = json.getCustomer("src/test/resources/jsonData/customerData.json", "customer2");
        } else {
            cusInfor = json.getCustomer("src/test/resources/jsonData/customerData.json", "customer1");
        }
        // Cập nhật thông tin cá nhân
        myProfilePage.updateProfile(cusInfor.getName(), cusInfor.getPhoneNumber(), cusInfor.getEmail(), cusInfor.getGender(), cusInfor.getDateOfBirth());
        Assert.assertEquals(validateHelper.getSnackbarMessage(), "Lưu hồ sơ thành công", "Notification is incorrect");
        Thread.sleep(4000);
        // Xác minh thông tin đã được cập nhật chính xác
        myProfilePage.verifyUpdateProfile(cusInfor.getName(), cusInfor.getPhoneNumber(), cusInfor.getEmail(), cusInfor.getGender(), cusInfor.getDateOfBirth());
        System.out.println("Test update profile passed");
    }

    // Test cập nhật với số điện thoại không hợp lệ
    @Test
    public void testIncorrectPhoneNumberUpdateProfile() throws InterruptedException {
        Customer cusInfor = json.getCustomer("src/test/resources/jsonData/customerData.json", "invalidPhoneNumberCustomer");
        myProfilePage.updateProfile(cusInfor.getName(), cusInfor.getPhoneNumber(), cusInfor.getEmail(), cusInfor.getGender(), cusInfor.getDateOfBirth());
        Assert.assertEquals(validateHelper.getSnackbarMessage(), "Số điện thoại không hợp lệ", "Notification is incorrect");
        Thread.sleep(4000);
        System.out.println("Test incorrect phone number update profile passed");
    }

    // Test cập nhật địa chỉ khách hàng
    @Test
    public void testUpdateAdress() throws InterruptedException {
        Address address;
        if (myProfilePage.getCurrentUser().equals("Võ Thị Mỹ Ngọc")) {
            address = json.getAddress("src/test/resources/jsonData/customerData.json", "customer2");
        } else {
            address = json.getAddress("src/test/resources/jsonData/customerData.json", "customer1");
        }
        myProfilePage.updateAdress(address.getCity(), address.getDistrict(), address.getWard());
        Assert.assertEquals(validateHelper.getSnackbarMessage(), "Lưu địa chỉ thành công", "Notification is incorrect");
        myProfilePage.verifyUpdateAdress(address.getCity(), address.getDistrict(), address.getWard());
        System.out.println("Test update address passed.");
    }

    // Test cập nhật địa chỉ trống
    @Test
    public void testEmptyUpdateAdress() throws InterruptedException {
        Address address = json.getAddress("src/test/resources/jsonData/customerData.json", "emptyAddressCustomer");
        myProfilePage.updateAdress(address.getCity(), address.getDistrict(), address.getWard());
        Assert.assertEquals(validateHelper.getSnackbarMessage(), "Vui lòng nhập đầy đủ thông tin!", "Notification is incorrect");
        System.out.println("Test empty update address passed.");
    }

    // Test thay đổi mật khẩu
    @Test
    public void testUpdatePassword() throws Exception {
        String currentPassword = excelHelper.getCellStringData("password", 1);
        String newPassword1 = "MyNgoc@(2207)";
        String newPassword2 = "MyNgoc@(1234)";
        if (currentPassword.equals(newPassword1)) {
            myProfilePage.updatePassword(currentPassword, newPassword2, newPassword2);
            excelHelper.setCellDataByColumn("password", 1, newPassword2);
            excelHelper.setCellDataByColumn("password", 3, newPassword2);
            excelHelper.setCellDataByColumn("password", 4, newPassword2);
        } else {
            myProfilePage.updatePassword(currentPassword, newPassword1, newPassword1);
            excelHelper.setCellDataByColumn("password", 1, newPassword1);
            excelHelper.setCellDataByColumn("password", 3, newPassword1);
            excelHelper.setCellDataByColumn("password", 4, newPassword1);
        }
        Assert.assertEquals(validateHelper.getSnackbarMessage(), "Thay đổi mật khẩu thành công", "Notification is incorrect");
        System.out.println("Test update password passed.");
    }

    // Test nhập sai mật khẩu hiện tại khi thay đổi mật khẩu
    @Test
    public void testCurrentPasswordIncorrectUpdatePassword() throws Exception {
        String currentPassword = "MyNgoc@(220789)";
        String newPassword = "MyNgoc@22072003";
        myProfilePage.updatePassword(currentPassword, newPassword, newPassword);
        Assert.assertEquals(validateHelper.getSnackbarMessage(), "Mật khẩu hiện tại không đúng", "Notification is incorrect");
        System.out.println("Test current password incorrect update password passed.");
    }

    // Test xác nhận mật khẩu không khớp khi thay đổi mật khẩu
    @Test
    public void testConfirmPasswordIncorrectUpdatePassword() throws Exception {
        String currentPassword = excelHelper.getCellStringData("password", 1);
        String newPassword = "MyNgoc@22072003";
        String confirmNewPassword = "MyNgoc@2207200345";
        myProfilePage.updatePassword(currentPassword, newPassword, confirmNewPassword);
        Assert.assertEquals(validateHelper.getSnackbarMessage(), "Mật khẩu không khớp", "Notification is incorrect");
        System.out.println("Test confirm password incorrect update password passed.");
    }
}
