package com.geominfo.mlsql.service.cloud;

import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;

/**
 * @program: MLSQL CONSOLE后端接口
 * @description: CloudService
 * @author: anan
 * @create: 2020-11-25 14:29
 * @version: 1.0.0
 */
public interface CloudService {
    ResponseEntity<String> execReqUrlAndResponse(LinkedMultiValueMap<String, String> params);
}
