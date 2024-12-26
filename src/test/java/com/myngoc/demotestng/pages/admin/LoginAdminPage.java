package com.myngoc.demotestng.pages.admin;

import com.myngoc.demotestng.common.BaseSetUp;
import com.myngoc.demotestng.common.ValidateHelper;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

public class LoginAdminPage extends BaseSetUp {
    private WebDriver drivre;
    private ValidateHelper validateHelper;

    @FindBy(css = "input[placeholder=\"Email\"]")
    private WebElement emailInput;
    @FindBy(css = "input[placeholder=\"Mật khẩu\"]")
    private WebElement passwordInput;
    @FindBy(css = "div[class=\"login\"] button[type=\"submit\"]")
    private WebElement submitBtn;
    @FindBy(xpath = "//label[contains(text(),\"N LÝ\")]")
    private WebElement loginPageText;


    public LoginAdminPage(WebDriver driver) {
        this.driver = driver;
        this.validateHelper = new ValidateHelper(driver);
        PageFactory.initElements(driver, this);
    }

    public DashboardAdminPage loginAdmin(String emailValue, String passwordValue) {
        Assert.assertTrue(validateHelper.verifyElementText(loginPageText, "QUẢN LÝ KANNBOOKSTORE"));
        validateHelper.setText(emailInput, emailValue);
        validateHelper.setText(passwordInput, passwordValue);
        validateHelper.clickElement(submitBtn);
        return new DashboardAdminPage(driver);
    }

}
