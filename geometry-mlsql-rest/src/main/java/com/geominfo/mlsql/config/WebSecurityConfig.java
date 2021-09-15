package com.geominfo.mlsql.config;

import com.geominfo.authing.common.utils.IdWorker;
import com.geominfo.mlsql.encoder.LoginPasswordEncoder;
import com.geominfo.mlsql.filter.JWTAuthenticationFilter;
import com.geominfo.mlsql.filter.JWTAuthorizationFilter;
import com.geominfo.mlsql.filter.TokenAuthenticationFilter;
import com.geominfo.mlsql.filter.URLFilterSecurityInterceptor;
import com.geominfo.mlsql.handler.JWTAccessDeniedHandler;
import com.geominfo.mlsql.handler.JWTAuthenticationEntryPoint;
import com.geominfo.mlsql.services.AuthQueryApiService;
import com.geominfo.mlsql.services.SystemPermissionService;
import com.geominfo.mlsql.services.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
* @Description:    安全配置
* @Author:         xqh
* @CreateDate:     2021/3/17 16:59
* @Version:        1.0
*/
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private URLFilterSecurityInterceptor urlFilterSecurityInterceptor;

    @Autowired
    private JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private JWTAccessDeniedHandler jwtAccessDeniedHandler;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private SystemPermissionService systemPermissionService;

    @Autowired
    private FilterIgnoreConfig filterIgnoreConfig;

    @Autowired
    private AuthQueryApiService authQueryApiService;

    /**
     * 密码编码器
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        //return NoOpPasswordEncoder.getInstance(); 明文
        return new LoginPasswordEncoder();
    }

    /**
     * 认证
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    /**
     * 授权（安全拦截机制）
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().authorizeRequests()
                // 不进行权限验证的请求或资源(从配置文件中读取)
                .antMatchers(filterIgnoreConfig.getUrls().toString().split(",")).permitAll()
                // 其他的需要登陆后才能访问  其他url都需要验证
                .anyRequest().authenticated()
                .and()
                .addFilter(new JWTAuthenticationFilter(idWorker,redisTemplate,authenticationManager(),systemPermissionService,authQueryApiService))//认证
                .addFilter(new JWTAuthorizationFilter(authenticationManager()))//授权
                //不需要session
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .httpBasic()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)//配置未登录自定义处理类
                .and()
                .exceptionHandling().accessDeniedHandler(jwtAccessDeniedHandler);//添加无权限时的处理

        // 拦截受保护的url
        http.addFilterBefore(urlFilterSecurityInterceptor, FilterSecurityInterceptor.class);
        // 配置jwt验证过滤器，位于用户名密码验证过滤器之后
        http.addFilterAfter(new TokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

}