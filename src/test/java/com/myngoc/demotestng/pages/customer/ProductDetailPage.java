package com.myngoc.demotestng.pages.customer;

import com.myngoc.demotestng.common.ValidateHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

public class ProductDetailPage {
    private WebDriver driver;
    private HomePage homePage;
    private ValidateHelper validateHelper;
    private MyProfilePage myProfilePage;

    @FindBy(xpath = "//span[contains(text(),\"THÊM VÀO GIỎ HÀNG\")]")
    private WebElement addToCartButton;
    @FindBy(xpath = "//span[normalize-space()=\"MUA NGAY\"]")
    private WebElement buyNowButton;
    @FindBy(css = "div[class=\"product-right-name\"] h1")
    private WebElement bookName;
    @FindBy(xpath = "//div[@class=\"product-content row ng-star-inserted\"]//div[@class=\"product-right-price\"]//p[1]")
    private WebElement bookPrice;
    @FindBy(xpath = "//input[@type=\"number\"]")
    private WebElement bookQuantity;

    public ProductDetailPage(WebDriver driver) {
        this.driver = driver;
        this.homePage = new HomePage(driver);
        this.validateHelper = new ValidateHelper(driver);
        this.myProfilePage = new MyProfilePage(driver);
        PageFactory.initElements(driver, this);
    }

    public void addToCart(String bookName, boolean shouldExist) {
        homePage.searchBook(bookName, true);
        validateHelper.clickElement(addToCartButton);
    }

    public boolean isBookInCart(String bookName) {
        homePage.openShoppingCart();
        return validateHelper.verifyElementExistByLocator(By.xpath("//td[contains(text(),'" + bookName + "')]"));
    }

    public void verifyAddToCartMessage(String bookName) {
        boolean isBookAlreadyInCart = isBookInCart(bookName);
        addToCart(bookName, true);
        String actualMessage = validateHelper.getSnackbarMessage();
        if (isBookAlreadyInCart) {
            Assert.assertEquals(actualMessage, "Sản phẩm đã có trong giỏ hàng", "Incorrect notification when the product already exists!");
        } else {
            Assert.assertEquals(actualMessage, "Thêm vào giỏ hàng thành công", "The message was incorrect when the product was first added!");
        }
    }

    public void verifyAddToCartMessage_nologin(String bookName) {
        addToCart(bookName, true);
        Assert.assertEquals(validateHelper.getSnackbarMessage(), "Đăng nhập để thêm vào giỏ hàng", "Notification is incorrect!");
    }

}
