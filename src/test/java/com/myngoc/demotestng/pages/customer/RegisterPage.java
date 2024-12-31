package com.myngoc.demotestng.pages.customer;

import com.myngoc.demotestng.common.ValidateHelper;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

public class RegisterPage {
    private final WebDriver driver;
    private final ValidateHelper validateHelper;

    @FindBy(xpath = "//span[contains(text(),\"Tài Khoản\")]")
    private WebElement accountModule;
    @FindBy(xpath = "//button[contains(text(),\"Đăng Ký\")]")
    private WebElement signUpButton;
    @FindBy(xpath = "//input[@name=\"phone\"]")
    private WebElement phoneNumberInput;
    @FindBy(xpath = "//input[@name=\"username\"]")
    private WebElement usernameInput;
    @FindBy(xpath = "//input[@name=\"password\"]")
    private WebElement passwordInput;
    @FindBy(xpath = "//input[@name=\"confirm_password\"]")
    private WebElement confirmPasswordInput;
    @FindBy(xpath = "//button[@id=\"btnregister\"]")
    private WebElement registerButton;

    public RegisterPage(WebDriver driver) {
        this.driver = driver;
        this.validateHelper = new ValidateHelper(driver);
        PageFactory.initElements(driver, this);
    }

    public void register(String phoneNumber, String username, String password, String confirmPassword) {
        validateHelper.clickElement(accountModule);
        validateHelper.clickElement(signUpButton);
        validateHelper.setText(phoneNumberInput, phoneNumber);
        validateHelper.setText(usernameInput, username);
        validateHelper.setText(passwordInput, password);
        validateHelper.setText(confirmPasswordInput, confirmPassword);
        validateHelper.clickElement(registerButton);

    }

    public boolean isRegisterSuccess(String phoneNumber, String username, String password, String confirmPassword, boolean expectedResult) {
        try {
            register(phoneNumber, username, password, confirmPassword);
            String snackBarMessage = validateHelper.getSnackbarMessage();
            if (expectedResult) {
                Assert.assertEquals(snackBarMessage, "Đăng ký thành công!", "Wrong snack bar message!");
                return true;
            } else {
                Assert.assertNotEquals(snackBarMessage, "Đăng ký thành công!", "Wrong snack bar message!");
                return false;
            }
        } catch (Exception e) {
            System.out.println("Username: " + username + ", Password: " + password + ", Sign up error due to: " + e.getMessage());
            return false;
        } finally {
            if (driver != null) driver.quit();
        }
    }
}
