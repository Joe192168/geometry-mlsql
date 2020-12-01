package com.geominfo.mlsql.domain.vo;

import lombok.Data;


/**
 * @program: geometry-mlsql
 * @description: FullPathAndScriptFile
 * @author: BJZ
 * @create: 2020-11-23 17:31
 * @version: 1.0.0
 */
@Data
public class FullPathAndScriptFile {
    private String path ;
    private MlsqlScriptFile scriptFile;

    public FullPathAndScriptFile(String path, MlsqlScriptFile scriptFile) {
        this.path = path;
        this.scriptFile = scriptFile;
    }
}