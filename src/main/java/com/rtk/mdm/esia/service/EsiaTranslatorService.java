package com.rtk.mdm.esia.service;

import com.rtk.mdm.esia.util.HttpCore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Service
public class EsiaTranslatorService {
    private Logger logger = LoggerFactory.getLogger(EsiaService.class);

    @Value("${esia.site}")
    private String esiaSite;

    @Value("${esia.token.url}")
    private String tokenUrl;

    @Value("${esia.response.person}")
    private String personUrl;

    @Value("${token.save.default.system}")
    private String tokenSaveDefaultSystem;

    private final HttpCore httpCore;
    private final EsiaService esiaService;

    @Autowired
    public EsiaTranslatorService(HttpCore httpCore, EsiaService esiaService) {
        this.httpCore = httpCore;
        this.esiaService = esiaService;
    }

    private String buildPathFromMap(Map<String, String> pathVarsMap){
        String path = "";
        String oid = pathVarsMap.get("oid");
        if (oid != null) {
            path = path + oid;
        }

        String pname = pathVarsMap.get("pname");
        if (pname != null) {
            path = path + "/" + pname;
        }

        String pvalue = pathVarsMap.get("pvalue");
        if (pvalue != null) {
            path = path +"/" + pvalue;
        }

        return path;
    }

    public ResponseEntity<String> authTokenTranslator(Map<String, String> mapHeaders, Map<String, String> mapBody) {
        logger.debug("authTokenTranslator ..");
        HttpEntity<MultiValueMap<String, String>> entity = httpCore.entityFromMaps(mapHeaders, mapBody);
        ResponseEntity<String> responseEntity = httpCore.exchange(HttpMethod.POST, esiaSite + tokenUrl, entity);
        logger.info(responseEntity.getBody());
        return responseEntity;
    }

    public String codeTranslator(Map<String, String> mapHeaders, Map<String, String> mapBody) {
        logger.debug("codeTranslator ..");
        MultiValueMap multiMapBoby = httpCore.multiValueMapFromMap(mapBody);
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme("https").host(esiaSite)
                .queryParams(multiMapBoby).build();

        return uriComponents.toUriString();
    }

    public ResponseEntity<String> prnsTranslator(Map<String, String> headers, Map<String, String> mapBody) {
        logger.debug("prnsTranslator ..");
        String path = buildPathFromMap(mapBody);
        HttpEntity<String> entity = httpCore.entityFromMaps(headers);
        return httpCore.exchange(HttpMethod.GET,esiaSite + personUrl + path, entity);
    }
}

