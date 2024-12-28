package com.myngoc.demotestng.pages.admin;

import com.myngoc.demotestng.common.ValidateHelper;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class DashboardAdminPage {
    private WebDriver driver;
    private ValidateHelper validateHelper;

    @FindBy(xpath = "//mat-list-item[@routerlink=\"/user-admin\"]")
    private WebElement userManagement;
    @FindBy(xpath = "//mat-list-item[@routerlink=\"/order-admin\"]")
    private WebElement orderManagement;
    @FindBy(css = ".mdc-button__label")
    private WebElement addBookButton;
    @FindBy(xpath = "//body//app-root//mat-list-item[8]")
    private WebElement logoutButton;
    @FindBy(xpath = "//span[contains(text(),\"Xác Nhận\")]")
    private WebElement confirmLogout;

    public DashboardAdminPage(WebDriver driver) {
        this.driver = driver;
        validateHelper = new ValidateHelper(driver);
        PageFactory.initElements(driver, this);
    }

    public OrderManagementPage openOrderManagementPage() {
        validateHelper.clickElement(orderManagement);
        return new OrderManagementPage(driver);
    }

    public BookPage openAddBookPage() {
        validateHelper.clickElement(addBookButton);
        return new BookPage(driver);
    }

    public void logout() {
        validateHelper.clickElement(logoutButton);
        validateHelper.clickElement(confirmLogout);
    }

}
