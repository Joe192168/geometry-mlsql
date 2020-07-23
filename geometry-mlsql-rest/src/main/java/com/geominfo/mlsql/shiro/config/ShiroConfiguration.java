package com.geominfo.mlsql.shiro.config;

import com.geominfo.mlsql.shiro.filter.ShiroFilterChainManager;
import com.geominfo.mlsql.shiro.filter.StatelessWebSubjectFactory;
import com.geominfo.mlsql.shiro.realm.AModularRealmAuthenticator;
import com.geominfo.mlsql.shiro.realm.RealmManager;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: geometry-bi
 * @description: shiro配置
 * @author: 肖乔辉
 * @create: 2019-05-23 19:02
 * @version: 1.0.0
 */
@Configuration
public class ShiroConfiguration {

//    @Value("${permission.Server-url}")
//    private String permissionServerUrl;
//    @Value("${permission.dev-url}")
//    private String devPermissionServerUrl;

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager,ShiroFilterChainManager filterChainManager) {
        RestShiroFilterFactoryBean shiroFilterFactoryBean = new RestShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setFilters(filterChainManager.initGetFilters());
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainManager.initGetFilterChain());
        return shiroFilterFactoryBean;
    }

    @Bean
    public SecurityManager securityManager(RealmManager realmManager) {
//        PermissionServerUrlBean permissionServerUrlBean = new PermissionServerUrlBean();
//        permissionServerUrlBean.setDevPermissionServer(devPermissionServerUrl);
//        permissionServerUrlBean.setPermissionServer(permissionServerUrl);

        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setAuthenticator(new AModularRealmAuthenticator());
        securityManager.setRealms(realmManager.initGetRealm());

        // 无状态subjectFactory设置
        DefaultSessionStorageEvaluator evaluator = (DefaultSessionStorageEvaluator)((DefaultSubjectDAO) securityManager.getSubjectDAO()).getSessionStorageEvaluator();
        evaluator.setSessionStorageEnabled(Boolean.FALSE);
        StatelessWebSubjectFactory subjectFactory = new StatelessWebSubjectFactory();
        securityManager.setSubjectFactory(subjectFactory);

        SecurityUtils.setSecurityManager(securityManager);
        return securityManager;
    }

}
