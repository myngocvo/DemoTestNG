package com.myngoc.demotestng.common;

public class Test {
    public static void main(String[] args) {
        JsonHelper jsonHelper = new JsonHelper();
        System.out.println(jsonHelper.getCustomer("src/test/resources/userData/customerData.json", "customer2"));
    }
}
