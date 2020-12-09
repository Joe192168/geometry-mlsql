package com.geominfo.mlsql.util;

import com.geominfo.mlsql.domain.vo.Message;
import com.geominfo.mlsql.globalconstant.ReturnCode;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @program: geometry-mlsql
 * @description: ReturnUtil
 * @author: BJZ
 * @create: 2020-12-07 16:16
 * @version: 1.0.0
 */
@Component
public class ReturnUtil {

    public  Message returnValue(Map<Integer ,Object> resMap)
    {
        int statudsCode = 500;
        String res = "" ;
        if(resMap != null && !resMap.isEmpty()){
            statudsCode = resMap.keySet().iterator().next() ;
            res = (String) resMap.values().iterator().next();
        }
        return statudsCode == 200 ?
                success(statudsCode, "success").addData("data", res) :
                error(statudsCode, "error").addData("data", res);

    }

    private  Message success(int statusCode,String statusMsg){
        Message message = new Message();
        message.ok(statusCode, statusMsg);
        return message;
    }

    private  Message error(int statusCode,String statusMsg){
        Message message = new Message();
        message.error(statusCode, statusMsg);
        return message;
    }

}