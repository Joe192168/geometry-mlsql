package com.geominfo.mlsql.mapper;

import com.geominfo.mlsql.domain.vo.MlsqlJob;
import com.geominfo.mlsql.domain.vo.MlsqlJobRender;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @program: geometry-mlsql
 * @description: 集群后端配置Mpper
 * @author: ryan
 * @create: 2020-11-09 18:00
 * @version: 1.0.0
 */
@Mapper
@Component
public interface JobMapper {

    /**
     * description: 获取历史任务信息
     * author: ryan
     * date: 2020/11/09
     * param: 用户id
     * return: list集合
     */
    List<MlsqlJobRender> getMlsqlJobList(Integer userId);


    /**
     * description: 获取单个任务信息
     * author: ryan
     * date: 2020/11/10
     * param: userId,jobName
     * return: MlsqlJob
     */
    //MlsqlJob getMlsqlJob(@Param("userId") Integer userId, @Param("jobName") String jobName);
    MlsqlJob getMlsqlJob(Map<String,Object> map);


    /**
     * description: 修改任务信息
     * author: ryan
     * date: 2020/11/12
     * param: map
     * return: int
     */
    int updateMlsqlJob(Map<String, Object> map);



}
