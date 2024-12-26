package com.myngoc.demotestng.pages.admin;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class AddBookPage {
    private WebDriver driver;

    @FindBy(xpath = "//button[contains(text(),\"TRANG CHỦ\")]")
    private WebElement homeModule;

    public AddBookPage(WebDriver driver) {
        this.driver = driver;
    }
}
