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
    DELETE_SUCCESS("删除成功！"),
    INSERT_SPACE_FAIL("新增工作空间失败！"),
    INSERT_SPACE_SUCCESS("新增工作空间成功！"),
    INSERT_SPACE_MEMBER_FAIL("新增工作空间成员失败！"),
    INSERT_SPACE_MEMBER_SUCCESS("新增工作空间成员成功！"),
    SET_DEFAULT_SPACE_SUCCESS("设置默认工作空间成功！"),
    SET_DEFAULT_SPACE_FAIL("设置默认工作空间失败！"),
    SET_DEFAULT_SPACE_ERROR("设置默认工作空间时发生异常！"),
    SPACE_NOT_TRANSFER("默认空间不能转让！"),
    TRANSFER_SPACE_SUCCESS("转让工作空间成功！"),
    TRANSFER_SPACE_FAIL("转让工作空间失败！"),
    INSERT_ENGINE_SUCCESS("新增引擎成功！"),
    INSERT_ENGINE_FAIL("新增引擎失败！"),
    UPDATE_ENGINE_SUCCESS("编辑引擎成功！"),
    UPDATE_ENGINE_FAIL("编辑引擎失败！"),
    INSERT_SPACE_ENGINE_SUCCESS("添加工作空间引擎成功！"),
    INSERT_SPACE_ENGINE_FAIL("添加工作空间引擎失败！"),
    SET_DEFAULT_ENGINE_SUCCESS("设置工作空间默认引擎成功！"),
    SET_DEFAULT_ENGINE_FAIL("设置工作空间默认引擎失败！"),
    SHARE_SCRIPT_SUCCESS("分享脚本成功！"),
    SHARE_SCRIPT_FAIL("分享脚本失败！"),
    ;

    private String msg;

    InterfaceMsg(String msg){
        this.msg = msg;
    }
    public String getMsg() {
        return msg;
    }
}
