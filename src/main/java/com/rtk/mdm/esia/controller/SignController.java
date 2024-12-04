package com.rtk.mdm.esia.controller;

import com.rtk.mdm.esia.dto.SignDataDto;
import com.rtk.mdm.esia.service.EsiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sign-service/v1")
public class SignController {

    EsiaService esiaService;

    @Autowired
    public SignController(EsiaService esiaService) {
        this.esiaService = esiaService;
    }

    @PostMapping("/sign")
    @ResponseBody
    public ResponseEntity<String> signMessage(@RequestBody SignDataDto dto) throws Exception {
        return esiaService.signMessage(dto.getData());
    }
}
