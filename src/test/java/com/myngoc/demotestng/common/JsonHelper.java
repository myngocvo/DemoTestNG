package com.myngoc.demotestng.common;

import com.myngoc.demotestng.Address;
import com.myngoc.demotestng.Customer;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;

public class JsonHelper {
    public Customer getCustomer(String filePath, String customerName) {
        Customer customer = null;
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

            customer = new Customer(name, phoneNumber, email, gender, birthDay, new Address(city, district, ward));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return customer;
    }

    public Address getAddress(String filePath, String customerName) {
        Address address = null;
        try {
            FileReader reader = new FileReader(filePath);
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);

            JSONObject cus = (JSONObject) jsonObject.get(customerName);
            JSONObject addr = (JSONObject) cus.get("address");
            String city = (String) addr.get("city");
            String district = (String) addr.get("district");
            String ward = (String) addr.get("ward");

            address = new Address(city, district, ward);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return address;
    }
}
