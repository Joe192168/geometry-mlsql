package com.geominfo.mlsql.config.restful;


import com.geominfo.mlsql.globalconstant.GlobalConstant;

/**
 * @program: geometry-mlsql
 * @description: 自定义异常处理类
 * @author: BJZ
 * @create: 2020-07-21 11:40
 * @version: 1.0.0
 */
public class RRException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private String msg;
    private int code = GlobalConstant.FIVE_HUNDRED;

    public RRException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public RRException(String msg, Throwable e) {
        super(msg, e);
        this.msg = msg;
    }

    public RRException(String msg, int code) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }

    public RRException(String msg, int code, Throwable e) {
        super(msg, e);
        this.msg = msg;
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}
