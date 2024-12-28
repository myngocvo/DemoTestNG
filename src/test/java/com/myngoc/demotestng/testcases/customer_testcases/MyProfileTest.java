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
import org.testng.annotations.*;

public class MyProfileTest {
    private WebDriver driver;
    public HomePage homePage;
    public LoginPage loginPage;
    public MyProfilePage myProfilePage;
    public ValidateHelper validateHelper;
    private ExcelHelper excelHelper;
    private JsonHelper json;

    @BeforeMethod
    public void setUp() throws Exception {
        driver = new BaseSetUp().setupDriver("chrome", "customer");
        homePage = new HomePage(driver);
        loginPage = new LoginPage(driver);
        validateHelper = new ValidateHelper(driver);
        myProfilePage = new MyProfilePage(driver);
        excelHelper = new ExcelHelper();
        json = new JsonHelper();
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

//    @BeforeMethod()
//    public void login() throws Exception {
//        excel.setExcelFile("src/test/resources/loginData/customerLoginData.xlsx", "dataLogin");
//        loginPage.login(excel.getCellStringData("username", 1), excel.getCellStringData("password", 1));
//    }

    @Test
    public void testUpdateProfile() throws InterruptedException {
        Customer cusInfor;
        if (myProfilePage.getCurrentUser().equals("Võ Thị Mỹ Ngọc")) {
            cusInfor = json.getCustomer("src/test/resources/jsonData/customerData.json", "customer2");
        } else {
            cusInfor = json.getCustomer("src/test/resources/jsonData/customerData.json", "customer1");
        }

        myProfilePage.updateProfile(cusInfor.getName(), cusInfor.getPhoneNumber(), cusInfor.getEmail(), cusInfor.getGender(), cusInfor.getDateOfBirth());
        Assert.assertEquals(validateHelper.getSnackbarMessage(), "Lưu hồ sơ thành công", "Notification is incorrect");
        Thread.sleep(4000);
        myProfilePage.verifyUpdateProfile(cusInfor.getName(), cusInfor.getPhoneNumber(), cusInfor.getEmail(), cusInfor.getGender(), cusInfor.getDateOfBirth());
        System.out.println("Test update profile passed");
    }

    @Test
    public void testIncorrectPhoneNumberUpdateProfile() throws InterruptedException {
        Customer cusInfor = json.getCustomer("src/test/resources/jsonData/customerData.json", "invalidPhoneNumberCustomer");
        myProfilePage.updateProfile(cusInfor.getName(), cusInfor.getPhoneNumber(), cusInfor.getEmail(), cusInfor.getGender(), cusInfor.getDateOfBirth());
        Assert.assertEquals(validateHelper.getSnackbarMessage(), "Số điện thoại không hợp lệ", "Notification is incorrect");
        Thread.sleep(4000);
        System.out.println("Test incorect phone number update profile passed");
    }

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

    @Test
    public void testEmptyUpdateAdress() throws InterruptedException {
        Address address = json.getAddress("src/test/resources/jsonData/customerData.json", "emptyAddressCustomer");

        myProfilePage.updateAdress(address.getCity(), address.getDistrict(), address.getWard());
        Assert.assertEquals(validateHelper.getSnackbarMessage(), "Vui lòng nhập đầy đủ thông tin!", "Notification is incorrect");
        System.out.println("Test empty update address passed.");
    }

    @Test
    public void testUpdatePassword() throws Exception {
        String currentPassword = excelHelper.getCellStringData("password", 1);
        String newPassword1 = "MyNgoc@(2207)";
        String newPassword2 = "MyNgoc@(1234)";
        if (currentPassword.equals(newPassword1)) {
            excelHelper.setCellDataByColumn("password", 1, newPassword2);
            myProfilePage.updatePassword(currentPassword, newPassword2, newPassword2);
        } else {
            excelHelper.setCellDataByColumn("password", 1, newPassword1);
            myProfilePage.updatePassword(currentPassword, newPassword1, newPassword1);
        }
        Assert.assertEquals(validateHelper.getSnackbarMessage(), "Thay đổi mật khẩu thành công", "Notification is incorrect");
        System.out.println("Test update password passed.");
    }

    @Test
    public void testCurrentPasswordIncorrectUpdatePassword() throws Exception {
        String currentPassword = "MyNgoc@(220789)";
        String newPassword = "MyNgoc@22072003";

        myProfilePage.updatePassword(currentPassword, newPassword, newPassword);
        Assert.assertEquals(validateHelper.getSnackbarMessage(), "Mật khẩu hiện tại không đúng", "Notification is incorrect");
        System.out.println("Test current password incorrect update password passed.");
    }

    @Test
    public void testConfirmPasswordIncorrectUpdatePassword() throws Exception {
        String currentPassword = excelHelper.getCellStringData("password", 1);
        String newPassword = "MyNgoc@22072003";
        String confirmNewPassword = "MyNgoc@2207200345";

        myProfilePage.updatePassword(currentPassword, newPassword, confirmNewPassword);
        Assert.assertEquals(validateHelper.getSnackbarMessage(), "Mật khẩu không khớp", "Notification is incorrect");
        System.out.println("Test current password incorrect update password passed.");
    }
}
