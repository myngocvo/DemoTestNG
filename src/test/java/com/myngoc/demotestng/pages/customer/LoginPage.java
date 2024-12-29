package com.myngoc.demotestng.pages.customer;

import com.myngoc.demotestng.common.ValidateHelper;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

public class LoginPage {
    private final WebDriver driver;
    private final ValidateHelper validateHelper;

    @FindBy(xpath = "//span[contains(text(),\"Tài Khoản\")]")
    private WebElement accountModule;
    @FindBy(xpath = "//input[@name='phoneLogin']")
    private WebElement usernameInput;
    @FindBy(xpath = "//input[@name='PassLogin']")
    private WebElement passwordInput;
    @FindBy(xpath = "//button[@class='btn btn-login']")
    private WebElement loginBtn;
    @FindBy(xpath = "//label[contains(text(),'Quên mật khẩu')]")
    private WebElement forgetPassBtn;
    @FindBy(xpath = "//button[contains(text(),'Đăng Ký')]")
    private WebElement registerBtn;
    @FindBy(css = "div[id=\"formlogin\"] h1:nth-child(1)")
    private WebElement welcomeloginText;


    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.validateHelper = new ValidateHelper(driver);
        PageFactory.initElements(driver, this);
    }

    public MyProfilePage login(String username, String password) {
        validateHelper.clickElement(accountModule);
        Assert.assertTrue(validateHelper.verifyElementText(welcomeloginText, "Chào mừng bạn đến với\nKANN bookstore"), "Welcome text does not match");
        validateHelper.setText(usernameInput, username);
        validateHelper.setText(passwordInput, password);
        validateHelper.clickElement(loginBtn);
        return new MyProfilePage(driver);
    }

    private boolean isSnackbarMessageCorrect(String expectedMessage) throws InterruptedException {
        String actualMessage = validateHelper.getSnackbarMessage();
        return expectedMessage.equals(actualMessage);
    }

    public boolean isLoginSuccessful(String username, String password, boolean expectedResult) {
        try {
            login(username, password);
            if (expectedResult) {
                System.out.println("Username: " + username + ", Password: " + password + ", Login success!");
                return isSnackbarMessageCorrect("Đăng nhập thành công") && validateHelper.verifyUrl("user");
            } else {
                System.out.println("Username: " + username + ", Password: " + password + ", Login failed!");
                return isSnackbarMessageCorrect("Đăng nhập thất bại. Vui lòng kiểm tra lại thông tin đăng nhập");
            }
        } catch (Exception e) {
            System.out.println("Username: " + username + ", Password: " + password + ", Login error!");
            System.err.println("Error during login validation: " + e.getMessage());
            return false;
        } finally {
            if (driver != null) driver.quit();
        }
    }
}
