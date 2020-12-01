package com.geominfo.mlsql.controller.base;

import com.geominfo.mlsql.domain.vo.JwtAccount;
import com.geominfo.mlsql.domain.vo.Message;
import com.geominfo.mlsql.util.JsonWebTokenUtil;
import com.geominfo.mlsql.util.RequestResponseUtil;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @program: MLSQL CONSOLE后端接口
 * @description: BaseController
 * @author: anan
 * @create: 2020-07-22 19:20
 * @version: 1.0.0
 */
public abstract class BaseController {

    protected String userName ="admin";
    protected Map<String, String> requestParams;
    protected Map<String, String> headers;
    protected String tokenId;

    /**
      * description: get message from request
      * author: anan
      * date: 2020/7/23
      * param:request
      * return:
     */
    @ModelAttribute
    public void initData(HttpServletRequest request){
        this.requestParams = getRequestParameter(request);
        this.headers = RequestResponseUtil.getRequestHeaders(request);
        if(this.headers.containsKey("authorization")){
            String jwt = WebUtils.toHttp(request).getHeader("authorization");
            JwtAccount jwtAccount = JsonWebTokenUtil.parseJwt(jwt);
            this.userName = jwtAccount.getAppId();
            this.tokenId = jwtAccount.getTokenId();
        }
    }
    /**
      * description: 
      * author: anan
      * date: 2020/7/23
      * param: 
      * return: 
     */
    
    public Message success(int statusCode,String statusMsg){
        Message message = new Message();
        message.ok(statusCode, statusMsg);
        return message;
    }
    /**
      * description: 
      * author: anan
      * date: 2020/7/23
      * param:
      * return: 
     */
    
    public Message error(int statusCode,String statusMsg){
        Message message = new Message();
        message.error(statusCode, statusMsg);
        return message;
    }
    /* *
     * @Description获得来的request中的 key value数据封装到map返回
     * @Param [request]
     * @Return java.util.Map<java.lang.String,java.lang.String>
     */
    @SuppressWarnings("rawtypes")
    protected Map<String, String> getRequestParameter(HttpServletRequest request) {

        return RequestResponseUtil.getRequestParameters(request);
    }

    protected Map<String,String> getRequestBody(HttpServletRequest request) {
        return RequestResponseUtil.getRequestBodyMap(request);
    }

    protected Map<String, String > getRequestHeader(HttpServletRequest request) {
        return RequestResponseUtil.getRequestHeaders(request);
    }

}
