package com.geominfo.mlsql.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * @program: MLSQL CONSOLE后端接口
 * @description: TableTypeMeta
 * @author: anan
 * @create: 2020-06-10 11:24
 * @version: 1.0.0
 */
@Data
public class TableTypeMeta implements Serializable {
    private String name;
    private Set<String> includes;
    public TableTypeMeta(String name, Set<String> includes){
        this.name = name;
        this.includes = includes;
    }
}
