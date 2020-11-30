package com.geominfo.mlsql.mapper;


import com.geominfo.mlsql.domain.vo.AppKv;
import com.geominfo.mlsql.domain.vo.MlsqlApply;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @program: geometry-mlsql
 * @description: 集群后端配置Mpper
 * @author: ryan(丁帅波)
 * @create: 2020-11-19 15:08
 * @version: 1.0.0
 */
@Mapper
@Component
public interface AppMapper {

    /**
     * description: AnalysisController
     * author: ryan
     * date: 2020/11/19
     * param:
     * return:
     */
    List<MlsqlApply> getMlsqlApplyList(Map<String,Object> map);

    /**
     * description: AppController
     * author: ryan
     * date: 2020/11/30
     * param:
     * return:
     */
    List<AppKv> appInfo();

    /**
     * description: AppController
     * author: ryan
     * date: 2020/11/30
     * param:
     * return:
     */
    int updateApp(@Param(value = "name") String name,
                  @Param(value = "value") String value);

    /**
     * description: AppController
     * author: ryan
     * date: 2020/11/30
     * param:
     * return:
     */
    int addApp(@Param(value = "name") String name,
               @Param(value = "value") String value);

}
