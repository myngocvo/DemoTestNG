package com.myngoc.demotestng.common;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;

import java.time.Duration;

// Lớp BaseSetUp dùng để thiết lập và khởi tạo WebDriver cho các trình duyệt
public class BaseSetUp {
    private static final String DEFAULT_URL = "http://localhost:4200"; // URL mặc định
    private static final String ADMIN_URL = "http://localhost:4200/admin"; // URL dành cho admin
    public WebDriver driver;

    // Hàm lấy WebDriver hiện tại
    public WebDriver getDriver() {
        return driver;
    }

    //Thiết lập WebDriver dựa trên loại trình duyệt và URL cần truy cập.
    public WebDriver setupDriver(String urlType) {
        JsonHelper json = new JsonHelper();
        String url = urlType.equalsIgnoreCase("admin") ? ADMIN_URL : DEFAULT_URL;
        String browserName = json.getDriverName();
        // Khởi tạo trình duyệt dựa trên cấu hình
        switch (browserName) {
            case "chrome":
                driver = initChromeDriver();
                break;
            case "edge":
                driver = initEdgeDriver();
                break;
        }
        driver.get(url);
        return driver;
    }

    //Khởi tạo trình duyệt Chrome với các thiết lập cơ bản.
    private WebDriver initChromeDriver() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
        return driver;
    }

    //Khởi tạo trình duyệt Edge với các thiết lập cơ bản.
    private WebDriver initEdgeDriver() {
        WebDriverManager.edgedriver().setup();
        driver = new EdgeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
        return driver;
    }
}
