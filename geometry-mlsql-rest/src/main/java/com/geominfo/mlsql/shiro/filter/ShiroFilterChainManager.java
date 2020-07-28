package com.geominfo.mlsql.shiro.filter;

import com.geominfo.mlsql.domain.rule.RolePermRule;
import com.geominfo.mlsql.service.user.TeamRoleService;
import com.geominfo.mlsql.service.user.UserService;
import com.geominfo.mlsql.shiro.config.RestPathMatchingFilterChainResolver;
import com.geominfo.mlsql.shiro.provider.ShiroFilterRulesProvider;
import com.geominfo.mlsql.support.SpringContextHolder;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: geometry-bi
 * @description: Filter 管理器
 * @author: 肖乔辉
 * @create: 2019-05-23 19:02
 * @version: 1.0.0
 */
@Component
//@Order(Ordered.HIGHEST_PRECEDENCE)
public class ShiroFilterChainManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShiroFilterChainManager.class);

    private final ShiroFilterRulesProvider shiroFilterRulesProvider;
    private final StringRedisTemplate redisTemplate;
    private final UserService accountService;
    private final TeamRoleService queryPermissionService;

    @Autowired
    public ShiroFilterChainManager(ShiroFilterRulesProvider shiroFilterRulesProvider, StringRedisTemplate redisTemplate, UserService accountService, TeamRoleService queryPermissionService){
        this.shiroFilterRulesProvider = shiroFilterRulesProvider;
        this.redisTemplate = redisTemplate;
        this.accountService = accountService;
        this.queryPermissionService = queryPermissionService;
    }

    // 初始化获取过滤链
    public Map<String,Filter> initGetFilters() {
        Map<String,Filter> filters = new LinkedHashMap<>();
        PasswordFilter passwordFilter = new PasswordFilter();
        passwordFilter.setRedisTemplate(redisTemplate);
        filters.put("auth",passwordFilter);
        BJwtFilter jwtFilter = new BJwtFilter();
        jwtFilter.setRedisTemplate(redisTemplate);
        jwtFilter.setAccountService(accountService);
        jwtFilter.setQueryPermissionService(queryPermissionService);
        filters.put("jwt",jwtFilter);
        return filters;
    }
    // 初始化获取过滤链规则
    public Map<String,String> initGetFilterChain() {
        Map<String,String> filterChain = new LinkedHashMap<>();
        // -------------anon 默认过滤器忽略的URL
        List<String> defalutAnon = Arrays.asList(
                "/css/**",
                "/js/**",
                "/swagger-ui.html",
                "/swagger-resources/**",
                "/v2/api-docs",
                "/webjars/springfox-swagger-ui/**",
                "/api_v1/user/register"
        );
        defalutAnon.forEach(ignored -> filterChain.put(ignored,"anon"));
        // -------------auth 默认需要认证过滤器的URL PasswordFilter
//        List<String> defalutAuth = Arrays.asList("/api_v1/user/login");
//        defalutAuth.forEach(auth -> filterChain.put(auth,"auth"));
//        // -------------dynamic 动态URL 角色验证
//        if (shiroFilterRulesProvider != null) {
//            List<RolePermRule> rolePermRules = this.shiroFilterRulesProvider.loadRolePermRules();
//            if (null != rolePermRules) {
//                rolePermRules.forEach(rule -> {
//                    StringBuilder Chain = rule.toFilterChain();
//                    if (null != Chain) {
//                        filterChain.putIfAbsent(rule.getUrl(),Chain.toString());
//                    }
//                });
//            }
//        }
        return filterChain;
    }
    // 动态重新加载过滤链规则
    public void reloadFilterChain() {
            ShiroFilterFactoryBean shiroFilterFactoryBean = SpringContextHolder.getBean(ShiroFilterFactoryBean.class);
            AbstractShiroFilter abstractShiroFilter = null;
            try {
                abstractShiroFilter = (AbstractShiroFilter)shiroFilterFactoryBean.getObject();
                RestPathMatchingFilterChainResolver filterChainResolver = (RestPathMatchingFilterChainResolver)abstractShiroFilter.getFilterChainResolver();
                DefaultFilterChainManager filterChainManager = (DefaultFilterChainManager)filterChainResolver.getFilterChainManager();
                filterChainManager.getFilterChains().clear();
                shiroFilterFactoryBean.getFilterChainDefinitionMap().clear();
                shiroFilterFactoryBean.setFilterChainDefinitionMap(this.initGetFilterChain());
                shiroFilterFactoryBean.getFilterChainDefinitionMap().forEach((k,v) -> filterChainManager.createChain(k,v));
            }catch (Exception e) {
                LOGGER.error(e.getMessage(),e);
            }
    }
}
