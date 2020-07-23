package com.geominfo.mlsql.utils;




import lombok.extern.log4j.Log4j2;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
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
public class NetWorkUtil {


   private RestTemplate restTemplate = new RestTemplate() ;

    /**
      * @description:  同步请求
      *
      * @author: BJZ
      *
      * @date: 2020/6/11 0011
      *
      * @param: 请求参数
      *
      * @return: 请求结果
     */
    public ResponseEntity<String> synPost(String url , MultiValueMap<String, String> postParameters){
        log.info("synPost url = "+ url);
        return synNetWorkUtil(url ,postParameters) ;
    }

    /**
      * @description: 同步请求
      *
      * @author: BJZ
      *
      * @date: 2020/6/11 0011
      *
      * @param: 请求参数
      *
      * @return: ResponseEntity<String>
     */

    private ResponseEntity<String> synNetWorkUtil(String url  , MultiValueMap<String, String> postParameters) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");
        HttpEntity<MultiValueMap<String, String>> requestEntity =
                new HttpEntity<MultiValueMap<String, String>>(postParameters, headers);
        ResponseEntity<String> responseEntityPost = restTemplate.postForEntity(url,
                requestEntity, String.class);
        return responseEntityPost ;
    }


    /**
      * @description:  异步请求 (必须填写回调接口，这是engine框架接口要求的）
      *
      * @author: BJZ
      *
      * @date: 2020/6/11 0011
      *
      * @param: 参数
      *
      * @return:
     */
    public ResponseEntity<String> aynPost(String url ,MultiValueMap<String, String> postParameters) throws ExecutionException, InterruptedException {

       if( !postParameters.containsKey("callback"))
       {
           log.info("必须填写回调接口!");
           return  new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
       }

        postParameters.add("async", "true");
       return aynNetWorkUtil(url ,postParameters) ;

    }


    /**
      * @description: 异步请求
      *
      * @author: BJZ
      *
      * @date: 2020/6/11 0011
      *
      * @param: 参数
      *
      * @return: void
     */
    private ResponseEntity<String> aynNetWorkUtil(String url , MultiValueMap<String, String> postParameters) throws ExecutionException, InterruptedException {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");
        HttpEntity<MultiValueMap<String, String>> requestEntity =
                new HttpEntity<MultiValueMap<String, String>>(postParameters, headers);
        ListenableFuture<ResponseEntity<String>> entity = asyncRestTemplate().postForEntity(url,
                requestEntity, String.class);
            return entity.get();

    }


    private AsyncRestTemplate asyncRestTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(100);
        factory.setReadTimeout(200);
        factory.setTaskExecutor(new SimpleAsyncTaskExecutor());
        return new AsyncRestTemplate(factory);
    }

}