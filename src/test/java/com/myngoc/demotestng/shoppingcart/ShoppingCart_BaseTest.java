package com.myngoc.demotestng.shoppingcart;

import com.myngoc.demotestng.BaseTest;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;

public class ShoppingCart_BaseTest extends BaseTest {
    //Tìm sách
    public void searchBook(String bookName) {
        WebElement searchBox = WaitForElementVisibleXpath(30, "//input[@placeholder='Tìm kiếm...']");
        new Actions(driver).click(searchBox).sendKeys(bookName).build().perform();
    }

    //Chọn sách từ kết quả tìm kiếm
    public void selectBookFromResults(String bookName) {
        WaitForElementVisibleXpath(30, "//div[@id=\"dropdown\"]//div[@class=\"cartegory-right-bottom-item ng-star-inserted\"]").click();
        String cartItemTitle = WaitForElementVisibleXpath(30, "//div[@class=\"product-right-name\"]//h1[contains(text(),\"" + bookName + "\")]").getText();
    }

    //Thêm sách vào giỏ hàng
    public void addToCart() throws InterruptedException {
        WaitForElementVisibleXpath(30, "//span[contains(text(),\"THÊM VÀO GIỎ HÀNG\")]").click();
        Thread.sleep(3000);
    }

    //Kiểm tra alert
    public void verifyAlert(String expectedText) throws InterruptedException {
        Alert alert = driver.switchTo().alert();
        String alertText = alert.getText();
        Assert.assertEquals(alertText, expectedText, "Thông báo trên alert không đúng");
        alert.accept();
    }

    //Kiểm tra sách trong giỏ hàng
    public void verifyBookInCart(String bookName) {
        WaitForElementVisibleXpath(30, "//span[contains(text(),\"Giỏ Hàng\")]").click();
        String actualBookTitle = WaitForElementVisibleXpath(30, "//td[contains(text(),\"" + bookName + "\")]").getText();
        Assert.assertEquals(actualBookTitle, bookName, "Không tìm thấy sách vừa thêm vào");
    }

}
