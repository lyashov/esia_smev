package com.rtk.mdm.esia.dto.personal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

//{
//    "stateFacts": [
//        "Identifiable"
//    ],
//    "id": 72154,
//    "type": "PLV",
//    "addressStr": "Архангельская обл, г Котлас, ул Ленина",
//    "fiasCode": "ac2324ec-456c-4f3c-a7fe-f3b452a04118",
//    "flat": "583",
//    "countryId": "RUS",
//    "area": "Котласский",
//    "house": "4А",
//    "frame": "1",
//    "zipCode": "165300",
//    "city": "Котлас",
//    "street": "Ленина",
//    "region": "Архангельская",
//    "eTag": "B72FEBFDA1DA53C04A3A644FE6A23E72E5EE0774"
//}

@JsonIgnoreProperties(ignoreUnknown = true)
public class Address {
    private int id;
    private String type;
    private String addressStr;
    private String flat;
    private String countryId;
    private String area;
    private String house;
    private String frame;
    private String zipCode;
    private String city;
    private String street;
    private String region;
    private String eTag;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddressStr() {
        return addressStr;
    }

    public void setAddressStr(String addressStr) {
        this.addressStr = addressStr;
    }

    public String getFlat() {
        return flat;
    }

    public void setFlat(String flat) {
        this.flat = flat;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public String getFrame() {
        return frame;
    }

    public void setFrame(String frame) {
        this.frame = frame;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String geteTag() {
        return eTag;
    }

    public void seteTag(String eTag) {
        this.eTag = eTag;
    }
}
