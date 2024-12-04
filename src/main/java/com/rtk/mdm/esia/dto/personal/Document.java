package com.rtk.mdm.esia.dto.personal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//{
//    "stateFacts": [
//        "EntityRoot"
//    ],
//    "id": 2630975,
//    "type": "RF_PASSPORT",
//    "vrfStu": "VERIFIED",
//    "series": "1411",
//    "number": "214022",
//    "issueDate": "15.02.2012",
//    "issueId": "000000",
//    "issuedBy": "отд №2 ОУФМС России по Белгородской обл в г Белгороде",
//    "fmsValid": true,
//    "eTag": "F0495731F852ECD33D8982C728DBCBA136A126CD"
//}
@JsonIgnoreProperties(ignoreUnknown = true)
public class Document {
    private int id;
    private String type;
    private String vrfStu;
    private String series;
    private String number;
    private String issueDate;
    private String issueId;
    private String issuedBy;
    private Boolean fmsValid;
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

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getIssueId() {
        return issueId;
    }

    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }

    public String getIssuedBy() {
        return issuedBy;
    }

    public void setIssuedBy(String issuedBy) {
        this.issuedBy = issuedBy;
    }

    public Boolean getFmsValid() {
        return fmsValid;
    }

    public void setFmsValid(Boolean fmsValid) {
        this.fmsValid = fmsValid;
    }

    public String geteTag() {
        return eTag;
    }

    public void seteTag(String eTag) {
        this.eTag = eTag;
    }
}
