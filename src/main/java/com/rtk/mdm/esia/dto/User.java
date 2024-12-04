package com.rtk.mdm.esia.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private String openid;
    private String firstName;
    private String lastName;
    private String middleName;
    private String inn;
    private String birthday;
    private String gender;
    private String snils;
    private String medical_doc;
    private String email;
}
