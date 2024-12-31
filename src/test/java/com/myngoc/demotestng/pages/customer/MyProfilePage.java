package com.myngoc.demotestng.pages.customer;

import com.myngoc.demotestng.common.ValidateHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

public class MyProfilePage {
    private WebDriver driver;
    private HomePage homePage;
    private ValidateHelper validateHelper;

    @FindBy(css = ".user-name")
    private WebElement userNameText;
    @FindBy(xpath = "//span[contains(text(),\"Đăng xuất\")]")
    private WebElement logoutButton;
    @FindBy(xpath = "//span[contains(text(),\"Xác Nhận\")]")
    private WebElement confirmLogoutButton;

    //profile
    @FindBy(className = "user-name")
    private WebElement userName;
    @FindBy(xpath = "//*[contains(@class, 'full-width-input')][1]//input")
    private WebElement userNameInput;
    @FindBy(xpath = "//*[contains(@class, 'full-width-input')][2]//input")
    private WebElement phoneNumberInput;
    @FindBy(xpath = "//*[contains(@class, 'full-width-input')][3]//input")
    private WebElement emailInput;
    @FindBy(xpath = "//*[contains(@class, 'checkbox radio-spacing')]")
    private WebElement genderRadioBtn;
    @FindBy(xpath = "//*[contains(@value, 'Nam')]//input")
    private WebElement maleRadioBtn;
    @FindBy(xpath = "//*[contains(@value, 'Nữ')]//input")
    private WebElement femaleRadioBtn;
    @FindBy(xpath = "//*[contains(@value, 'Khác')]//input")
    private WebElement ortherGenderRadioBtn;
    @FindBy(xpath = "//*[contains(@class, 'full-width-input')][4]//input")
    private WebElement birthdayInput;
    @FindBy(xpath = "//button[contains(text(),\"Lưu\")]")
    private WebElement saveProfileButton;

    //adress
    @FindBy(xpath = "//body//app-root//mat-list-item[2]")
    private WebElement adressTab;
    @FindBy(xpath = "//*[contains(@class, 'full-width-input')][1]//input")
    private WebElement provinceCityInput;
    @FindBy(xpath = "//*[contains(@class, 'full-width-input')][2]//input")
    private WebElement districtInput;
    @FindBy(xpath = "//*[contains(@class, 'full-width-input')][3]//input")
    private WebElement wardInput;
    @FindBy(xpath = "//*[contains(@class, 'full-width-input')][4]//input")
    private WebElement fullAddressText;
    @FindBy(xpath = "//button[contains(text(),\"Lưu\")]")
    private WebElement saveAdressButton;

    //password
    @FindBy(xpath = "//body//app-root//mat-list-item[3]")
    private WebElement passwordTab;
    @FindBy(xpath = "//*[contains(@class, 'full-width-input')][1]//input")
    private WebElement currentPasswordInput;
    @FindBy(xpath = "//*[contains(@class, 'full-width-input')][2]//input")
    private WebElement newPasswordInput;
    @FindBy(xpath = "//*[contains(@class, 'full-width-input')][3]//input")
    private WebElement confirmPasswordInput;
    @FindBy(xpath = "//button[contains(text(),\"Lưu\")]")
    private WebElement savePasswordButton;

    public MyProfilePage(WebDriver driver) {
        this.driver = driver;
        this.validateHelper = new ValidateHelper(driver);
        this.homePage = new HomePage(driver);
        PageFactory.initElements(driver, this);
    }

    public boolean isLoggedIn() {
        homePage.openMyProfilePage();
        return validateHelper.verifyElementExistByWebElement(userNameText);
    }

    public void logout() {
        homePage.openMyProfilePage();
        validateHelper.clickElement(logoutButton);
        validateHelper.clickElement(confirmLogoutButton);
    }

    //updateprofile
    public void updateProfile(String username, String phoneNumber, String email, String gender, String birthday) {
        System.out.println("Updating profile with: " + username + ", " + phoneNumber + ", " + email + ", " + gender + ", " + birthday);
        validateHelper.setText(userNameInput, username);
        validateHelper.setText(emailInput, email);
        validateHelper.setText(phoneNumberInput, phoneNumber);
        if (gender.equals("Nam")) {
            maleRadioBtn.click();
        } else if (gender.equals("Nữ")) {
            femaleRadioBtn.click();
        } else {
            ortherGenderRadioBtn.click();
        }
        validateHelper.scrollToElement(saveProfileButton);
        validateHelper.setTextV2(birthdayInput, birthday);
        validateHelper.clickElement(saveProfileButton);
    }

    public void verifyUpdateProfile(String username, String phoneNumber, String email, String gender, String birthday) {
        Assert.assertEquals(userNameInput.getAttribute("value"), username, "Username is incorrect!");
        Assert.assertEquals(phoneNumberInput.getAttribute("value"), phoneNumber, "Phone number is incorrect!");
        Assert.assertEquals(emailInput.getAttribute("value"), email, "Email is incorrect!");
        WebElement genderElement = driver.findElement(By.xpath("//*[contains(@value, '" + gender + "')]//input"));
        Assert.assertTrue(genderElement.isSelected(), "Gender is incorrect!");
        Assert.assertEquals(birthdayInput.getAttribute("value"), birthday, "Birthday is incorrect!");
    }

    //updateadress
    public void updateAdress(String city, String district, String ward) {
        validateHelper.clickElement(adressTab);
        validateHelper.setText(provinceCityInput, city);
        validateHelper.setText(districtInput, district);
        validateHelper.setText(wardInput, ward);
        validateHelper.scrollToElement(saveAdressButton);
        validateHelper.clickElement(saveAdressButton);
    }

    public void verifyUpdateAdress(String city, String district, String ward) {
        Assert.assertEquals(provinceCityInput.getAttribute("value"), city, "City is incorrect!");
        Assert.assertEquals(districtInput.getAttribute("value"), district, "District is incorrect!");
        Assert.assertEquals(wardInput.getAttribute("value"), ward, "Ward is incorrect!");
        Assert.assertEquals(validateHelper.getElementByContainText(ward + ", " + district + ", " + city).getText(), ward + ", " + district + ", " + city + "  ", "Full address is incorrect!");
    }

    //updatepassword
    public void updatePassword(String currentPassword, String newPassword, String confirmPassword) {
        validateHelper.clickElement(passwordTab);
        validateHelper.setText(currentPasswordInput, currentPassword);
        validateHelper.setText(newPasswordInput, newPassword);
        validateHelper.setText(confirmPasswordInput, confirmPassword);
        validateHelper.scrollToElement(savePasswordButton);
        validateHelper.clickElement(savePasswordButton);
    }

    public void verifyUpdatePassword(String currentPassword, String newPassword, String confirmPassword) {
        Assert.assertEquals(currentPasswordInput.getAttribute("value"), currentPassword, "Current password is incorrect!");
        Assert.assertEquals(newPasswordInput.getAttribute("value"), newPassword, "New password is incorrect!");
        Assert.assertEquals(confirmPasswordInput.getAttribute("value"), confirmPassword, "Confirm password is incorrect!");
    }

    public String getCurrentUser() {
        return userName.getText();
    }
}
