package com.geominfo.mlsql.services.feign;  /**
 * @title: MlsqlFeignService
 * @projectName geometry-mlsql
 * @description: TODO
 * @author Lenovo
 * @date 2021/4/613:52
 */

import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Auther zrd
 * @Date 2021-04-06 13:52 
 *
 */
@FeignClient(name = "mlsqlFeignService",url = "${my_url.url3}",fallbackFactory = MlsqlFeignService.MlsqlFeignServiceFallbackFactory.class)
public interface MlsqlFeignService {





    class MlsqlFeignServiceFallbackFactory{

    }
}
