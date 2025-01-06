package com.myngoc.demotestng.pages.admin;

import com.myngoc.demotestng.common.BaseSetUp;
import com.myngoc.demotestng.common.ValidateHelper;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

public class LoginPageAdmin extends BaseSetUp {
    private final WebDriver driver;
    private final ValidateHelper validateHelper;

    @FindBy(css = "input[placeholder=\"Email\"]")
    private WebElement emailInput;
    @FindBy(css = "input[placeholder=\"Mật khẩu\"]")
    private WebElement passwordInput;
    @FindBy(css = "div[class=\"login\"] button[type=\"submit\"]")
    private WebElement submitBtn;
    @FindBy(xpath = "//label[contains(text(),\"N LÝ\")]")
    private WebElement loginPageText;


    public LoginPageAdmin(WebDriver driver) {
        this.driver = driver;
        this.validateHelper = new ValidateHelper(driver);
        PageFactory.initElements(driver, this);
    }

    public void loginAdmin(String emailValue, String passwordValue) {
        Assert.assertTrue(validateHelper.verifyElementText(loginPageText, "QUẢN LÝ KANNBOOKSTORE"));
        validateHelper.setText(emailInput, emailValue);
        validateHelper.setText(passwordInput, passwordValue);
        validateHelper.clickElement(submitBtn);
        new DashboardPageAdmin(driver);
    }

    public void verifyLoginAdmin(String email, String password, boolean expectedResult) {
        try {
            loginAdmin(email, password);
            if (expectedResult) {
                Thread.sleep(800);
                Assert.assertTrue(validateHelper.verifyUrl("books"), "Redirect to book page failed!");
                Assert.assertEquals(validateHelper.getSnackbarMessage(), "Đăng nhập thành công");
                System.out.println("Email: " + email + ", Password: " + password + " Passed!");
            } else {
                Assert.assertEquals(validateHelper.getSnackbarMessage(), "Đăng nhập thất bại. Kiểm tra lại tài khoản và mật khẩu");
                Assert.assertTrue(validateHelper.verifyUrl("admin"), "Redirect to profile page failed!");
                System.out.println("Email: " + email + ", Password: " + password + " Failed!");
            }
        } catch (Exception e) {
            System.err.println("Error during login validation: " + e.getMessage());
        } finally {
            if (driver != null) driver.quit();
        }
    }
}
