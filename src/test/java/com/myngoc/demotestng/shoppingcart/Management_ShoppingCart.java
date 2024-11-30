package com.myngoc.demotestng.shoppingcart;

import com.myngoc.demotestng.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;

public class Management_ShoppingCart extends BaseTest {

    ShoppingCart_BaseTest cart_baseTest = new ShoppingCart_BaseTest();

    @BeforeTest
    public void setUpDriver() {
        setUp();
        login();
    }

    @AfterTest
    public void tearDownDriver() {
        tearDown();
    }


    private double convertToDouble(String text) {
        return Double.parseDouble(text.replace("đ", "").replace(",", "").trim());
    }

    public void deleteAllProductsFromCart() throws InterruptedException {

        WaitForElementVisibleXpath(30, "//span[contains(text(),\"Giỏ Hàng\")]").click();
        Thread.sleep(2000);

        List<WebElement> productRows = driver.findElements(By.cssSelector("tbody tr"));

        while (!productRows.isEmpty()) {
            WebElement deleteButton = productRows.get(0).findElement(By.cssSelector("td:nth-child(7) button.btn-trash"));
            deleteButton.click();
            Thread.sleep(2000);

            productRows = driver.findElements(By.cssSelector("tbody tr"));
        }

        Assert.assertTrue(productRows.isEmpty(), "Sản phẩm trong giỏ hàng chưa được xóa hết");

        System.out.println("Tất cả sản phẩm đã được xóa thành công.");
    }

    @Test()
    public void priceUpdateOnQuantityChange() throws InterruptedException {
        WaitForElementVisibleXpath(40, "//span[contains(text(),\"Giỏ Hàng\")]").click();
        Thread.sleep(2000);

        String productName = WaitForElementVisibleCss(30, "tbody tr:nth-child(1) td:nth-child(3)").getText();
        //System.out.println("Tên sách: " + productName);

        WebElement quantityElement = WaitForElementVisibleCss(30, "tbody tr:nth-child(1) td:nth-child(4) div:nth-child(1) input:nth-child(1)");
        String quantityValue = quantityElement.getAttribute("placeholder");

        assert quantityValue != null;
        int quantity = Integer.parseInt(quantityValue);

        //System.out.println("Quantity: " + quantity);

        String priceBeforeText = WaitForElementVisibleCss(30, "tbody tr:nth-child(1) td:nth-child(5) span:nth-child(2)").getText();
        double priceBefore = convertToDouble(priceBeforeText);

        int newQuantity = 3;
        quantityElement.clear();
        quantityElement.sendKeys(String.valueOf(newQuantity));

        double expectedTotal = newQuantity * priceBefore;

        String actualTotalItemBeforeText = WaitForElementVisibleCss(30, "tbody tr:nth-child(1) td:nth-child(6)").getText();
        double actualTotalItemBefore = convertToDouble(actualTotalItemBeforeText);

        System.out.println(expectedTotal);

        Assert.assertEquals(actualTotalItemBefore, expectedTotal, "Tổng tiền không được cập nhật khi thay đổi số lượng.");
        System.out.println(actualTotalItemBefore);
    }

    @Test
    public void priceTotalUpdateCheckboxSelected() throws InterruptedException {
        WaitForElementVisibleXpath(30, "//span[contains(text(),\"Giỏ Hàng\")]").click();

        String initTotalPriceText = WaitForElementVisibleCss(30, "tbody tr th:nth-child(2)").getText();
        double initTotalPrice = convertToDouble(initTotalPriceText);
        System.out.println(initTotalPrice);

        List<WebElement> productCheckboxes = driver.findElements(By.tagName("mat-checkbox"));
        List<WebElement> productPrices = driver.findElements(By.cssSelector("tbody tr td:nth-child(6)"));

        if (productCheckboxes.isEmpty()) {
            System.out.println("Không có checkbox nào trên trang.");
            return;
        }

        for (int i = 1; i < productCheckboxes.size(); i++) {
            WebElement checkbox = productCheckboxes.get(i);

            String productPriceText = productPrices.get(i - 1).getText();
            double productPrice = convertToDouble(productPriceText);
            Thread.sleep(2000);

            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView(true);", checkbox);

            checkbox.click();
            Thread.sleep(2000);

            String updatedTotalPriceText = WaitForElementVisibleCss(30, "tbody tr th:nth-child(2)").getText();
            double updatedTotalPrice = convertToDouble(updatedTotalPriceText);

            double expectedTotalPrice = initTotalPrice + productPrice;
            Thread.sleep(2000);
            System.out.println(updatedTotalPriceText);
            Assert.assertEquals(expectedTotalPrice, updatedTotalPrice, "Tổng tiền không chính xác sau khi chọn checkbox");

            initTotalPrice = updatedTotalPrice;

            System.out.println("Tổng tiền sau khi chọn checkbox " + (i + 1) + ": " + initTotalPriceText + "+" + updatedTotalPrice);

        }
    }


    @Test
    public void deleteProduct() throws InterruptedException {


        String bookName = "Giải Thích Ngữ Pháp Tiếng Anh (Tái Bản 2022)";
        cart_baseTest.searchBook(bookName);
        cart_baseTest.selectBookFromResults(bookName);
        cart_baseTest.addToCart();
        Thread.sleep(2000);
        driver.switchTo().alert().accept();

        WaitForElementVisibleXpath(30, "//span[contains(text(),\"Giỏ Hàng\")]").click();

        List<WebElement> productInShoppingCart = driver.findElements(By.cssSelector("tbody tr"));

        boolean isProductFound = false;
        for (WebElement row : productInShoppingCart) {
            WebElement productNameElement = row.findElement(By.cssSelector("td:nth-child(3)"));
            System.out.println(row.getText());

            if (productNameElement.getText().equals(bookName)) {
                isProductFound = true;

                WebElement deleteButton = row.findElement(By.cssSelector("td:nth-child(7) button.btn-trash"));
                deleteButton.click();
                break;
            }
        }
        Assert.assertTrue(isProductFound, "Không tìm thấy sách trong giỏ hàng");

        Thread.sleep(2000);
        List<WebElement> remainingProducts = driver.findElements(By.cssSelector("tbody tr td:nth-child(3)"));
        boolean isProductDeleted = remainingProducts.stream()
                .noneMatch(element -> element.getText().equals(bookName));
        Thread.sleep(2000);
        Assert.assertTrue(isProductDeleted, "Sản phẩm chưa bị xóa khỏi giỏ hàng!");

        System.out.println("Xóa sản phẩm \"" + bookName + "\" thành công.");
    }
}
