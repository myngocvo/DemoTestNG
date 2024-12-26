package com.myngoc.demotestng;

public class Address {
    private String city;
    private String district;
    private String ward;

    public Address() {
    }

    public Address(String city, String district, String ward) {
        this.city = city;
        this.district = district;
        this.ward = ward;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "com.myngoc.demotestng.Address{" +
                "city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", ward='" + ward + '\'' +
                '}';
    }
}
