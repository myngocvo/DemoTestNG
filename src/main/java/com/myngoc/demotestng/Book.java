package com.myngoc.demotestng;

public class Book {
    String bookName;
    String publishYear;
    String author;
    String category;
    String supplier;
    String price;
    String quantity;
    String pricePrecent;
    String pageNumber;
    String dimention;
    String status;
    String description;
    Image image;

    public Book() {
    }

    public Book(String bookName, String publishYear, String author, String category, String supplier, String price, String quantity, String pricePrecent, String pageNumber, String dimention, String status, String description, Image image) {
        this.bookName = bookName;
        this.publishYear = publishYear;
        this.author = author;
        this.category = category;
        this.supplier = supplier;
        this.price = price;
        this.quantity = quantity;
        this.pricePrecent = pricePrecent;
        this.pageNumber = pageNumber;
        this.dimention = dimention;
        this.status = status;
        this.description = description;
        this.image = image;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublishYear() {
        return publishYear;
    }

    public void setPublishYear(String publishYear) {
        this.publishYear = publishYear;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDimention() {
        return dimention;
    }

    public void setDimention(String dimention) {
        this.dimention = dimention;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPricePrecent() {
        return pricePrecent;
    }

    public void setPricePrecent(String pricePrecent) {
        this.pricePrecent = pricePrecent;
    }

    public String getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(String pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Book{" +
                "bookName='" + bookName + '\'' +
                ", publishYear='" + publishYear + '\'' +
                ", author='" + author + '\'' +
                ", category='" + category + '\'' +
                ", supplier='" + supplier + '\'' +
                ", price='" + price + '\'' +
                ", quantity='" + quantity + '\'' +
                ", pricePrecent='" + pricePrecent + '\'' +
                ", pageNumber='" + pageNumber + '\'' +
                ", dimention='" + dimention + '\'' +
                ", status='" + status + '\'' +
                ", description='" + description + '\'' +
                ", image=" + image +
                '}';
    }
}
