package com.myngoc.demotestng.shoppingcart;

import com.myngoc.demotestng.BaseTest;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

public class Add_ShoppingCart extends BaseTest {
    ShoppingCart_BaseTest cart_baseTest = new ShoppingCart_BaseTest();

    @BeforeTest
    public void setUpDriver() {
        setUp();
    }

    @AfterTest
    public void tearDownDriver() {
        tearDown();
    }

    @Test
    public void noLogin_Add_ShoppingCart() throws InterruptedException {
        String bookName = "Tuyển tập Nam Cao";
        Thread.sleep(2000);
        cart_baseTest.searchBook(bookName);
        cart_baseTest.selectBookFromResults(bookName);
        cart_baseTest.addToCart();
        cart_baseTest.verifyAlert("Đăng nhập để thêm sách vào giỏ hàng");
    }

    @Test
    public void login_Add_ShoppingCart() throws InterruptedException {
        String bookName = "Tuyển tập Nam Cao";
        login();
        goToHomePage();
        cart_baseTest.searchBook(bookName);
        cart_baseTest.selectBookFromResults(bookName);
        cart_baseTest.addToCart();
        cart_baseTest.verifyAlert("Thêm vào giỏ hàng thành công");
        cart_baseTest.verifyBookInCart(bookName);
    }

    @Test
    public void addMultiple_ShoppingCart() throws InterruptedException {

        List<String> booksToAdd = Arrays.asList("Tuyển tập Vũ Trọng Phụng", "Tuyển tập Nam Cao", "Ai Được Gì Và Tại Sao");
        login();

        for (String bookName : booksToAdd) {
            cart_baseTest.searchBook(bookName);
            cart_baseTest.selectBookFromResults(bookName);
            cart_baseTest.addToCart();
            cart_baseTest.verifyAlert("Thêm vào giỏ hàng thành công");
        }

        WaitForElementVisibleXpath(30, "//span[contains(text(),\"Giỏ Hàng\")]").click();

        //Kiểm tra số sách trong giỏ hàng
        List<WebElement> cartItems = driver.findElements(By.xpath("//div[@class='table-1']/tbody/tr"));
        Assert.assertEquals(cartItems.size(), booksToAdd.size(), "Số lượng sách trong giỏ hàng không đúng");
        System.out.println(cartItems);

        //Kiểm tra sách có trong giỏ hàng
        for (String bookName : booksToAdd) {
            WebElement bookTitleElement = driver.findElement(By.xpath("//div[@class='table-1']/tbody/tr/td[contains(text(),'" + bookName + "')]"));
            Assert.assertNotNull(bookTitleElement, "Sách " + bookName + " không có trong giỏ hàng");

        }
    }

    //Theem sách 2 lần giống nhau
    @Test(enabled = false)
    public void addSameMultiple_ShoppingCart() throws InterruptedException {
        String bookName = "Tuyển tập Nam Cao";
        double expectedPrice = 97.113;
        int expectedQuantity = 1;
        login();
        cart_baseTest.searchBook(bookName);
        cart_baseTest.selectBookFromResults(bookName);

        String actualBookNameBefore = WaitForElementVisibleCss(30, "div[class=\"product-right-name\"] h1").getText();
        String actualPriceBefore = WaitForElementVisibleXpath(30, "//div[@class=\"product-content row ng-star-inserted\"]//div[@class=\"product-right-price\"]//p[1]").getText().replaceAll("[^\\d.]", "");
        double actualPriceBeforeDouble = Double.parseDouble(actualPriceBefore);
        int actualQuantityBefore = expectedQuantity;

        Assert.assertEquals(actualBookNameBefore, bookName, "Tên sách không đúng trước khi thêm vào giỏ hàng");
        Assert.assertEquals(actualPriceBeforeDouble, expectedPrice, "Giá sách không đúng trước khi thêm vào giỏ hàng");

        cart_baseTest.addToCart();
        cart_baseTest.verifyAlert("Sản phẩm đã được thêm vào giỏ hàng");
        WaitForElementVisibleXpath(30, "//span[contains(text(),\"Giỏ Hàng\")]").click();

        String actualBookNameInCart = WaitForElementVisibleXpath(30, "//td[contains(text(),\"" + bookName + "\")]").getText();
        String actualPriceInCart = WaitForElementVisibleXpath(30, "//td[contains(text(),\"" + bookName + "\")]/following-sibling::td[contains(@class,'product-price')]").getText().replaceAll("[^\\d.]", ""); // Lấy giá từ giỏ hàng
        double actualPriceInCartDouble = Double.parseDouble(actualPriceInCart);  // Chuyển sang kiểu số
        int actualQuantityInCart = Integer.parseInt(WaitForElementVisibleXpath(30, "//td[contains(text(),\"" + bookName + "\")]/following-sibling::td[contains(@class,'quantity')]/input").getAttribute("value")); // Lấy số lượng từ giỏ hàng

        cart_baseTest.searchBook(bookName);
        cart_baseTest.selectBookFromResults(bookName);
        cart_baseTest.addToCart();
        cart_baseTest.verifyAlert("Sản phẩm đã được thêm vào giỏ hàng");
    }

}
