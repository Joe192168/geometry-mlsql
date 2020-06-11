package com.geominfo.mlsql.domain.vo;

import lombok.Data;

/**
 * @program: MLSQL CONSOLE后端接口
 * @description: MLSQLJobProgress
 * @author: anan
 * @create: 2020-06-11 17:51
 * @version: 1.0.0
 */
@Data
public class MLSQLJobProgress {
    private long totalJob = 0;
    private long currentJobIndex = 0;
    private String script = "";
}
