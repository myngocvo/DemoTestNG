package com.myngoc.demotestng.pages.customer;

import com.myngoc.demotestng.common.ValidateHelper;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class RegisterPage {
    private WebDriver driver;
    private ValidateHelper validateHelper;

    @FindBy(xpath = "")
    private WebElement firstName;


    public RegisterPage(WebDriver driver) {
        this.driver = driver;
        this.validateHelper = new ValidateHelper(driver);
        PageFactory.initElements(driver, this);
    }

    public void register(String phoneNumber, String username, String password, String confirmPassword) {
//        validateHelper.clickElement();
    }

    public void isRegisterSuccess(String phoneNumber, String username, String password, String confirmPassword, boolean status) {
    }
}
