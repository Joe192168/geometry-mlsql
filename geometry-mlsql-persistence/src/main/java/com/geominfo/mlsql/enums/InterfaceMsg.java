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
    QUERY_ERROR("查询时发生异常，请稍后再试!"),
    RESOURCE_NAME_EXIT("名称已存在！"),
    INSERT_ERROR("新增时发生异常！"),
    INSERT_SUCCESS("新增成功！"),
    UPDATE_ERROR("修改时发生异常！"),
    UPDATE_SUCCESS("修改成功！"),
    DELETE_ERROR("删除时发生异常！"),
    DELETE_SUCCESS("删除成功！"),;

    private String msg;

    InterfaceMsg(String msg){
        this.msg = msg;
    }
    public String getMsg() {
        return msg;
    }
}
