package com.geominfo.mlsql.support;

import com.geominfo.mlsql.domain.vo.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @program: geometry-bi
 * @description: 全局的的异常拦截器（拦截所有的控制器）（带有@RequestMapping注解的方法上都会拦截）
 * @author: 肖乔辉
 * @create: 2018-08-17 15:39
 * @version: 1.0.0
 */
@RestControllerAdvice
@Order(-1)
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    /* *
     * @Description 拦截数据库异常
     * @Param [e] 数据库访问异常
     * @Return Message
     */
    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.OK)
    public Message sqlException(DataAccessException e) {
        LOGGER.error("数据操作异常:",e);
        return new Message().error(500, e.getMessage());
    }

    /* *
     * @Description 拦截未知的运行时异常
     * @Param [e] 运行时异常
     * @Return Message
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.OK)
    public Message notFoundException(RuntimeException e) {
        LOGGER.error("运行时异常:",e);
        return new Message().error(500,e.getMessage());
    }
}
