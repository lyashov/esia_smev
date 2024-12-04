package com.rtk.mdm.esia.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonAutoDetect
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SignDataDto {
    @JsonProperty
    private String data;
}
