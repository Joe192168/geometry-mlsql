package com.geominfo.mlsql.domain.vo;

import lombok.Data;
import net.sf.json.JSONObject;
import java.io.Serializable;

/**
 * Job参数实体
 */
@Data
public class JobParameter implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String JOB_PARAM = "jobParam";

    private String contentInfo;

    EngineDetailsVO engineDetailsVO ;
}