package com.myngoc.demotestng.pages.customer;

import com.myngoc.demotestng.common.ValidateHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import java.util.List;

public class HomePage {
    private WebDriver driver;
    private ValidateHelper validateHelper;

    @FindBy(xpath = "//button[contains(text(),\"TRANG CHỦ\")]")
    private WebElement homeModule;
    @FindBy(xpath = "//mat-icon[normalize-space()=\"shopping_cart\"]")
    private WebElement shoppingCartPage;
    @FindBy(xpath = "//mat-card-content[contains(text(),\"GIỎ HÀNG CỦA TÔI\")]")
    private WebElement shoppingCartText;
    @FindBy(xpath = "//input[@placeholder='Tìm kiếm...']")
    private WebElement searchBox;
    @FindBy(xpath = "//div[@id=\"dropdown\"]")
    private WebElement searchResultsDropdown;
    @FindBy(xpath = "//span[@class=\"btn-outline-secondary\"]")
    private WebElement searchResult;
    @FindBy(xpath = "//span[contains(text(),\"Tài Khoản\")]")
    private WebElement accountModule;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.validateHelper = new ValidateHelper(driver);
        PageFactory.initElements(driver, this);
    }

    public ShoppingCartPage openShoppingCartPage() {
        validateHelper.clickElement(shoppingCartPage);
        return new ShoppingCartPage(driver);
    }

    public void openShoppingCartsuccessfully() {
        Assert.assertTrue(validateHelper.verifyUrl("/cart"), " Haven't navigated to the cart page yet!");
        Assert.assertTrue(validateHelper.verifyElementExistByWebElement(shoppingCartText), "This is not a shopping cart page");
    }

    public ShoppingCartPage openMyProfilePage() {
        validateHelper.clickElement(accountModule);
        return new ShoppingCartPage(driver);
    }

    private void enterSearchQuery(String bookName) {
        searchBox.clear();
        new Actions(driver).click(searchBox).sendKeys(bookName).build().perform();
    }

    private boolean isSearchResultsDropdownVisible() {
        return validateHelper.verifyElementExistByWebElement(searchResultsDropdown);
    }

    private List<WebElement> getSearchResults() {
        return searchResultsDropdown.findElements(By.xpath(".//div"));
    }

    private boolean isNoResultsMessageDisplayed(List<WebElement> searchResults) {
        return searchResults.size() == 1 &&
                searchResults.get(0).getText().trim().equalsIgnoreCase("Không tìm thấy kết quả");
    }

    private boolean arePartialResultsMatching(List<WebElement> searchResults, String partialBookName) {
        return searchResults.stream()
                .anyMatch(result -> result.getText().toLowerCase().contains(partialBookName.toLowerCase()));
    }

    public void searchBook(String bookName, boolean shouldExist) {
        enterSearchQuery(bookName);
        validateHelper.waitForPageLoaded();
        Assert.assertTrue(isSearchResultsDropdownVisible(), "Dropdown kết quả tìm kiếm không hiển thị.");

        if (isSearchResultsDropdownVisible()) {
            List<WebElement> searchResults = getSearchResults();

            if (shouldExist) {
                if (arePartialResultsMatching(searchResults, bookName)) {
                    System.out.println("Test passed: Results match search query - " + bookName);
                    for (WebElement result : searchResults) {
                        if (result.getText().toLowerCase().contains(bookName.toLowerCase())) {
                            validateHelper.clickElement(result);
                            return;
                        }
                    }
                } else {
                    System.out.println("Test failed: No matching results found.");
                }
            } else {
                if (isNoResultsMessageDisplayed(searchResults)) {
                    System.out.println("Test passed: 'Không tìm thấy kết quả' message is displayed.");
                } else {
                    System.out.println("Test failed: Unexpected results found.");
                }
            }
        } else {
            System.out.println("Test failed: Search results dropdown is not visible.");
        }
    }

}