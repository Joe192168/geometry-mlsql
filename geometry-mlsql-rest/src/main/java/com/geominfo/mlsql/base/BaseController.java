package com.geominfo.mlsql.base;

import com.geominfo.mlsql.utils.RequestResponseUtil;
import org.springframework.web.bind.annotation.ModelAttribute;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
* @Description:    Controller基类
* @Author:         xqh
* @CreateDate:     2021/3/18 10:19
* @Version:        1.0
*/
public class BaseController {

    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected String appId;
    protected String loginName;

    @ModelAttribute
    public void setResAnReq(HttpServletRequest request,HttpServletResponse response) {
        this.request = request;
        this.response = response;
        Map<String ,String> map = RequestResponseUtil.getRequestBodyMap(request);
        this.appId = map.get("appId");
        this.loginName = map.get("loginName");
    }

}
