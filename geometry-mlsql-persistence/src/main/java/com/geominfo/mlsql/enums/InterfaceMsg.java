package com.geominfo.mlsql.enums;

/**
 * @program: geometry-bi
 * @description:
 * @author: LF
 * @create: 2021/5/20 15:50
 * @version: 1.0.0
 */
public enum InterfaceMsg {


    QUERY_SUCCESS("查询成功！"),
    QUERY_ERROR("查询时发生异常，请稍后再试!"),;

    private String msg;

    InterfaceMsg(String msg){
        this.msg = msg;
    }
    public String getMsg() {
        return msg;
    }
}
