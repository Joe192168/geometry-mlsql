package com.geominfo.mlsql.services;  /**
 * @title: ScriptService
 * @projectName geometry-mlsql
 * @description: TODO
 * @author Lenovo
 * @date 2021/6/214:30
 */

import java.math.BigDecimal;

/**
 * @Auther zrd
 * @Date 2021-06-02 14:30 
 *
 */

public interface ScriptService {

    /***
     * @Description: 创建文件夹接口
     * @Author: zrd
     * @Date: 2021/6/3 14:28
     * @param parentId
     * @return boolean
     */
    boolean mkdir(BigDecimal parentId,BigDecimal owner,String dirName);

    /***
     * @Description: 删除文件夹接口
     * @Author: zrd
     * @Date: 2021/6/3 15:45
     * @param resourceId 文件夹资源id
     * @return boolean
     */
    boolean deleteDir(BigDecimal resourceId);

    boolean modifyDirName(String name,BigDecimal resourceId,BigDecimal parentId);

    boolean deleteScript(BigDecimal resourceId);
}
