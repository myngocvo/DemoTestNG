package com.myngoc.demotestng.login;

import com.myngoc.demotestng.BaseTest;
import org.openqa.selenium.Alert;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.Objects;


public class LoginPageTest extends BaseTest {
    @BeforeMethod
    public void setUpDriver() {
        setUp();
    }

    @AfterMethod
    public void tearDownDriver() {
        tearDown();
    }

    @DataProvider(name = "loginData")
    public Object[][] createData() {
        return new Object[][]{
                {"0932877995", "Ngoc@123", true},
                {"0932877995", "Nnn@123", false},
                {"0384753419", "Ngoc@123", false},
                {"0932877995", "", false},
                {"", "Ngoc@123", false},
                {"", "", false}
        };
    }

    @Test(dataProvider = "loginData")
    public void testLogin(String username, String password, boolean expected) {
        driver.navigate().to("http://localhost:4200/home");

        WaitForElementVisibleXpath(30, "//span[contains(text(),\"Tài Khoản\")]").click();

        WaitForElementVisibleXpath(30, "//input[@name=\"phoneLogin\"]").sendKeys(username);

        WaitForElementVisibleXpath(30, "//input[@name=\"PassLogin\"]").sendKeys(password);

        WaitForElementVisibleXpath(30, "//button[@class=\"btn btn-login\"]").click();

        String alertMessage = null;
        boolean isUserPage = false;

        try {
            Alert alert = driver.switchTo().alert();
            alertMessage = alert.getText();
            alert.accept();
        } catch (Exception ignored) {
            isUserPage = Objects.equals(driver.getCurrentUrl(), "http://localhost:4200/user");
        }

        if (expected) {
            Assert.assertTrue(isUserPage, "Đăng nhập thành công nhưng khong chuyển hướng được");
        } else {
            Assert.assertNotNull(alertMessage, "Không nhận được thông báo khi đăng nhập thất bại");
            System.out.println("Thông báo alert: " + alertMessage);

            Assert.assertEquals(alertMessage, " Đăng nhập không thành công ", "Thông báo không đúng.");
        }
    }
}
