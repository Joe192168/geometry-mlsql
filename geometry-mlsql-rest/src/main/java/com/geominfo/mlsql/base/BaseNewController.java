package com.geominfo.mlsql.base;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @program: geometry-bi
 * @description:
 * @author: LF
 * @create: 2021/5/20 15:38
 * @version: 1.0.0
 */
public abstract class BaseNewController {

    protected static final Logger logger = LoggerFactory.getLogger(BaseNewController.class);

    protected Integer getUserId(){
        //获取当前用户的id
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String parseInt = authentication.getName().substring(authentication.getName().indexOf("$") + 1);
        if (StringUtils.isBlank(parseInt) || parseInt.equals("anonymousUser")) {
            //判断是否为第三方api，api调用默认为0
            return 0;
        } else  {
            return Integer.parseInt(parseInt);
        }
    }
}
