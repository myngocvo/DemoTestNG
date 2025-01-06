package com.myngoc.demotestng.pages.customer;

import com.myngoc.demotestng.Address;
import com.myngoc.demotestng.common.JsonHelper;
import com.myngoc.demotestng.common.ValidateHelper;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import java.text.DecimalFormat;
import java.util.List;

public class PaymentPage {
    private final WebDriver driver;
    private final ValidateHelper validateHelper;
    private final MyProfilePage myProfilePage;
    private final JsonHelper jsonHelper;
    private final HomePage homePage;
    private ShoppingCartPage shoppingCartPage;

    // Định nghĩa các thành phần giao diện liên quan đến trang thanh toán
    @FindBy(xpath = "//tr[1]//th[1]//input[@type='checkbox']")
    private WebElement checkAll; // Checkbox chọn tất cả sản phẩm
    @FindBy(xpath = "//span[normalize-space()='THANH TOÁN']")
    private WebElement paymentButton; // Nút "THANH TOÁN"
    @FindBy(xpath = "//tfoot//tr[1]")
    private WebElement totolOfBook; // Tổng tiền sản phẩm
    @FindBy(xpath = "//tfoot//tr[4]")
    private WebElement totolOfOrder; // Tổng tiền đơn hàng
    @FindBy(xpath = "//tfoot//tr[2]//td[2]")
    private WebElement shippingFee; // Phí vận chuyển
    @FindBy(xpath = "//tfoot//tr[3]")
    private WebElement discountPrice; // Giá giảm
    @FindBy(xpath = "//span[contains(text(),'ĐẶT HÀNG')]")
    private WebElement orderButton; // Nút "ĐẶT HÀNG"
    @FindBy(xpath = "(//input[@type='radio'])[2]")
    private WebElement updateAddress; // Radio button để cập nhật địa chỉ

    // Hàm khởi tạo PaymentPage, khởi tạo các helper và liên kết các thành phần giao diện
    public PaymentPage(WebDriver driver) {
        this.driver = driver;
        validateHelper = new ValidateHelper(driver);
        myProfilePage = new MyProfilePage(driver);
        homePage = new HomePage(driver);
        shoppingCartPage = new ShoppingCartPage(driver);
        jsonHelper = new JsonHelper();
        PageFactory.initElements(driver, this);
    }

    // Hàm chuyển đổi từ chuỗi định dạng tiền tệ sang số nguyên
    private int getIntFromCurrency(String bookPrice) {
        bookPrice = bookPrice.replace("đ", "").replace(".", "").replace(",", "");
        return Integer.parseInt(bookPrice);
    }

    // Hàm chuyển đổi từ số nguyên sang chuỗi định dạng tiền tệ
    private String getCurrencyFromInt(int total) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(total) + "đ";
    }

    // Lấy thông tin sách trong giỏ hàng
    public Object[][] getBookInfo() {
        List<WebElement> bookInCart = shoppingCartPage.getCartItems();
        int bookNum = bookInCart.size();
        Object[][] booksData = new Object[bookNum + 1][2];
        int total = 0;
        for (int i = 0; i < bookNum; i++) {
            booksData[i][0] = driver.findElement(By.xpath("//tr[contains(@class,'book')][" + (i + 1) + "]//td[3]")).getText(); // Lấy tên sách
            booksData[i][1] = getIntFromCurrency(driver.findElement(By.xpath("//tr[contains(@class,'book')][" + (i + 1) + "]//td[6]")).getText()); // Lấy giá sách
            total += (int) booksData[i][1]; // Cộng tổng tiền sách
        }
        // Kiểm tra tổng tiền sách
        if (verifyTotal(total)) {
            booksData[bookNum][0] = "Total";
            booksData[bookNum][1] = total;
        }
        return booksData;
    }

    // Hàm kiểm tra tổng tiền
    private boolean verifyTotal(int total) {
        checkAll.click(); // Chọn tất cả sản phẩm
        return validateHelper.verifyElementExistByLocator(By.xpath("//span[contains(text(),'" + getCurrencyFromInt(total).replace("đ", "") + "')]"));
    }

    // Xác minh thông tin thanh toán
    public void verifyPaymentInfo(Object[][] bookInfo, boolean isHaveVoucher, double discountPercent, double maxValueDiscount) {
        for (int i = 0; i < bookInfo.length - 1; i++) {
            validateHelper.scrollToElement(driver.findElement(By.xpath("//tbody//tr[" + (i + 1) + "]"))); // Cuộn tới từng sản phẩm
            String bookName = driver.findElement(By.xpath("//tbody//tr[" + (i + 1) + "]//td[2]")).getText(); // Lấy tên sách
            String bookPrice = driver.findElement(By.xpath("//tbody//tr[" + (i + 1) + "]//td[6]")).getText(); // Lấy giá sách
            Assert.assertEquals(bookName, bookInfo[i][0]); // Kiểm tra tên sách
            Assert.assertEquals(getIntFromCurrency(bookPrice), bookInfo[i][1]); // Kiểm tra giá sách
        }
        validateHelper.scrollToElement(totolOfOrder);
        int shipingFee = getIntFromCurrency(shippingFee.getText()); // Lấy phí vận chuyển
        int totalOfBook = (int) bookInfo[bookInfo.length - 1][1]; // Lấy tổng tiền sách
        int totalOfOrder = totalOfBook + shipingFee; // Tính tổng tiền đơn hàng
        if (isHaveVoucher) {
            int discountValue = (int) (totalOfBook * (discountPercent / 100)) < maxValueDiscount ? (int) (totalOfBook * (discountPercent / 100)) : (int) maxValueDiscount; // Tính giá trị giảm giá
            Assert.assertEquals(discountPrice.getText(), "Giảm giá " + getCurrencyFromInt(discountValue)); // Kiểm tra giá trị giảm giá
            totalOfOrder -= discountValue; // Cập nhật tổng tiền đơn hàng
        }
        Assert.assertEquals(totolOfBook.getText(), "Tổng tiền sản phẩm " + getCurrencyFromInt(totalOfBook)); // Kiểm tra tổng tiền sản phẩm
        Assert.assertEquals(totolOfOrder.getText(), "Tổng tiền " + getCurrencyFromInt(totalOfOrder)); // Kiểm tra tổng tiền đơn hàng
    }

    // Click vào nút "ĐẶT HÀNG"
    public void clickOrder() throws InterruptedException {
        validateHelper.scrollToElement(orderButton);
        validateHelper.clickElement(orderButton);
        Thread.sleep(1000); // Chờ xử lý
        Alert alert = driver.switchTo().alert(); // Xử lý cảnh báo
        Assert.assertEquals(alert.getText(), "Vui lòng chờ xác nhận đơn hàng từ shop"); // Kiểm tra nội dung cảnh báo
        alert.accept();
    }

    // Xử lý thanh toán
    public void makeAPayment(boolean isHaveAddress, String addressOfCus, boolean isHaveVoucher, double discountPercent, double maxValueDiscount) {
        validateHelper.clickElement(paymentButton);
        if (!isHaveAddress) {
            updateAddressWhenPayment(addressOfCus);
        }
        if (isHaveVoucher) {
            clickAVoucher(discountPercent, maxValueDiscount);
        }
    }

    // Chọn voucher phù hợp
    private void clickAVoucher(double discountPercent, double maxValueDiscount) {
        List<WebElement> vouchers = driver.findElements(By.xpath("//div[contains(@class,'coupon-section')]//div[contains(@class,'mdc-form-field')]"));
        String voucherDiscount = "Giảm giá " + (int) discountPercent + "%";
        String maxPriceDiscount = "Số tiền tối đa: " + (int) maxValueDiscount + "đ";
        for (WebElement voucher : vouchers) {
            if (voucher.getText().contains(voucherDiscount) && voucher.getText().contains(maxPriceDiscount)) {
                validateHelper.clickElement(voucher);
            }
        }
    }

    // Cập nhật địa chỉ khi thanh toán
    public void updateAddressWhenPayment(String addressOfCus) {
        updateAddress.click();
        Address address = jsonHelper.getAddress("src/test/resources/jsonData/customerData.json", addressOfCus);
        myProfilePage.updateAdress(address.getCity(), address.getDistrict(), address.getWard());
        homePage.openShoppingCart();
        checkAll.click();
        validateHelper.clickElement(paymentButton);
    }
}
