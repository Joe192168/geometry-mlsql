package com.geominfo.mlsql.utils;


import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutionException;

/**
 * @program: geometry-mlsql
 * @description: 网络请求工具类
 * @author: BJZ
 * @create: 2020-06-10 15:18
 * @version: 1.0.0
 */
@Component
@Log4j2
public class HttpUtil {


    private RestTemplate restTemplate = new RestTemplate();

//    Logger logger = LoggerFactory.getLogger(NetWorkUtil.class);

    /**
     * @description: GET同步请求
     * @author: BJZ
     * @date: 2020/6/11 0011
     * @return: 请求结果
     */
    public <T> T synGet(String url) {

        return (T) restTemplate.getForEntity(url, String.class);
    }

    /**
     * @description: POST同步请求
     * @author: BJZ
     * @date: 2020/6/11 0011
     * @param: 请求参数
     * @return: 请求结果
     */
    public <T> T synPost(String url, LinkedMultiValueMap<String, String> postParameters) {
        log.info("synPost url = " + url);
        return synNetWorkUtil(url, postParameters);
    }

    private <T> T synNetWorkUtil(String url, LinkedMultiValueMap<String, String> postParameters) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");
        HttpEntity<MultiValueMap<String, String>> requestEntity =
                new HttpEntity<MultiValueMap<String, String>>(postParameters, headers);
        ResponseEntity<String> responseEntityPost  = restTemplate.postForEntity(url,
                requestEntity, String.class);
//        String result = responseEntityPost.getBody();
//        logger.info("同步请求返回数据 = " + result);

        return (T) responseEntityPost;
    }


    /**
     * @description: POST异步请求 (必须填写回调接口，这是engine框架接口要求的）
     * @author: BJZ
     * @date: 2020/6/11 0011
     * @param: 参数
     * @return:
     */
    public <T> T aynPost(String url, LinkedMultiValueMap<String, String> postParameters) throws ExecutionException, InterruptedException {

//        if (!postParameters.containsKey("callback")) {
//            log.info("必须填写回调接口!");
//            return (T) "必须填写回调接口!";
//        }
//
//        if (postParameters.containsKey("callback")) {
//            if (postParameters.get("callback").size() == 0)
//                return (T) "回调接口空";
//        }
//        postParameters.add("async", "true");
        return aynNetWorkUtil(url, postParameters);

    }

    private <T> T aynNetWorkUtil(String url, LinkedMultiValueMap<String, String> postParameters) throws ExecutionException, InterruptedException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");
        HttpEntity<MultiValueMap<String, String>> requestEntity =
                new HttpEntity<MultiValueMap<String, String>>(postParameters, headers);
        ListenableFuture<ResponseEntity<String>> entity  = asyncRestTemplate().postForEntity(url,
                requestEntity, String.class);
        return (T) entity.get();

    }

    private AsyncRestTemplate asyncRestTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(100);
        factory.setReadTimeout(200);
        factory.setTaskExecutor(new SimpleAsyncTaskExecutor());
        return new AsyncRestTemplate(factory);
    }

}