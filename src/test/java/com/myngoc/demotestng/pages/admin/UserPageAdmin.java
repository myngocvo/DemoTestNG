package com.myngoc.demotestng.pages.admin;

import com.myngoc.demotestng.common.ExcelHelper;
import com.myngoc.demotestng.common.ValidateHelper;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import java.util.List;

public class UserPageAdmin {
    private final LoginPageAdmin loginPage;
    private final ExcelHelper excelHelper;
    private final ValidateHelper validateHelper;
    private final WebDriver driver;

    @FindBy(xpath = "//span[contains(text(),'Người dùng')]")
    private WebElement userTab;
    @FindBy(xpath = "//tbody//tr")
    private List<WebElement> userList;

    public UserPageAdmin(WebDriver webDriverd) {
        driver = webDriverd;
        validateHelper = new ValidateHelper(driver);
        loginPage = new LoginPageAdmin(driver);
        excelHelper = new ExcelHelper();
        PageFactory.initElements(driver, this);
    }

    private void loginSuccess() {
        excelHelper.setExcelFile("src/test/resources/excelData/loginData.xlsx", "admin");
        loginPage.loginAdmin(excelHelper.getCellStringData("email", 1), excelHelper.getCellStringData("password", 1));
    }

    public boolean isUserExisted(String phoneNumber, String username) {
        if (!validateHelper.verifyUrl("admin")) {
            loginSuccess();
        }
        if (!validateHelper.verifyUrl("user-admin")) {
            validateHelper.clickElement(userTab);
        }
        for (WebElement user : userList) {
            if (user.getText().contains(phoneNumber) && user.getText().contains(username)) {
                System.out.println("User " + username + " is existed!");
                return true;
            }
        }
        System.out.println("User " + username + " is not existed!");
        return false;
    }

    public void deleteUser(String phoneNumber, String username) throws InterruptedException {
        if (!validateHelper.verifyUrl("admin")) {
            loginSuccess();
        }
        if (!validateHelper.verifyUrl("user-admin")) {
            validateHelper.clickElement(userTab);
        }
        for (int i = 0; i < userList.size(); i++) {
            WebElement user = userList.get(i);
            if (user.getText().contains(phoneNumber)) {
                WebElement deleteButton = driver.findElement(By.xpath("//tbody//tr[" + (i + 1) + "]//td[5]//span"));
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deleteButton);
                WebElement confirmDelete = driver.findElement(By.xpath("//span[contains(text(),'Xóa')]"));
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", confirmDelete);
                Thread.sleep(1000);
                Alert alert = driver.switchTo().alert();
                Assert.assertEquals(alert.getText(), "Xóa khách hàng thành công!", "Wrong message!");
                alert.accept();
                System.out.println("User " + username + " is deleted!");
            }
        }
    }
}
