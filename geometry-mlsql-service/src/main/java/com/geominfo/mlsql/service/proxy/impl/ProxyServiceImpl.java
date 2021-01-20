package com.geominfo.mlsql.service.proxy.impl;

import com.geominfo.mlsql.service.proxy.ProxyService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

/**
 * @program: MLSQL CONSOLE后端接口
 * @description: ProxyServiceImpl
 * @author: anan
 * @create: 2020-11-26 16:35
 * @version: 1.0.0
 */
@Service
@Log4j2
public class ProxyServiceImpl<T> implements ProxyService{
    @Autowired
    private RestTemplate restTemplate ;

    /**
     * get HttpEntity
     * @param postParameters
     * @return
     */
    private  HttpEntity<MultiValueMap<String, String>> getHttpEntity(LinkedMultiValueMap<String, String> postParameters){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(postParameters, headers);
        return requestEntity;
    }

    private  HttpEntity<String> getHttpEntity(String formEntity){
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<String> requestEntity = new HttpEntity<String>(formEntity, headers);
        return requestEntity;
    }


    /**
     * post 请求url带变量
     * @param url
     * @param postParameters
     * @param responseType
     * @param uriVariables
     * @param <T>
     * @return
     */
    @Override
    public <T> ResponseEntity<T> postForEntity(String url,
                                               LinkedMultiValueMap<String, String> postParameters,
                                               Class<T> responseType, Object... uriVariables) {
        HttpEntity<MultiValueMap<String, String>> requestEntity = getHttpEntity(postParameters);
        ResponseEntity<T> responseEntityPost  = restTemplate.postForEntity(url, requestEntity, responseType, uriVariables);
        return responseEntityPost;
    }

    @Override
    public <T> ResponseEntity<T> postForEntity(String url,String jsonParam, Class<T> responseType) {
        HttpEntity<String> httpEntity = getHttpEntity(jsonParam);
        ResponseEntity<T> responseEntityPost  = restTemplate.postForEntity(url, httpEntity, responseType);
        return responseEntityPost;
    }

    /**
     * post
     * @param url
     * @param postParameters
     * @param responseType
     * @param <T>
     * @return
     */
    @Override
    public <T> ResponseEntity<T> postForEntity(String url,
                                               LinkedMultiValueMap<String, String> postParameters,
                                               Class<T> responseType) {
        HttpEntity<MultiValueMap<String, String>> requestEntity = getHttpEntity(postParameters);
        ResponseEntity<T> responseEntityPost  = restTemplate.postForEntity(url, requestEntity, responseType);
        return responseEntityPost;
    }

    /**
     * post url is java.net.URL
     * @param url
     * @param postParameters
     * @param responseType
     * @param <T>
     * @return
     */
    @Override
    public <T> ResponseEntity<T> postForEntity(URI url, LinkedMultiValueMap<String, String> postParameters, Class<T> responseType) {
        HttpEntity<MultiValueMap<String, String>> requestEntity = getHttpEntity(postParameters);
        ResponseEntity<T> responseEntityPost  = restTemplate.postForEntity(url, requestEntity, responseType);
        return responseEntityPost;
    }

    /**
     * get  请求url带变量
     * @param url
     * @param responseType
     * @param uriVariables
     * @param <T>
     * @return
     */
    @Override
    public <T> ResponseEntity<T> getForEntity(String url, Class<T> responseType, Object... uriVariables) {
        ResponseEntity<T> responseEntityPost  = restTemplate.getForEntity(url,responseType,uriVariables);
        return responseEntityPost;
    }

    /**
     * get
     * @param url
     * @param responseType
     * @param <T>
     * @return
     */
    @Override
    public <T> ResponseEntity<T> getForEntity(String url, Class<T> responseType) {
        ResponseEntity<T> responseEntityPost  = restTemplate.getForEntity(url,responseType);
        return responseEntityPost;
    }

    /**
     * get url is java.net.URL
     * @param url
     * @param responseType
     * @param <T>
     * @return
     */
    @Override
    public <T> ResponseEntity<T> getForEntity(URI url, Class<T> responseType) {
        ResponseEntity<T> responseEntityPost  = restTemplate.getForEntity(url,responseType);
        return responseEntityPost;
    }
}
