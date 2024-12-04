package com.rtk.mdm.esia.controller;

import com.rtk.mdm.esia.service.EsiaService;
import com.rtk.mdm.esia.session.TokenInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class EsiaController {

    EsiaService esiaService;

    @Autowired
    public EsiaController(EsiaService esiaService) {
        this.esiaService = esiaService;
    }

    @RequestMapping(value = "/get-token", method = RequestMethod.GET)
    public ModelAndView login() throws Exception {
        return new ModelAndView("redirect:" + esiaService.getLoginUrl());
    }

    @RequestMapping(value = "/get-token-test", method = RequestMethod.GET)
    public ModelAndView loginTest() throws Exception {
        return new ModelAndView("redirect:" + esiaService.getLoginUrlTest());
    }

    @RequestMapping(value = "/codetest", method = RequestMethod.GET)
    public ResponseEntity<String> codeTest(@RequestParam("code") String code) throws Exception {
        return esiaService.getTokenTest(code);
    }

    @RequestMapping(value = "/code", method = RequestMethod.GET)
    public ResponseEntity<String> code(@RequestParam("code") String code) throws Exception {
        return esiaService.getToken(code);
    }

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public ResponseEntity<TokenInfo> info(){
        return esiaService.getTokenInfo();
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public ResponseEntity<String> user() {
        return esiaService.getUserInfo();
    }

    @RequestMapping(value = "/user-id", method = RequestMethod.GET)
    public String userId() {
        return esiaService.getUserIDInfo();
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ResponseEntity<String> logout() {
        return esiaService.logout();
    }
}
