package com.rtk.mdm.esia.session;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component(value = "session")
public class TokenInfo {
    private String access_token;
    private String expires_in;
    private String state;
    private String token_type;
    private String refresh_token;
    private String id_token;
    public TokenInfo getTokenInfo(){
        return this;
    }
}
