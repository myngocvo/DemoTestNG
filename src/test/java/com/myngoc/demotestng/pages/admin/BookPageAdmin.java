package com.myngoc.demotestng.pages.admin;

import com.myngoc.demotestng.Image;
import com.myngoc.demotestng.common.ValidateHelper;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

public class BookPageAdmin {
    private WebDriver driver;
    private ValidateHelper validateHelper;
    private DashboardPageAdmin dashboardPageAdmin;

    @FindBy(className = "mdc-button__label")
    private WebElement addBookButton;
    @FindBy(xpath = "//input[@placeholder=\"Tìm kiếm...\"]")
    private WebElement searchInput;
    @FindBy(xpath = "//tr[contains(@class, 'book-search-result')]")
    private WebElement bookSearchResult;
    //img
    @FindBy(xpath = "//div[@class='imgs-add']//div[1]//input[1]")
    private WebElement addBookImg1Button;
    @FindBy(xpath = "//div[@class='imgs-add']//div[2]//input[1]")
    private WebElement addBookImg2Button;
    @FindBy(xpath = "//div[@class='imgs-add']//div[3]//input[1]")
    private WebElement addBookImg3Button;
    @FindBy(xpath = "//div[@class='imgs-add']//div[4]//input[1]")
    private WebElement addBookImg4Button;
    //addbook
    @FindBy(xpath = "//input[@id='bookNameInput']")
    private WebElement bookNameInput;
    @FindBy(xpath = "//div[@class='publish-year']//input[@type='number']")
    private WebElement publishYearInput;
    @FindBy(xpath = "//div[@class='author-add']//select[@class='ng-untouched ng-pristine ng-valid']")
    private WebElement authorSelect;
    @FindBy(xpath = "//div[@class='category-add']//select[@class='ng-untouched ng-pristine ng-valid']")
    private WebElement categorySelect;
    @FindBy(xpath = "//div[@class='publish-company']//select[@class='ng-untouched ng-pristine ng-valid']")
    private WebElement supplierSelect;
    @FindBy(xpath = "//div[@class='price-add']//input[@type='number']")
    private WebElement priceInput;
    @FindBy(xpath = "//div[@class='quantity-add']//input[@type='number']")
    private WebElement quantityInput;
    @FindBy(xpath = "//div[@class='pricepercent-add']//input[@type='number']")
    private WebElement pricePercentInput;
    @FindBy(xpath = "//div[@class='pages-add']//input[@type='number']")
    private WebElement pageNumberInput;
    @FindBy(xpath = "//div[@class='dimensions-add']//input[@type='text']")
    private WebElement dimensionsInput;
    @FindBy(xpath = "//div[@class='status']//select[@class='ng-untouched ng-pristine ng-valid']")
    private WebElement statusSelect;
    @FindBy(xpath = "//textarea[@id='descriptionInput']")
    private WebElement descriptionInput;
    @FindBy(xpath = "//button[contains(text(),\"Xác nhận\")]")
    private WebElement saveBookButton;
    //delete
    @FindBy(xpath = "(//tr[contains(@class, 'book-search-result')]//mat-icon)[2]")
    private WebElement deleteButton;
    @FindBy(xpath = "//button//span[contains(text(), 'Xóa')]")
    private WebElement confirmDelete;

    public BookPageAdmin(WebDriver driver) {
        this.driver = driver;
        validateHelper = new ValidateHelper(driver);
        dashboardPageAdmin = new DashboardPageAdmin(driver);
        PageFactory.initElements(driver, this);
    }

    public void searchBook(String bookName) {
        searchInput.clear();
        new Actions(driver).click(searchInput).sendKeys(bookName).build().perform();
    }

    public void uploadImages(Image image) {
        addBookImg1Button.sendKeys(image.getImage1());
        addBookImg2Button.sendKeys(image.getImage2());
        addBookImg3Button.sendKeys(image.getImage3());
        addBookImg4Button.sendKeys(image.getImage4());
    }

    public void addBook(String bookName, String publishYear, String author, String category, String supplier, String price, String quantity, String pricePrecent, String pageNumber, String dimention, String status, String description, Image image) throws Exception {
        //validateHelper.clickElement(addBookButton);
        dashboardPageAdmin.openAddBookPage();
        uploadImages(image);
        validateHelper.setText(bookNameInput, bookName);
        validateHelper.setText(publishYearInput, publishYear);
        validateHelper.selectOptionByText(authorSelect, author);
        validateHelper.selectOptionByText(categorySelect, category);
        validateHelper.selectOptionByText(supplierSelect, supplier);
        validateHelper.setText(priceInput, price);
        validateHelper.setText(quantityInput, quantity);
        validateHelper.setText(pricePercentInput, pricePrecent);
        validateHelper.setText(pageNumberInput, pageNumber);
        validateHelper.setText(dimensionsInput, dimention);
        validateHelper.selectOptionByText(statusSelect, status);
        validateHelper.setText(descriptionInput, description);
        validateHelper.scrollToElement(saveBookButton);
        validateHelper.clickElement(saveBookButton);
        Thread.sleep(1000);
    }

    public void deleteBook(String bookName) throws Exception {
        searchBook(bookName);
        String actualSearchResult = bookSearchResult.getText();
        if (actualSearchResult.contains(bookName)) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deleteButton);
            validateHelper.clickElement(confirmDelete);
            Thread.sleep(1000);
            Assert.assertEquals(driver.switchTo().alert().getText(), "Xóa sách thành công", "Delete failed!");
            driver.switchTo().alert().accept();
        }
    }
}