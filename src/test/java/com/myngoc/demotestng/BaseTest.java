package com.myngoc.demotestng;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;

public class BaseTest {
    public WebDriver driver;
    public String baseUrl = "http://localhost:4200/home";
    By homeButton = By.xpath("//button[contains(text(),\"TRANG CHỦ\")]");
    public By cartButton = By.xpath("//span[contains(text(),\"Giỏ Hàng\")]");

    public void goToHomePage() {
        driver.findElement(homeButton).click(); // Sử dụng biến homeBtn
    }

    public void setUp() {
//        WebDriverManager.chromedriver().setup();
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
        driver.manage().window().maximize();
        driver.navigate().to("http://localhost:4200/home");
    }

    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    public void login() {
        WaitForElementVisibleXpath(30, "//span[contains(text(),\"Tài Khoản\")]").click();

        WaitForElementVisibleXpath(30, "//input[@name=\"phoneLogin\"]").sendKeys("0932877995");

        WaitForElementVisibleXpath(30, "//input[@name=\"PassLogin\"]").sendKeys("Ngoc@123");

        WaitForElementVisibleXpath(30, "//button[@class=\"btn btn-login\"]").click();
    }

    public WebElement WaitForElementVisibleXpath(int seconds, String locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locator)));
    }

    public WebElement WaitForElementVisibleCss(int seconds, String locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(locator)));
    }

}
