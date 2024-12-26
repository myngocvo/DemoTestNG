package com.myngoc.demotestng.pages.admin;

import com.myngoc.demotestng.common.ValidateHelper;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

public class DashboardAdminPage {
    private WebDriver driver;
    private ValidateHelper validateHelper;

    @FindBy(xpath = "//mat-list-item[@routerlink=\"/user-admin\"]")
    private WebElement userManagement;

    @FindBy(xpath = "//mat-list-item[@routerlink=\"/order-admin\"]")
    private WebElement orderManagement;

    @FindBy(css = ".mdc-button__label")
    private WebElement addBookBtn;

    public DashboardAdminPage(WebDriver driver) {
        this.driver = driver;
    }

    public OrderManagementPage openOrderManagementPage() {
        validateHelper.clickElement(orderManagement);
        return new OrderManagementPage(driver);
    }

    public AddBookPage openAddBookPage() {
        validateHelper.clickElement(addBookBtn);
        return new AddBookPage(driver);
    }

}
