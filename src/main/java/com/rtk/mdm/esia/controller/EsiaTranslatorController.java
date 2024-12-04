package com.rtk.mdm.esia.controller;

import com.rtk.mdm.esia.service.EsiaTranslatorService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
public class EsiaTranslatorController {
    EsiaTranslatorService esiaTranslatorService;

    public EsiaTranslatorController(EsiaTranslatorService esiaTranslatorService) {
        this.esiaTranslatorService = esiaTranslatorService;
    }

    @GetMapping(value = { "/rs/prns/{oid}", "/rs/prns/{oid}/{pname}", "/rs/prns/{oid}/{pname}/{pvalue}" })
    public ResponseEntity prnsTranslator(@RequestHeader Map<String, String> headers,
                                         @PathVariable Map<String, String> body) {
        return esiaTranslatorService.prnsTranslator(headers, body);
    }

    @GetMapping(value = { "/aas/oauth2/ac" })
    public ModelAndView codeTranslator(@RequestHeader Map<String, String> headers,
                                              @RequestParam Map<String,String> body) {
        return new ModelAndView("redirect:" +  esiaTranslatorService.codeTranslator(headers, body));
    }

    @PostMapping(value = { "/aas/oauth2/te" })
    public ResponseEntity authTokenTranslator(@RequestHeader Map<String, String> headers,
                                              @RequestParam Map<String,String> body) {
        return esiaTranslatorService.authTokenTranslator(headers, body);
    }
}
