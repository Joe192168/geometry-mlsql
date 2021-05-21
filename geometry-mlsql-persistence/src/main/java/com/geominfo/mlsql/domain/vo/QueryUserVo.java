package com.geominfo.mlsql.domain.vo;

import com.geominfo.mlsql.domain.vo.base.PageInfoVo;
import org.apache.commons.lang3.StringUtils;

/**
 * @program: geometry-bi
 * @description: 查人员信息参数实体
 * @author: LF
 * @create: 2021/5/21 11:15
 * @version: 1.0.0
 */
public class QueryUserVo extends PageInfoVo {
    //编码
    private String code;

    //查询名称
    private String name;

    public String getCode() {
        if (StringUtils.isBlank(code))
            return null;
        else
            return code.toLowerCase();
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        if (StringUtils.isBlank(name))
            return null;
        else
            return name.toLowerCase();
    }

    public void setName(String name) {
        this.name = name;
    }

}
