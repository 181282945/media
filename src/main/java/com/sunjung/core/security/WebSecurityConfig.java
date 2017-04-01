package com.sunjung.core.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.event.LoggerListener;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.annotation.Resource;

/**
 * Created by ZhenWeiLai on 2017/3/27.
 *
 *  三种方法级权限控制
 *
 * 1.securedEnabled: Spring Security’s native annotation
 * 2.jsr250Enabled: standards-based and allow simple role-based constraints
 * 3.prePostEnabled: expression-based
 */
@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Resource
    private UserDetailsService userDetailsService;
//
//    @Resource
//    private MySecurityMetadataSource mySecurityMetadataSource;


    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/js/**");
        web.ignoring().antMatchers("/user/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterAfter(MyUsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        // 自定义accessDecisionManager访问控制器,并开启表达式语言
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler())
                .and().authorizeRequests().anyRequest().authenticated().expressionHandler(webSecurityExpressionHandler());

        // 配置其所有页面必须经过验证
        http.authorizeRequests().anyRequest().authenticated().and().exceptionHandling();

        //配置登录页面,退出后页面,不需要验证
        http.formLogin().loginPage("/login.html").permitAll().and().logout().logoutSuccessUrl("/index.html").permitAll();

        // 自定义登录页面
        http.csrf().disable();

        // 自定义注销
        //        http.logout().logoutUrl("/logout").logoutSuccessUrl("/login")
        //                .invalidateHttpSession(true);

        // session管理
        http.sessionManagement().maximumSessions(1);

        // RemeberMe
        //        http.rememberMe().key("webmvc#FD637E6D9C0F1A5A67082AF56CE32485");

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        // 自定义UserDetailsService
        auth.userDetailsService(userDetailsService);
    }

    @Bean
    UsernamePasswordAuthenticationFilter MyUsernamePasswordAuthenticationFilter(){
        UsernamePasswordAuthenticationFilter myUsernamePasswordAuthenticationFilter = new UsernamePasswordAuthenticationFilter();
        myUsernamePasswordAuthenticationFilter.setPostOnly(true);
        myUsernamePasswordAuthenticationFilter.setAuthenticationManager(authenticationManagerBean());
        myUsernamePasswordAuthenticationFilter.setUsernameParameter("name_key");
        myUsernamePasswordAuthenticationFilter.setPasswordParameter("pwd_key");
        myUsernamePasswordAuthenticationFilter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/login", "POST"));
        myUsernamePasswordAuthenticationFilter.setAuthenticationFailureHandler(simpleUrlAuthenticationFailureHandler());
        return myUsernamePasswordAuthenticationFilter;
    }

    @Bean
    AccessDeniedHandler accessDeniedHandler(){
        AccessDeniedHandlerImpl accessDeniedHandler = new AccessDeniedHandlerImpl();
        accessDeniedHandler.setErrorPage("/securityException/accessDenied");
        return accessDeniedHandler;
    }

    @Bean
    public LoggerListener loggerListener() {
        System.out.println("org.springframework.security.authentication.event.LoggerListener");
        return new LoggerListener();
    }

    @Bean
    public org.springframework.security.access.event.LoggerListener eventLoggerListener() {
        System.out.println("org.springframework.security.access.event.LoggerListener");
        return new org.springframework.security.access.event.LoggerListener();
    }

    /**
     * 这里可以增加自定义的投票器
     * @return
     */
//    @Bean(name = "accessDecisionManager")
//    public AccessDecisionManager accessDecisionManager() {
//        List<AccessDecisionVoter<? extends Object>> decisionVoters = new ArrayList();
//        decisionVoters.add(new RoleVoter());
//        decisionVoters.add(new AuthenticatedVoter());
//        decisionVoters.add(webExpressionVoter());// 启用表达式投票器
//        MyAccessDecisionManager accessDecisionManager =  new MyAccessDecisionManager(decisionVoters);
//        return accessDecisionManager;
//    }

    @Bean(name = "authenticationManager")
    @Override
    public AuthenticationManager authenticationManagerBean(){
        AuthenticationManager authenticationManager = null;
        try {
            authenticationManager = super.authenticationManagerBean();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return authenticationManager;
    }


    /**
     * 验证异常处理器
     * @return
     */
    @Bean(name = "failureHandler")
    public SimpleUrlAuthenticationFailureHandler simpleUrlAuthenticationFailureHandler(){
        return  new SimpleUrlAuthenticationFailureHandler("/getLoginError");
    }

//    @Bean(name = "aclResourcesService")
//    @ConditionalOnMissingBean
//    public AclResourcesService aclResourcesService(){
//        return new AclResourcesServiceImpl();
//    }

    /**
     * 表达式控制器
     * @return
     */
    @Bean(name = "expressionHandler")
    public DefaultWebSecurityExpressionHandler webSecurityExpressionHandler() {
        DefaultWebSecurityExpressionHandler webSecurityExpressionHandler = new DefaultWebSecurityExpressionHandler();
        return webSecurityExpressionHandler;
    }

    /**
     * 表达式投票器
     * @return
     */
    @Bean(name = "expressionVoter")
    public WebExpressionVoter webExpressionVoter() {
        WebExpressionVoter webExpressionVoter = new WebExpressionVoter();
        webExpressionVoter.setExpressionHandler(webSecurityExpressionHandler());
        return webExpressionVoter;
    }
}
