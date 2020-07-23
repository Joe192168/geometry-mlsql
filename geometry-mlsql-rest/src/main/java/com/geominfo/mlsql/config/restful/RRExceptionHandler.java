package com.geominfo.mlsql.config.restful;


import com.geominfo.mlsql.domain.vo.Message;
import com.geominfo.mlsql.globalconstant.GlobalConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.util.HashMap;

/**
 * @program: geometry-mlsql
 * @description: 异常统一处理
 * @author: BJZ
 * @create: 2020-07-21 11:39
 * @version: 1.0.0
 */
@RestControllerAdvice
public class RRExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(RRException.class)
    public Message handleRRException(RRException e) {
        Message r = new Message();
        HashMap<String ,Object> hashMap =  new HashMap();
        hashMap.put("code", e.getCode()) ;
        hashMap.put("msg", e.getMessage()) ;
        r.setData(hashMap);
        return r;
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public Message handleDuplicateKeyException(DuplicateKeyException e) {
        logger.error(e.getMessage(), e);
        return new Message(GlobalConstant.REORD_EXISTING) ;
    }

    @ExceptionHandler(Exception.class)
    public Message handleException(Exception e) {
        logger.error(e.getMessage(), e);
        return new Message().error();
    }
}
