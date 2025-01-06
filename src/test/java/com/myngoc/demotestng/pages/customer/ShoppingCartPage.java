package com.myngoc.demotestng.pages.customer;

import com.myngoc.demotestng.common.ValidateHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.text.DecimalFormat;
import java.util.List;

public class ShoppingCartPage {
    private WebDriver driver;
    private ValidateHelper validateHelper;

    @FindBy(xpath = "//img[@alt=\"No products\"]")
    private WebElement noProductImg;
    @FindBy(css = "div[class=\"cart-content\"] p")
    private WebElement noProductText;
    @FindBy(xpath = "//button[contains(text(),\"MUA SẮM NGAY\")]")
    private WebElement noProductBtn;
    private final By cartItems = By.xpath("//*[contains(@class, 'book')]");
    @FindBy(xpath = "//tbody/tr[1]/td[7]/button[1]/mat-icon[1]")
    private WebElement deleteBtn;
    @FindBy(xpath = "//tr[1]//th[1]//input[@type='checkbox']")
    private WebElement checkAll;

    public ShoppingCartPage(WebDriver driver) {
        this.driver = driver;
        this.validateHelper = new ValidateHelper(driver);
        PageFactory.initElements(driver, this);
    }

    public boolean checkDisplayEmptyShoppingCart() {
        boolean isNoProductImgDisplayed = validateHelper.verifyElementExistByWebElement(noProductImg);
        boolean isNoProductTextDisplayed = validateHelper.verifyElementExistByWebElement(noProductText);
        boolean isNoProductBtnDisplayed = validateHelper.verifyElementExistByWebElement(noProductBtn);
        return isNoProductImgDisplayed && isNoProductTextDisplayed && isNoProductBtnDisplayed;
    }

    public List<WebElement> getCartItems() {
        return driver.findElements(cartItems);
    }

    public boolean deleteBookFromCart(String bookName) {
        List<WebElement> cartItems = getCartItems();
        for (WebElement item : cartItems) {
            WebElement bookNameElement = item.findElement(By.xpath("//td[contains(text(),'" + bookName + "')]"));
            if (bookNameElement.getText().equalsIgnoreCase(bookName)) {
                validateHelper.clickElement(deleteBtn);
                validateHelper.waitForPageLoaded();
                return true;
            }
        }
        System.out.println("Product '\" + bookName + \"' does not exist in the shopping cart.");
        return false;
    }

    public void deleteAllBooksFromCart() {
        if (checkDisplayEmptyShoppingCart()) {
            System.out.println("Shopping cart is empty.");
            return;
        }
        List<WebElement> cartItems = getCartItems();
        int productCount = cartItems.size();
        while (productCount > 0) {
            try {
                System.out.println(productCount);
                validateHelper.clickElement(deleteBtn);
                productCount--;
            } catch (Exception e) {
                System.out.println("Error during deleting due to: " + e.getMessage());
            }
        }
    }

    public boolean verifyTotalAfterCheckBook(int total) {
        checkAll.click();
        return validateHelper.verifyElementExistByLocator(By.xpath("//span[contains(text(),'" + getCurrencyFromInt(total).replace("đ", "") + "')]"));
    }

    private int getIntFromCurrency(String bookPrice) {
        bookPrice = bookPrice.replace("đ", "").replace(".", "").replace(",", "");
        return Integer.parseInt(bookPrice);
    }

    private String getCurrencyFromInt(int total) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(total) + "đ";
    }

    public int getTotalPriceOfAllBook() {
        int totalPrice = 0;
        for (int i = 0; i < getCartItems().size(); i++) {
            totalPrice += getIntFromCurrency(driver.findElement(By.xpath("//tr[contains(@class,'book')][" + (i + 1) + "]/td[6]")).getText());
        }
        return totalPrice;
    }
}
