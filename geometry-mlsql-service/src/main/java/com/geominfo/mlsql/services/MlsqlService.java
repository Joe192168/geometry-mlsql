package com.geominfo.mlsql.services;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.geominfo.mlsql.domain.po.TEtFunctionInfo;
import com.geominfo.mlsql.domain.po.TScriptExecLog;
import com.geominfo.mlsql.domain.po.TScriptExecMetricLog;
import com.geominfo.mlsql.domain.po.TSystemResources;
import com.geominfo.mlsql.domain.vo.MlsqlExecuteSqlVO;
import com.geominfo.mlsql.utils.TreeVo;
import org.bouncycastle.asn1.cmc.BodyPartID;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Auther zrd
 * @Date 2021-04-06 10:13 
 *
 */
public interface MlsqlService {

    /***
     * @Description: 执行mlsql脚本方法
     * @Author: zrd
     * @Date: 9:40
     * @Param [mlsqlExecuteSqlVO]
     * @return void
     */
    String executeMlsql(MlsqlExecuteSqlVO mlsqlExecuteSqlVO);
    
    /***
     * @Description: 获取正在执行MLSQL 作业
     * @Author: zrd 
     * @Date: 9:42
     * @Param []
     * @return java.lang.String
     */
    JSONObject getAllExecuteJobs();

    /***
     * @Description: 杀掉正在执行的job任务
     * @Author: zrd
     * @Date: 9:42
     * @Param []
     * @return java.lang.String
     */
    String killMlsqlJob(String jobName,String groupId);

    /***
     * @Description: 获取engine状态
     * @Author: zrd
     * @Date: 11:39
     * @Param []
     * @return com.alibaba.fastjson.JSONObject
     */
    JSONObject getEngineState(String engineUrl);

    /***
     * @Description: 处理异步回调接口入库
     * @Author: zrd
     * @Date: 2021/5/8 13:55
     * @param map 异步回调返回值
     * @return void
     */
    void dealAsyncCallback(Map<String,String> map);

    /***
     * @Description: 根据父节点ID获取该父节点下所有资源
     * @Author: zrd
     * @Date: 2021/5/12 16:40
     * @param
     * @return java.util.List<com.geominfo.mlsql.utils.TreeVo<com.geominfo.mlsql.domain.po.TSystemResources>>
     */
    List<TreeVo<TSystemResources>> listTreeByParentId(BigDecimal resourceTypeId,BigDecimal parentId);

    /***
     * @Description: 根据脚本路径获取脚本内容
     * @Author: zrd
     * @Date: 2021/5/12 17:03
     * @param scriptRoute 脚本路径
     * @return com.geominfo.mlsql.domain.po.TSystemResources
     */
    TSystemResources getScriptByRoute(String scriptRoute);

    /***
     * @Description: 根据id获取脚本内容
     * @Author: zrd
     * @Date: 2021/5/12 17:19
     * @param id
     * @return com.geominfo.mlsql.domain.po.TSystemResources
     */
    TSystemResources getScriptById(BigDecimal id);

    /***
     * @Description: 根据job名获取job日志
     * @Author: zrd
     * @Date: 2021/5/13 15:43
     * @param jobName
     * @return void
     */
    Map<String,Object> getJobLogByJobName(String jobName);

    /***
     * @Description: 检查任务是否完成
     * @Author: zrd
     * @Date: 2021/5/17 14:23
     * @param jobName
     * @return void
     */
    JSONObject checkJobIsFinish(String jobName);

    /***
     * @Description: 语法检查
     * @Author: zrd
     * @Date: 2021/5/18 9:56
     * @param sql
     * @param owner
     * @param jobType
     * @param executeMode
     * @param jobName
     * @param sessionPerUser
     * @param skipInclude
     * @param skipGrammarValidate
     * @param contextDefaultIncludeFetchUrl
     * @return com.alibaba.fastjson.JSONObject
     */
    JSONArray grammarCheck(String sql, String owner, String jobType, String executeMode, String jobName, Boolean sessionPerUser, Boolean skipInclude, Boolean skipGrammarValidate, String contextDefaultIncludeFetchUrl) throws Exception;

    String getScriptExecuteProgress(String sql, String owner, String jobType, String executeMode, String jobName, Boolean sessionPerUser);

    /***
     * @Description: 根据类型树状返回资源
     * @Author: zrd
     * @Date: 2021/5/20 17:26
     * @param resourceType 资源类型
     * @return java.util.List<com.geominfo.mlsql.utils.TreeVo<com.geominfo.mlsql.domain.po.TEtFunctionInfo>>
     */
    List<TreeVo<TEtFunctionInfo>> listFunctionTreeByParentId(BigDecimal resourceType);

    TScriptExecLog getAsyncLogResult(String jobId);

    TScriptExecMetricLog getRuntimeDetails(String jobId);

    Boolean deleteJobHistory(String jobId);

    /**
     * @Description: 保存脚本接口
     * @Author: zrd
     * @Date: 2021/6/1 14:00
     * @param path 脚本路径
     * @param owner 脚本拥有人
     * @return void
     */
    boolean saveScript(BigDecimal parentId, BigDecimal owner,String content,String scriptName,String path);
}
