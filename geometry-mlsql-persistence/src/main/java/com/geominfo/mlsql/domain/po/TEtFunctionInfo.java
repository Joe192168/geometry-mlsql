package com.geominfo.mlsql.domain.po;

import com.baomidou.mybatisplus.extension.api.R;
import com.geominfo.mlsql.commons.PageResult;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @title: TEtFunctionInfo
 * @date 2021/5/1910:31
 */
@Data
public class TEtFunctionInfo {

    private BigDecimal id;

    private BigDecimal parentId;

    private String etfnType;

    private String etfnName;

    private String etfnDesc;

    private Integer recordState;
}
