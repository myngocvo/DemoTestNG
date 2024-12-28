package com.myngoc.demotestng.testcases.admin_testcases;

import com.myngoc.demotestng.Book;
import com.myngoc.demotestng.common.BaseSetUp;
import com.myngoc.demotestng.common.ExcelHelper;
import com.myngoc.demotestng.common.JsonHelper;
import com.myngoc.demotestng.pages.admin.BookPage;
import com.myngoc.demotestng.pages.admin.LoginAdminPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class AddBookAdminTest {
    private WebDriver driver;
    private LoginAdminPage loginAdminPage;
    private BookPage bookPage;
    private ExcelHelper excelHelper;
    private JsonHelper jsonHelper;

    @BeforeMethod
    public void setUp() throws Exception {
        driver = new BaseSetUp().setupDriver("chrome", "admin");
        loginAdminPage = new LoginAdminPage(driver);
        bookPage = new BookPage(driver);
        excelHelper = new ExcelHelper();
        jsonHelper = new JsonHelper();
        PageFactory.initElements(driver, this);
        excelHelper.setExcelFile("src/test/resources/excelData/adminLoginData.xlsx", "adminLoginData");
        loginAdminPage.loginAdmin(excelHelper.getCellStringData("email", 1), excelHelper.getCellStringData("password", 1));
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void addBook() throws Exception {
        Book book = jsonHelper.getBook("src/test/resources/jsonData/bookData.json", "book1");
        bookPage.addBook(book.getBookName(), book.getPublishYear(), book.getAuthor(), book.getCategory(), book.getSupplier(), book.getPrice(), book.getQuantity(), book.getPricePrecent(), book.getPageNumber(), book.getDimention(), book.getStatus(), book.getDescription(), book.getImage());
        Assert.assertEquals(driver.switchTo().alert().getText(), "Thêm sách thành công!", "Notification is incorrect!");
        driver.switchTo().alert().accept();
        System.out.println("Test add book successful");
    }

    @Test(priority = 1)
    public void deleteBook() throws Exception {
        Book book = jsonHelper.getBook("src/test/resources/jsonData/bookData.json", "book1");
        bookPage.deleteBook(book.getBookName());
    }
}
