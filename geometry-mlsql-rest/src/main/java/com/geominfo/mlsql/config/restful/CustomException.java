package com.geominfo.mlsql.config.restful;

import org.springframework.web.client.RestClientException;

/**
 * @program: MLSQL CONSOLE后端接口
 * @description: CustomException
 * @author: anan
 * @create: 2020-06-08 16:31
 * @version: 1.0.0
 */
public class CustomException extends RestClientException {
    private RestClientException restClientException;
    private String body;

    public RestClientException getRestClientException() {
        return restClientException;
    }

    public void setRestClientException(RestClientException restClientException) {
        this.restClientException = restClientException;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }


    public CustomException(String msg, RestClientException restClientException, String body) {
        super(msg);
        this.restClientException = restClientException;
        this.body = body;
    }
}
