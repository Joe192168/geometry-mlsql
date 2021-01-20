package com.geominfo.mlsql.service.proxy;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.util.LinkedMultiValueMap;

import java.net.URI;
import java.util.Map;

/**
 * @program: MLSQL CONSOLE后端接口
 * @description: ProxyService
 * @author: anan
 * @create: 2020-11-26 16:34
 * @version: 1.0.0
 */
public interface ProxyService {
    <T> ResponseEntity<T> postForEntity(String url, LinkedMultiValueMap<String, String> postParameters, Class<T> responseType);
    <T> ResponseEntity<T> postForEntity(URI url, LinkedMultiValueMap<String, String> postParameters, Class<T> responseType);
    <T> ResponseEntity<T> postForEntity(String url, LinkedMultiValueMap<String, String> postParameters, Class<T> responseType, Object... uriVariables);
    <T> ResponseEntity<T> postForEntity(String url, String josnParams, Class<T> responseType);
    <T> ResponseEntity<T> getForEntity(String url, Class<T> responseType,  Object... uriVariables);
    <T> ResponseEntity<T> getForEntity(String url, Class<T> responseType);
    <T> ResponseEntity<T> getForEntity(URI url, Class<T> responseType);
}
