package com.myngoc.demotestng.testcases.admin_testcases;

import com.myngoc.demotestng.common.BaseSetUp;
import com.myngoc.demotestng.common.ValidateHelper;
import com.myngoc.demotestng.pages.admin.DashboardAdminPage;
import com.myngoc.demotestng.pages.admin.LoginAdminPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class LoginAdminTest extends BaseSetUp {
    private WebDriver driver;
    private LoginAdminPage loginAdminPage;
    private DashboardAdminPage dashboardAdminPage;
    private ValidateHelper validateHelper;

    @BeforeClass
    public void setUp() {
        driver = getDriver();
        dashboardAdminPage = new DashboardAdminPage(driver);
        loginAdminPage = new LoginAdminPage(driver);
        validateHelper = new ValidateHelper(driver);
        PageFactory.initElements(driver, this);
    }

    @Test
    public void loginAdmin() throws InterruptedException {
        dashboardAdminPage = loginAdminPage.loginAdmin("vtmn22070312@gmail.com", "Ngoc@123");
        if ("Đăng nhập thành công".equals(validateHelper.getSnackbarMessage())) {
            Assert.assertTrue(validateHelper.verifyUrl("books"), "Redirect to profile page failed!");
            System.out.println("Login succeeded!");
        } else {
            Assert.fail("Login failed! Snackbar message: " + validateHelper.getSnackbarMessage());
        }
    }

}