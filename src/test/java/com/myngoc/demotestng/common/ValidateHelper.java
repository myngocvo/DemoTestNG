package com.myngoc.demotestng.common;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

// Lớp ValidateHelper cung cấp các phương thức hỗ trợ kiểm tra và thao tác với các thành phần trên trang web
public class ValidateHelper {

    private final WebDriver driver; // Đối tượng WebDriver chính
    private final Actions actions; // Hỗ trợ thao tác nâng cao như cuộn trang, kéo thả
    By snackbarLocator = By.cssSelector("simple-snack-bar .mat-mdc-snack-bar-label"); // Định vị Snackbar
    private WebDriverWait wait; // WebDriverWait để xử lý chờ

    // Hàm khởi tạo, thiết lập các đối tượng cần thiết
    public ValidateHelper(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        actions = new Actions(driver);
    }

    // Nhập text vào một ô input
    public void setText(WebElement element, String textValue) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
        element.clear();
        element.sendKeys(textValue);
    }

    // Nhập text sau khi xóa toàn bộ nội dung cũ
    public void setTextV2(WebElement element, String textValue) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
        element.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        element.sendKeys(textValue);
    }

    // Click vào một phần tử trên trang
    public void clickElement(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
        Assert.assertNotNull(element, "The element is not found!");
        Assert.assertTrue(element.isDisplayed(), "The element is not displayed");
        element.click();
    }

    // Lấy phần tử bằng text chứa trong XPath
    public WebElement getElementByContainText(String text) {
        String dynamicXpath = "//*[contains(text(), '" + text + "')]";
        return driver.findElement(By.xpath(dynamicXpath));
    }

    // Chọn giá trị trong dropdown bằng text
    public void selectOptionByText(WebElement element, String value) {
        // Hỗ trợ làm việc với dropdown
        Select select = new Select(element);
        select.selectByVisibleText(value);
    }

    // Xác minh URL hiện tại có chứa đoạn text mong muốn
    public boolean verifyUrl(String url) {
        String currentUrl = driver.getCurrentUrl();
        System.out.println(currentUrl);
        return currentUrl != null && currentUrl.contains(url);
    }

    // Xác minh text của một phần tử
    public boolean verifyElementText(WebElement element, String expectedText) {
        try {
            String actualText = element.getText().trim().replaceAll("\\s+", "");
            String normalizedExpectedText = expectedText.trim().replaceAll("\\s+", "");
            Assert.assertEquals(actualText, normalizedExpectedText, "Element text does not match!");
            return true;
        } catch (Exception e) {
            System.err.println("Error verifying element text: " + e.getMessage());
            return false;
        }
    }

    // Kiểm tra sự tồn tại của phần tử WebElement
    public boolean verifyElementExistByWebElement(WebElement element) {
        try {
            return element != null && element.isDisplayed();
        } catch (Exception e) {
            System.err.println("Error verifying WebElement existence: " + e.getMessage());
            return false;
        }
    }

    // Kiểm tra sự tồn tại của phần tử bằng Locator
    public boolean verifyElementExistByLocator(By element) {
        List<WebElement> listElement = driver.findElements(element);
        return !listElement.isEmpty();
    }

    // Lấy message từ Snackbar nếu tồn tại
    public String getSnackbarMessage() throws InterruptedException {
        Thread.sleep(400);
        if (driver.findElement(snackbarLocator).isDisplayed()) {
            return driver.findElement(snackbarLocator).getText();
        }
        return null;
    }

    // Cuộn tới một phần tử cụ thể trên trang
    public void scrollToElement(WebElement element) {
        actions.scrollToElement(element).perform();
        System.out.println("Scrolled to element: " + element.toString());
    }

    // Chờ trang tải hoàn toàn (bao gồm jQuery và Javascript)
    public void waitForPageLoaded() {
        // Điều kiện chờ jQuery tải xong
        ExpectedCondition<Boolean> jQueryLoad = driver -> {
            try {
                assert driver != null;
                Object jQueryActive = ((JavascriptExecutor) driver).executeScript("return jQuery.active");
                return jQueryActive != null && (Long) jQueryActive == 0;
            } catch (Exception e) {
                return true; // Nếu không có jQuery, coi như tải xong
            }
        };

        // Điều kiện chờ Javascript tải xong
        ExpectedCondition<Boolean> jsLoad = driver ->
        {
            assert driver != null;
            return Objects.requireNonNull(((JavascriptExecutor) driver).executeScript("return document.readyState"))
                    .toString().equals("complete");
        };

        try {
            int timeoutWaitForPageLoaded = 30; // Thời gian chờ tối đa
            wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutWaitForPageLoaded));
            wait.until(jQueryLoad); // Chờ jQuery
            wait.until(jsLoad);    // Chờ Javascript
        } catch (Throwable error) {
            Assert.fail("Page load time exceeded");
        }
    }

}
