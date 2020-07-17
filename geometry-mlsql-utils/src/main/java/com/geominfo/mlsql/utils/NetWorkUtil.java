package com.geominfo.mlsql.utils;


import com.geominfo.mlsql.constants.Constants;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
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
        if(CollectionUtils.isEmpty(postParameters))
        {
            log.info("请求参数不能为空!");
            return null ;
        }
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
        headers.add(Constants.ACCEPT, Constants.APPLICATION_JSON);
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
    public ResponseEntity<String> aynPost(String url ,MultiValueMap<String, String> postParameters)
    {
        if(CollectionUtils.isEmpty(postParameters))
        {
            log.info("请求参数不能为空!");
            return  new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

       if( !postParameters.containsKey(Constants.CALLBACK))
       {
           log.info("必须填写回调接口!");
           return  new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
       }

        postParameters.add(Constants.ASYNC, Constants.TRUE);
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
    private ResponseEntity<String> aynNetWorkUtil(String url , MultiValueMap<String, String> postParameters) {

        HttpHeaders headers = new HttpHeaders();
        headers.add(Constants.ACCEPT, Constants.APPLICATION_JSON);
        HttpEntity<MultiValueMap<String, String>> requestEntity =
                new HttpEntity<MultiValueMap<String, String>>(postParameters, headers);
        ListenableFuture<ResponseEntity<String>> entity = asyncRestTemplate().postForEntity(url,
                requestEntity, String.class);

        try {
            return entity.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return new ResponseEntity(HttpStatus.OK) ;
    }


    private AsyncRestTemplate asyncRestTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Constants.ONE_HUNDRED);
        factory.setReadTimeout(Constants.TOW_HUNDRED);
        factory.setTaskExecutor(new SimpleAsyncTaskExecutor());
        return new AsyncRestTemplate(factory);
    }

}