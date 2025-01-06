package com.myngoc.demotestng.testcases.admin_testcases;

// Import các thư viện cần thiết

import com.myngoc.demotestng.Book;
import com.myngoc.demotestng.common.BaseSetUp;
import com.myngoc.demotestng.common.ExcelHelper;
import com.myngoc.demotestng.common.JsonHelper;
import com.myngoc.demotestng.pages.admin.BookPageAdmin;
import com.myngoc.demotestng.pages.admin.LoginPageAdmin;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Lớp kiểm tra chức năng thêm và xóa sách trên trang quản trị (Admin).
 */
public class AddBookAdminTest {
    private WebDriver driver; // Đối tượng WebDriver để điều khiển trình duyệt
    private BookPageAdmin bookPageAdmin; // Trang quản lý sách của Admin
    private JsonHelper jsonHelper; // Hỗ trợ thao tác với file JSON

    @BeforeMethod
    public void setUp() throws Exception {
        // Thiết lập trình duyệt và đăng nhập Admin
        driver = new BaseSetUp().setupDriver("admin");
        LoginPageAdmin loginPageAdmin = new LoginPageAdmin(driver);
        bookPageAdmin = new BookPageAdmin(driver);
        ExcelHelper excelHelper = new ExcelHelper();
        jsonHelper = new JsonHelper();
        PageFactory.initElements(driver, this); // Khởi tạo các phần tử trang với PageFactory

        // Đọc thông tin đăng nhập từ file Excel
        excelHelper.setExcelFile("src/test/resources/excelData/loginData.xlsx", "admin");
        loginPageAdmin.loginAdmin(excelHelper.getCellStringData("email", 1), excelHelper.getCellStringData("password", 1));
    }

    @AfterMethod
    public void tearDown() {
        // Đóng trình duyệt sau khi chạy xong mỗi test
        if (driver != null) {
            driver.quit();
        }
    }

    // Test chức năng thêm sách
    @Test
    public void addBook() throws Exception {
        // Đọc thông tin sách từ file JSON
        Book book = jsonHelper.getBook("src/test/resources/jsonData/bookData.json", "book1");
        // Thêm sách với các thông tin từ file JSON
        bookPageAdmin.addBook(
                book.getBookName(),
                book.getPublishYear(),
                book.getAuthor(),
                book.getCategory(),
                book.getSupplier(),
                book.getPrice(),
                book.getQuantity(),
                book.getPricePrecent(),
                book.getPageNumber(),
                book.getDimention(),
                book.getStatus(),
                book.getDescription(),
                book.getImage()
        );
        // Xác nhận thông báo thành công và kiểm tra
        Assert.assertEquals(driver.switchTo().alert().getText(), "Thêm sách thành công!", "Notification is incorrect!");
        driver.switchTo().alert().accept();
        System.out.println("Test add book successful");
    }

    // Test chức năng xóa sách
    @Test(priority = 1)
    public void deleteBook() throws Exception {
        // Đọc thông tin sách từ file JSON
        Book book = jsonHelper.getBook("src/test/resources/jsonData/bookData.json", "book1");
        // Xóa sách dựa trên tên sách
        bookPageAdmin.deleteBook(book.getBookName());
        System.out.println("Test delete book successful");
    }
}