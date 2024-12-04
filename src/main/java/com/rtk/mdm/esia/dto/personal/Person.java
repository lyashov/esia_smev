package com.rtk.mdm.esia.dto.personal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

//    {"stateFacts":["EntityRoot"],"firstName":"Галина","lastName":"Петорва","middleName":"Ноколаевна",
//    "birthDate":"11.03.1998","gender":"M","trusted":true,"citizenship":"RUS","snils":"000-000-600 06",
//    "inn":"158913903354","updatedOn":1624961587,"status":"REGISTERED","verifying":false,"rIdDoc":5828,
//    "containsUpCfmCode":false,"eTag":"E64DA0DC52459A1E23A2C5C7AFE34BEEC53E350A"}

@JsonIgnoreProperties(ignoreUnknown = true)
public class Person {
    private Long rawId;
    private String firstName;
    private String lastName;
    private String middleName;
    private String birthDate;
    private String gender;
    private boolean trusted;
    private String citizenship;
    private String snils;
    private String inn;
    private int updatedOn;
    private String status;
    private boolean verifying;
    private int rIdDoc;
    private boolean containsUpCfmCode;
    private String eTag;
    private String mobilePhone;
    private String homePhone;
    private String email;
    private String registrationAddress;
    private String liveAddress;
    private Document passport;

    public Long getRawId() {
        return rawId;
    }

    public void setRawId(Long rawId) {
        this.rawId = rawId;
    }

    public String getFIO(){
        return lastName + " " + firstName + " " + middleName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isTrusted() {
        return trusted;
    }

    public void setTrusted(boolean trusted) {
        this.trusted = trusted;
    }

    public String getCitizenship() {
        return citizenship;
    }

    public void setCitizenship(String citizenship) {
        this.citizenship = citizenship;
    }

    public String getSnils() {
        return snils;
    }

    public void setSnils(String snils) {
        this.snils = snils;
    }

    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }

    public int getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(int updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isVerifying() {
        return verifying;
    }

    public void setVerifying(boolean verifying) {
        this.verifying = verifying;
    }

    public int getrIdDoc() {
        return rIdDoc;
    }

    public void setrIdDoc(int rIdDoc) {
        this.rIdDoc = rIdDoc;
    }

    public boolean isContainsUpCfmCode() {
        return containsUpCfmCode;
    }

    public void setContainsUpCfmCode(boolean containsUpCfmCode) {
        this.containsUpCfmCode = containsUpCfmCode;
    }

    public String geteTag() {
        return eTag;
    }

    public void seteTag(String eTag) {
        this.eTag = eTag;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRegistrationAddress() {
        return registrationAddress;
    }

    public void setRegistrationAddress(String registrationAddress) {
        this.registrationAddress = registrationAddress;
    }

    public String getLiveAddress() {
        return liveAddress;
    }

    public void setLiveAddress(String liveAddress) {
        this.liveAddress = liveAddress;
    }

    public Document getPassport() {
        return passport;
    }

    public void setPassport(Document passport) {
        this.passport = passport;
    }
}
