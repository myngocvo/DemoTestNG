package com.myngoc.demotestng.common;

import com.myngoc.demotestng.Address;
import com.myngoc.demotestng.Book;
import com.myngoc.demotestng.Customer;
import com.myngoc.demotestng.Image;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;

public class JsonHelper {
    public Customer getCustomer(String filePath, String customerName) {
        Customer result = null;
        try {
            FileReader reader = new FileReader(filePath);
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);

            JSONObject cus = (JSONObject) jsonObject.get(customerName);
            String name = (String) cus.get("name");
            String phoneNumber = (String) cus.get("phoneNumber");
            String email = (String) cus.get("email");
            String gender = (String) cus.get("gender");
            String birthDay = (String) cus.get("birthDay");

            JSONObject address = (JSONObject) cus.get("address");
            String city = (String) address.get("city");
            String district = (String) address.get("district");
            String ward = (String) address.get("ward");

            result = new Customer(name, phoneNumber, email, gender, birthDay, new Address(city, district, ward));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public Address getAddress(String filePath, String addressName) {
        Address result = null;
        try {
            FileReader reader = new FileReader(filePath);
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);

            JSONObject address = (JSONObject) jsonObject.get(addressName);
            JSONObject addr = (JSONObject) address.get("address");
            String city = (String) addr.get("city");
            String district = (String) addr.get("district");
            String ward = (String) addr.get("ward");

            result = new Address(city, district, ward);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public Book getBook(String filePath, String bookName) {
        Book result = null;
        try {
            FileReader reader = new FileReader(filePath);
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);

            JSONObject book = (JSONObject) jsonObject.get(bookName);
            String name = (String) book.get("bookName");
            String publishYear = (String) book.get("publishYear");
            String author = (String) book.get("author");
            String category = (String) book.get("category");
            String supplier = (String) book.get("supplier");
            String price = (String) book.get("price");
            String quantity = (String) book.get("quantity");
            String pricePrecent = (String) book.get("pricePrecent");
            String pageNumber = (String) book.get("pageNumber");
            String dimention = (String) book.get("dimention");
            String status = (String) book.get("status");
            String description = (String) book.get("description");

            JSONObject image = (JSONObject) book.get("image");
            String image1 = (String) image.get("image1");
            String image2 = (String) image.get("image2");
            String image3 = (String) image.get("image3");
            String image4 = (String) image.get("image4");

            result = new Book(name, publishYear, author, category, supplier, price, quantity, pricePrecent, pageNumber, dimention, status, description, new Image(image1, image2, image3, image4));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }
}
