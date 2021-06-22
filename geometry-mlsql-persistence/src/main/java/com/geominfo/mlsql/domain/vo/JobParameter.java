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
    
    private String conditionId;
    private String resourceId;
    private String contentInfo;

    private String id;
    private String recipient;
    private String recipientIds;

    private String levelType;
    private String relativeDate;
    private String uniqueName;
    private String symbolType;
    private int changeNum;
    private JSONObject jsonObject;

    //登陆账号
    private String loginName;

    //用户id
    private Integer userId;

    //标题
    private String title;
    //监控名称
    private String conditionTitle;
    //监控值
    private String trueValue;
    //条件
    private String condition;
    //规定阈值
    private String fixedThresholdDisplay;

}