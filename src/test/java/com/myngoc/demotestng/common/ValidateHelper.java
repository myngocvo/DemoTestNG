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

public class ValidateHelper {

    private final WebDriver driver;
    By snackbarLocator = By.cssSelector("simple-snack-bar .mat-mdc-snack-bar-label");
    private WebDriverWait wait;
    private int timeoutWaitForPageLoaded = 30;
    private Actions actions;
    private Select select;
    private JavascriptExecutor js;

    public ValidateHelper(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        actions = new Actions(driver);
        js = (JavascriptExecutor) driver;
    }

    public String getPageTitle() {
        try {
            return driver.getTitle();
        } catch (Exception e) {
            System.err.println("Unable to fetch the page title: " + e.getMessage());
            return null;
        }
    }

    public void setText(WebElement element, String textValue) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
        element.clear();
        element.sendKeys(textValue);
    }

    public void setTextV2(WebElement element, String textValue) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
//        element.sendKeys(Keys.CONTROL + "a");
//        element.sendKeys(Keys.DELETE);
        element.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        element.sendKeys(textValue);
    }

    public void clickElement(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
        Assert.assertNotNull(element, "The element is not found!");
        Assert.assertTrue(element.isDisplayed(), "The element is not displayed");
        element.click();
    }

    public WebElement getElementByContainText(String text) {
        String dynamicXpath = "//*[contains(text(), '" + text + "')]";
        return driver.findElement(By.xpath(dynamicXpath));
    }

    public void rightClickElement(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
        actions.contextClick().build().perform();
    }

    public void selectOptionByText(WebElement element, String value) {
        select = new Select(element);
        select.selectByVisibleText(value);
    }

    public void selectOptionByValue(WebElement element, String value) {
        select = new Select(element);
        select.selectByValue(value);
    }

    public void verifyOptionTotal(WebElement element) {
        select = new Select(element);
        System.out.println(select.getOptions().size());
        System.out.println(select.getOptions());
    }

    public boolean verifyUrl(String url) {
        String currentUrl = driver.getCurrentUrl();
        assert currentUrl != null;
        return currentUrl.contains(url);
    }

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

    public boolean verifyElementExistByWebElement(WebElement element) {
        try {
            if (element != null && element.isDisplayed()) {
                return true;
            } else {
                System.err.println("Element is either null or not displayed.");
                return false;
            }
        } catch (Exception e) {
            System.err.println("Error verifying WebElement existence: " + e.getMessage());
            return false;
        }
    }

    public boolean verifyElementExistByLocator(By element) {
        List<WebElement> listElement = driver.findElements(element);
        return !listElement.isEmpty();
    }

    public String getSnackbarMessage() throws InterruptedException {
        Thread.sleep(200);
        if (driver.findElement(snackbarLocator).isDisplayed()) {
            return driver.findElement(snackbarLocator).getText();
        }
        return null;
    }

    public void scrollToElement(WebElement element) {
        new Actions(driver)
                .scrollToElement(element)
                .perform();
        System.out.println("Scrolled to element: " + element.toString());
    }

    public void waitForElementToBeClickable(WebElement element, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public void waitForElementNotPresent(WebElement element, int timeoutInSeconds) {
        new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds))
                .until(ExpectedConditions.invisibilityOf(element));
    }

    public void waitForPageLoaded() {
        // wait for jQuery to loaded
        ExpectedCondition<Boolean> jQueryLoad = new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    return ((Long) ((JavascriptExecutor) driver).executeScript("return jQuery.active") == 0);
                } catch (Exception e) {
                    return true;
                }
            }
        };

        // wait for Javascript to loaded
        ExpectedCondition<Boolean> jsLoad = new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return ((JavascriptExecutor) driver).executeScript("return document.readyState")
                        .toString().equals("complete");
            }
        };

        try {
            wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutWaitForPageLoaded));
            wait.until(jQueryLoad);
            wait.until(jsLoad);
        } catch (Throwable error) {
            Assert.fail("Page load time exceeded");
        }

    }


}
