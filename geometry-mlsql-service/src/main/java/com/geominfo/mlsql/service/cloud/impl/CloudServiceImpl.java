package com.geominfo.mlsql.service.cloud.impl;

import com.geominfo.mlsql.service.cloud.CloudService;
import com.geominfo.mlsql.utils.NetWorkProxy;
import com.geominfo.mlsql.utils.NetWorkUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;

/**
 * @program: MLSQL CONSOLE后端接口
 * @description: CloudServiceImpl
 * @author: anan
 * @create: 2020-11-25 14:30
 * @version: 1.0.0
 */
@Service
@Log4j2
public class CloudServiceImpl implements CloudService {
    private NetWorkUtil netWorkUtil = NetWorkProxy.getNetWorkProxy() ;
    @Override
    public ResponseEntity<String> execReqUrlAndResponse(LinkedMultiValueMap<String, String> params) {
        return netWorkUtil.synPost(params.get("url").get(0), params) ;
    }
}
