package com.rtk.mdm.esia.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rtk.mdm.esia.dto.TokenDto;
import com.rtk.mdm.esia.dto.personal.*;
import com.rtk.mdm.esia.session.TokenInfo;
import com.rtk.mdm.esia.util.HttpCore;
import com.rtk.mdm.esia.util.StrUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
public class EsiaService {
    @Value("${esia.client_id}")
    private String clientId;

    @Value("${esia.site}")
    private String esiaSite;

    @Value("${esia.site.auth.code}")
    private String esiaSiteAuthCode;

    @Value("${esia.redirect_url}")
    private String redirectUrl;

    @Value("${esia.scope}")
    private String scope;

    @Value("${esia.oauth2.url}")
    private String oAuth2Url;

    @Value("${esia.token.url}")
    private String tokenUrl;

    @Value("${esia.token.result}")
    private String tokenResult;

    @Value("${esia.response.person}")
    private String personUrl;

    @Value("${token.save.default.system}")
    private String tokenSaveDefaultSystem;

    private final int MAX_QUEUE_SIZE = 100_000;
    private final LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<>(MAX_QUEUE_SIZE);
    private final ExecutorService executor = new ThreadPoolExecutor(1, 1, 1, TimeUnit.MINUTES, queue);

    private final TokenInfo tokenInfo;
    private final SignerService signerService;
    private final HttpCore httpCore;

    private Logger logger = LoggerFactory.getLogger(EsiaService.class);

    @Autowired
    public EsiaService(TokenInfo tokenInfo, SignerService signerService, HttpCore httpCore) {
        this.tokenInfo = tokenInfo;
        this.signerService = signerService;
        this.httpCore = httpCore;
    }

    public String getLoginUrl() throws Exception {
        String state = UUID.randomUUID().toString();
        String timestamp = getEsiaDateNow();
        String clientMsg = scope + timestamp + clientId + state;
        byte[] signMessage = signerService.signMessageGost(clientMsg.getBytes());
        byte[] clientSecret = Base64.getEncoder().encode(signMessage);
        String base =  esiaSiteAuthCode + oAuth2Url;
        String params =
                "?timestamp=" + URLEncoder.encode(timestamp, "UTF-8") +
                        "&scope=" + scope +
                        "&client_secret=" + URLEncoder.encode(new String(clientSecret), "UTF-8") +
                        "&response_type=code" +
                        "&redirect_uri=" + redirectUrl +
                        "&state=" + state +
                        "&client_id=" + clientId +
                        "&access_type=offline";
        logger.info(base+params);
        return base + params;
    }

    public String getLoginUrlTest() throws Exception {
        String state = UUID.randomUUID().toString();
        String timestamp = getEsiaDateNow();
        String clientMsg = scope + timestamp + clientId + state;
        byte[] signMessage = signerService.signMessageGost(clientMsg.getBytes());
        byte[] clientSecret = Base64.getEncoder().encode(signMessage);
        String base =  esiaSiteAuthCode + oAuth2Url;
        String params =
                "?timestamp=" + URLEncoder.encode(timestamp, "UTF-8") +
                        "&scope=" + scope +
                        "&client_secret=" + URLEncoder.encode(new String(clientSecret), "UTF-8") +
                        "&response_type=code" +
                        "&redirect_uri=" + redirectUrl + "test" +
                        "&state=" + state +
                        "&client_id=" + clientId +
                        "&access_type=offline";
        logger.info(base+params);
        return base + params;
    }

    public ResponseEntity<String> signMessage(String message) throws Exception {
        logger.info("message: " + message);
        byte[] signMessage = signerService.signMessageGost(message.getBytes());
        String result = new String(Base64.getEncoder().encode(signMessage));
        logger.info("sig message: " + result);
        return ResponseEntity.ok(result);
    }

    public ResponseEntity<String> getToken(String code) throws Exception {
        logger.info("getToken start..");
        String timestamp = getEsiaDateNow();
        String state = UUID.randomUUID().toString();
        String clientMsg = scope + timestamp + clientId + state;
        byte[] signMessage = signerService.signMessageGost(clientMsg.getBytes());
        byte[] clientSecret = Base64.getEncoder().encode(signMessage);
        String grantPype = "authorization_code";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();

        map.add("grant_type",       grantPype);
        map.add("timestamp",        timestamp);
        map.add("code",             code);
        map.add("redirect_uri",     tokenResult);
        map.add("client_id",        clientId);
        map.add("client_secret",    new String(clientSecret));
        map.add("state",            state);
        map.add("scope",            scope);
        logger.info("prepare get token HttpEntity..");
        HttpEntity<MultiValueMap<String, String>> entity =
                new HttpEntity<MultiValueMap<String, String>>(map, headers);


        ResponseEntity<String> exchange = httpCore.exchange(HttpMethod.POST, esiaSite + tokenUrl, entity);
        return exchange;
    }

    public ResponseEntity<String> getTokenTest(String code) throws Exception {
        logger.info("getToken start..");
        String timestamp = getEsiaDateNow();
        String state = UUID.randomUUID().toString();
        String clientMsg = scope + timestamp + clientId + state;
        byte[] signMessage = signerService.signMessageGost(clientMsg.getBytes());
        byte[] clientSecret = Base64.getEncoder().encode(signMessage);
        String grantPype = "authorization_code";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();

        map.add("grant_type",       grantPype);
        map.add("timestamp",        timestamp);
        map.add("code",             code);
        map.add("redirect_uri",     tokenResult);
        map.add("client_id",        clientId);
        map.add("client_secret",    new String(clientSecret));
        map.add("state",            state);
        map.add("scope",            scope);
        logger.info("prepare get token HttpEntity..");
        HttpEntity<MultiValueMap<String, String>> entity =
                new HttpEntity<MultiValueMap<String, String>>(map, headers);

        return ResponseEntity.ok(entity.getBody().toString());//httpCore.exchange(HttpMethod.POST, esiaSite + tokenUrl, entity);
    }

    public ResponseEntity<TokenInfo> getTokenInfo(){
        ResponseEntity<TokenInfo>  response = ResponseEntity.ok(tokenInfo.getTokenInfo());
        return response;
    }

    private Integer getUserIdByToken(String token) {
        logger.debug("starting getUserIdByToken, split..");
        String payload = token.split("\\." )[1];
        logger.debug("decode payload..");
        byte[] bb = Base64.getUrlDecoder().decode(payload);
        String st = new String(bb);
        Integer sbj_id = null;
        try {
            logger.info("pharsing json");
            JSONObject object = new JSONObject(st);
            sbj_id = (Integer) object.get("urn:esia:sbj_id");
        }
        catch (JSONException e){
            logger.error("PHARSE USER ERROR " + e);
        }
        return sbj_id;
    }

    private Person parsePersonFromPrns(String json){
        Person person = null;
        ObjectMapper om = new ObjectMapper();
        try {
            person = om.readValue(json, Person.class);
        } catch (JsonProcessingException e) {
            logger.error("parsePerson error" + e);
        }
        return person;
    }

    private Elements parseCtts(String json){
        Elements ctts = null;
        ObjectMapper om = new ObjectMapper();
        try {
            ctts = om.readValue(json, Elements.class);
        } catch (JsonProcessingException e) {
            logger.error("parseCtts error" + e);
        }
        return ctts;
    }

    private Contact parseContact(String json){
        Contact contact = null;
        ObjectMapper om = new ObjectMapper();
        try {
            contact = om.readValue(json, Contact.class);
        } catch (JsonProcessingException e) {
            logger.error("parseContact error" + e);
        }
        return contact;
    }

    private Address parseAddress(String json){
        Address address = null;
        ObjectMapper om = new ObjectMapper();
        try {
            address = om.readValue(json, Address.class);
        } catch (JsonProcessingException e) {
            logger.error("parseAddress error" + e);
        }
        return address;
    }

    private Document parseDocument(String json){
        Document doc = null;
        ObjectMapper om = new ObjectMapper();
        try {
            doc = om.readValue(json, Document.class);
        } catch (JsonProcessingException e) {
            logger.error("parseDocument error" + e);
        }
        return doc;
    }

    private HttpEntity<String> createEntity(String token){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.set("Authorization", "Bearer " + token);
        return new HttpEntity<>(headers);
    }

    public Person getPrnsByToken(String token) {
        HttpEntity<String> entity = createEntity(token);
        logger.debug("getPrnsByToken..");
        ResponseEntity<String> exchange = httpCore.exchange(HttpMethod.GET, esiaSite + personUrl + getUserIdByToken(token), entity);
        return parsePersonFromPrns(exchange.getBody());
    }

    public Elements getCttsListByToken(String token) {
        HttpEntity<String> entity = createEntity(token);
        logger.debug("getCttsListByToken..");
        ResponseEntity<String> exchange = httpCore.exchange(HttpMethod.GET, esiaSite + personUrl + getUserIdByToken(token) + "/ctts/", entity);
        return parseCtts(exchange.getBody());
    }

    public Contact getContactByToken(String token, String url) {
        HttpEntity<String> entity = createEntity(token);
        logger.debug("getContactByToken..");
        ResponseEntity<String> exchange = httpCore.exchange(HttpMethod.GET, url, entity);
        return parseContact(exchange.getBody());
    }

    public Elements getAddrsListByToken(String token) {
        HttpEntity<String> entity = createEntity(token);
        logger.debug("getAddrsListByToken..");
        ResponseEntity<String> exchange = httpCore.exchange(HttpMethod.GET, esiaSite + personUrl + getUserIdByToken(token) + "/addrs/", entity);
        return parseCtts(exchange.getBody());
    }

    public Address getAddressByToken(String token, String url) {
        HttpEntity<String> entity = createEntity(token);
        logger.debug("getAddressByToken..");
        ResponseEntity<String> exchange = httpCore.exchange(HttpMethod.GET, url, entity);
        return parseAddress(exchange.getBody());
    }

    public Elements getDocumentsByToken(String token) {
        HttpEntity<String> entity = createEntity(token);
        logger.debug("getDocumentsByToken..");
        ResponseEntity<String> exchange = httpCore.exchange(HttpMethod.GET, esiaSite + personUrl + getUserIdByToken(token) + "/docs/", entity);
        return parseCtts(exchange.getBody());
    }

    public Document getDocumentByToken(String token, String url) {
        HttpEntity<String> entity = createEntity(token);
        logger.debug("getDocumentByToken..");
        ResponseEntity<String> exchange = httpCore.exchange(HttpMethod.GET, url, entity);
        return parseDocument(exchange.getBody());
    }

    public ResponseEntity<String> getUserInfo() {
        HttpEntity<String> entity = createEntity(tokenInfo.getAccess_token());
        logger.debug("get response user info http entity..");
        return httpCore.exchange(HttpMethod.GET, esiaSite + personUrl + getUserIdByToken(tokenInfo.getAccess_token()), entity);
    }

    public ResponseEntity<String> logout() {
        logger.debug("logout info ..");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.set("Authorization", "Bearer " + tokenInfo.getAccess_token());
        logger.debug("prepare logout http entity..");
        HttpEntity<String> entity =
                new HttpEntity<>(headers);
        logger.debug("logout http entity..");
        ResponseEntity<String>  response =
                httpCore.exchange(HttpMethod.GET,
                        esiaSite + "/idp/ext/Logout?client_id=" + getUserIdByToken(tokenInfo.getAccess_token()), entity);
        return response;
    }

    public String getUserIDInfo() {
        logger.debug("user info ..");
        return "UserID:" + getUserIdByToken(tokenInfo.getAccess_token());
    }

    private String getEsiaDateNow(){
        //2020.10.08 22:30:52 +0300
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss Z");
        return sdf.format(new Date());
    }

    public Person getAllPersonInfoByToken(String token, String systemCode){
        Person person = getPrnsByToken(token);
        person.setRawId(getUserIdByToken(token).longValue());
        Elements ctts = getCttsListByToken(token);
        if ((ctts != null) && (ctts.getSize() > 0)){
            for (String el:ctts.getElements()){
                Contact contact = getContactByToken(token, el);
                fillPersonContact(person, contact);
            }
        }

        Elements addrs = getAddrsListByToken(token);
        if ((addrs != null) && (addrs.getSize() > 0)){
            for (String el:addrs.getElements()){
                Address address = getAddressByToken(token, el);
                fillPersonAddress(person, address);
            }
        }

        Elements docs = getDocumentsByToken(token);
        if ((docs != null) && (docs.getSize() > 0)){
            for (String el:docs.getElements()){
                logger.info("search passport");
                Document doc = getDocumentByToken(token, el);
                logger.info("---passport " + doc);
                fillPersonDocument(person, doc);
            }
        }
        return person;
    }

    private void fillPersonContact(Person person, Contact contact) {
        switch (contact.getType()) {
            case "MBT":
                person.setMobilePhone(contact.getValue());
                break;
            case "PHN":
                person.setHomePhone(contact.getValue());
                break;
            case "EML":
                person.setEmail(contact.getValue());
                break;
            default:
                logger.error("Unknown contact");
        }
    }

    private void fillPersonAddress(Person person, Address address) {
        switch (address.getType()) {
            case "PLV":
                person.setLiveAddress(buildAddress(address));
                break;
            case "PRG":
                person.setRegistrationAddress(buildAddress(address));
                break;
            default:
                logger.error("Unknown address");
        }
    }

    private void fillPersonDocument(Person person, Document doc) {
        if (doc == null){
            return;
        }
        switch (doc.getType()) {
            case "RF_PASSPORT":
                person.setPassport(doc);
                logger.info("passport " + doc.getSeries() + doc.getNumber());
                break;
            default:
                logger.error("Unknown document " + doc.getType());
        }
    }

    private String buildAddress(Address address){
        String zipCode = address.getZipCode();
        String addressStr = address.getAddressStr();
        String house = address.getHouse();
        String flat = address.getFlat();
        StringBuilder fullAddress = new StringBuilder();
        if (!StrUtils.isEmpty(zipCode)){
            fullAddress.append(zipCode).append(", ");
        }

        fullAddress.append(addressStr);

        if (!StrUtils.isEmpty(house)){
            fullAddress.append(" дом ").append(house);
        }

        if (!StrUtils.isEmpty(flat)){
            fullAddress.append(" кв ").append(flat);
        }

        return fullAddress.toString();
    }

    private String toJson(Object obj){
        if (obj == null) {
            return null;
        }
        String json = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            json = mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            logger.error("toJson" + e);
        }
        return json;
    }

    public Person getAllPersonInfo(String token, String systemCode){
        return getAllPersonInfoByToken(token, systemCode);
    }


    public TokenDto pharseToken(String json){
        ObjectMapper om = new ObjectMapper();
        TokenDto token = null;
        try {
            token = om.readValue(json, TokenDto.class);
        } catch (JsonProcessingException e) {
            logger.error("pharseToken" + e);
        }
        return token;
    }
}
