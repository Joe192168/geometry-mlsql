package com.geominfo.mlsql.config.restful;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClientException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * @program: MLSQL CONSOLE后端接口
 * @description: CustomResponseErrorHandler
 * @author: anan
 * @create: 2020-06-08 16:29
 * @version: 1.0.0
 */
public class CustomResponseErrorHandler implements ResponseErrorHandler {
    private ResponseErrorHandler errorHandler = new DefaultResponseErrorHandler();
    @Override
    public void handleError(ClientHttpResponse response) throws IOException {

        List<String> customHeader = response.getHeaders().get("x-app-err-id");

        String svcErrorMessageID = "";
        if (customHeader != null) {
            svcErrorMessageID = customHeader.get(0);
        }
        String body = convertStreamToString(response.getBody());

        try {

            errorHandler.handleError(response);

        } catch (RestClientException scx) {

            throw new CustomException(scx.getMessage(), scx, body);
        }
    }
    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return errorHandler.hasError(response);
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }
}
