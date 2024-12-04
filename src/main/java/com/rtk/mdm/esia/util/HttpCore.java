package com.rtk.mdm.esia.util;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.TrustStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

@Service
public class HttpCore {
    @Value("${esia.site}")
    private String esiaSite;

    @Value("${esia.our.url}")
    private String esiaOurUrl;

    @Value("${proxy.host}")
    private String proxyHost;

    @Value("${proxy.port}")
    private Integer proxyPort;

    private Logger logger = LoggerFactory.getLogger(HttpCore.class);

    public RestTemplate restTemplate2() {
        if ((proxyHost != null) && (!proxyHost.equals(""))) {
            SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
            requestFactory.setProxy(proxy);
            return new RestTemplate(requestFactory);
        } else {
            return new RestTemplate();
        }
    }

    public RestTemplate restTemplate() {
        if ((proxyHost != null) && (!proxyHost.equals(""))) {
            SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
            requestFactory.setProxy(proxy);
            return new RestTemplate(requestFactory);
        } else {
            TrustStrategy acceptingTrustStrategy = (x509Certificates, s) -> true;
            SSLContext sslContext = null;
            try {
                sslContext = org.apache.http.ssl.SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
            } catch (NoSuchAlgorithmException e) {
                logger.error("NoSuchAlgorithmException " + e);
            } catch (KeyManagementException e) {
                logger.error("KeyManagementException " + e);
            } catch (KeyStoreException e) {
                logger.error("KeyStoreException " + e);
            }
            SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());
            CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
            HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
            requestFactory.setHttpClient(httpClient);
            RestTemplate restTemplate = new RestTemplate(requestFactory);
            return restTemplate;
        }
    }

    private HttpHeaders headerFromMap(Map<String, String> map) {
        HttpHeaders headers = new HttpHeaders();
        map.forEach(headers::set);
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }

    public MultiValueMap multiValueMapFromMap(Map<String, String> map) {
        MultiValueMap multiValueMap = new LinkedMultiValueMap();
        map.forEach(multiValueMap::add);
        return multiValueMap;
    }

    public HttpEntity<MultiValueMap<String, String>> entityFromMaps(Map<String, String> mapHeaders, Map<String, String> mapBody) {
        MultiValueMap multiMapBoby = multiValueMapFromMap(mapBody);
        HttpHeaders headers = headerFromMap(mapHeaders);
        HttpEntity<MultiValueMap<String, String>> entity =
                new HttpEntity<MultiValueMap<String, String>>(multiMapBoby, headers);
        return entity;
    }

    public HttpEntity<String> entityFromMaps(Map<String, String> mapHeaders) {
        HttpHeaders headers = headerFromMap(mapHeaders);
        HttpEntity<String> entity =
                new HttpEntity<>(headers);
        return entity;
    }

    private String replaceUrls(String str) {
        if (str == null) return null;
        return str.replace(esiaSite, esiaOurUrl);
    }

    public ResponseEntity<String> exchange(HttpMethod method, String path, HttpEntity entity) {
        logger.info("request to :" + path + " method " + method.name() + entity.toString());
        //вот эта штука отключает верификацию сертификата. по типу --no-check-certificate в wget
        HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);

        RestTemplate restTemplate = restTemplate();

        ResponseEntity<String> response = null;
        try {
            response =
                    restTemplate.exchange(path,
                            method,
                            entity, String.class);
            logger.info("got response! " + response.toString());
        } catch (ResourceAccessException e) {
            logger.error("ResourceAccessException" + e);
        } catch (RestClientException e) {
            logger.error("RestClientException " + e);
            response = new ResponseEntity<String>(((HttpClientErrorException) e).getResponseBodyAsString(),
                    ((HttpClientErrorException) e).getResponseHeaders(),
                    ((HttpClientErrorException) e).getStatusCode());

        }

        //Делаем подмену esiaSite на наш url если встречается в теле ответа.
        return new ResponseEntity<String>(replaceUrls(response.getBody()), response.getHeaders(), response.getStatusCode());
    }
}
