package com.geominfo.mlsql.domain.vo;

import lombok.*;

import java.io.Serializable;

/**
 * @program: geometry-mlsql
 * @description: 脚本类
 * @author: BJZ
 * @create: 2020-06-04 14:44
 * @version: 3.0.0
 */
@Data
public class MlsqlScriptFile implements Serializable{

    private Integer id ;
    private String name;
    private Integer has_caret;
    private String icon;
    private String label;
    private Integer parent_id;
    private Integer is_dir;
    private String content;
    private Integer is_expanded;
}