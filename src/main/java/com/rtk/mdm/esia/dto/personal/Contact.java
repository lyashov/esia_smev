package com.rtk.mdm.esia.dto.personal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//{
//    "stateFacts": [
//        "Identifiable"
//    ],
//    "id": 14458380,
//    "type": "MBT",
//    "vrfStu": "VERIFIED",
//    "value": "+7(926)2464800",
//    "verifyingValue": "+7(926)2499999",
//    "vrfValStu": "VERIFYING",
//    "isCfmCodeExpired": true,
//    "eTag": "B9DA77BEBD0ADEB4E9EDA024CED81E41D2EE42CD"
//}
@JsonIgnoreProperties(ignoreUnknown = true)
public class Contact {
    private int id;
    private String type;
    private String vrfStu;
    private String value;
    private String verifyingValue;
    private String vrfValStu;
    private boolean isCfmCodeExpired;
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

    public String getVrfStu() {
        return vrfStu;
    }

    public void setVrfStu(String vrfStu) {
        this.vrfStu = vrfStu;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getVerifyingValue() {
        return verifyingValue;
    }

    public void setVerifyingValue(String verifyingValue) {
        this.verifyingValue = verifyingValue;
    }

    public String getVrfValStu() {
        return vrfValStu;
    }

    public void setVrfValStu(String vrfValStu) {
        this.vrfValStu = vrfValStu;
    }

    public boolean isCfmCodeExpired() {
        return isCfmCodeExpired;
    }

    public void setCfmCodeExpired(boolean cfmCodeExpired) {
        isCfmCodeExpired = cfmCodeExpired;
    }

    public String geteTag() {
        return eTag;
    }

    public void seteTag(String eTag) {
        this.eTag = eTag;
    }
}
