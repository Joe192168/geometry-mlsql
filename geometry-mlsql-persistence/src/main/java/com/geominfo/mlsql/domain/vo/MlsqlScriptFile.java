package com.geominfo.mlsql.domain.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.io.Serializable;
import java.util.List;

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
    private Integer hasCaret;
    private String icon;
    private String label;
    private Integer parentId;
    private Integer isDir;
    private String content;
    private Integer isExpanded=1;
    @JsonIgnore
    private List<ScriptUserRw> scriptUserRws;
    public static int DIR = 1;
    public static int FILE = 2;
}