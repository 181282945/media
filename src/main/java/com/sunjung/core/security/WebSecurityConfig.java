package com.sunjung.core.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.event.LoggerListener;
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
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by ZhenWeiLai on 2017/3/27.
 * <p>
 * 三种方法级权限控制
 * <p>
 * 1.securedEnabled: Spring Security’s native annotation
 * 2.jsr250Enabled: standards-based and allow simple role-based constraints
 * 3.prePostEnabled: expression-based
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private SecurityInterceptor securityInterceptor;

    @Resource
    private UserDetailsService userDetailsService;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/js/**");
        web.ignoring().antMatchers("/css/**");
        web.ignoring().antMatchers("/font/**");
        web.ignoring().antMatchers("/ace/**");
        web.ignoring().antMatchers("/favicon.ico");
//        web.ignoring().antMatchers("/test/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.addFilterBefore(securityInterceptor, FilterSecurityInterceptor.class)//在正确的位置添加我们自定义的过滤器
             .authorizeRequests()
                .and().exceptionHandling().authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/ace/html/login.html"))
                .and().logout().logoutSuccessUrl("/ace/html/index.html").permitAll().invalidateHttpSession(true)
                .and().rememberMe().key("webmvc#FD637E6D9C0F1A5A67082AF56CE32485").tokenValiditySeconds(1209600);

        http.authorizeRequests().antMatchers("/inquiry/**").hasAnyAuthority("BUYER");


        // 自定义accessDecisionManager访问控制器,并开启表达式语言
//        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler())
//                .and().authorizeRequests().anyRequest().authenticated().expressionHandler(webSecurityExpressionHandler());
        // 关闭csrf
        http.csrf().disable();

        // session管理,只允许一个用户登录,如果同一个账户两次登录,那么第一个账户将被踢下线,跳转到登录页面
        http.sessionManagement().maximumSessions(1).expiredUrl("/ace/html/login.html");
    }




    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        // 自定义UserDetailsService
        auth.userDetailsService(userDetailsService);
        //不删除凭据，以便记住用户
        auth.eraseCredentials(false);
    }

    @Bean
    UsernamePasswordAuthenticationFilter MyUsernamePasswordAuthenticationFilter() throws Exception {
        UsernamePasswordAuthenticationFilter myUsernamePasswordAuthenticationFilter = new UsernamePasswordAuthenticationFilter();
        myUsernamePasswordAuthenticationFilter.setPostOnly(true);
        myUsernamePasswordAuthenticationFilter.setAuthenticationManager(this.authenticationManager());
        myUsernamePasswordAuthenticationFilter.setUsernameParameter("name_key");
        myUsernamePasswordAuthenticationFilter.setPasswordParameter("pwd_key");
        myUsernamePasswordAuthenticationFilter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/login", "POST"));
        myUsernamePasswordAuthenticationFilter.setAuthenticationFailureHandler(simpleUrlAuthenticationFailureHandler());
        myUsernamePasswordAuthenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler());
        return myUsernamePasswordAuthenticationFilter;
    }

    @Bean
    AccessDeniedHandler accessDeniedHandler() {
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
     *
     * @return
     */
    @Bean(name = "accessDecisionManager")
    public MyAccessDecisionManager accessDecisionManager() {
        List<AccessDecisionVoter<? extends Object>> decisionVoters = new ArrayList();
        decisionVoters.add(new RoleVoter());
        decisionVoters.add(new AuthenticatedVoter());
        decisionVoters.add(webExpressionVoter());// 启用表达式投票器
        MyAccessDecisionManager accessDecisionManager = new MyAccessDecisionManager(decisionVoters);
        return accessDecisionManager;
    }

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
     *
     * @return
     */
//    @Bean(name = "failureHandler")
    public SimpleUrlAuthenticationFailureHandler simpleUrlAuthenticationFailureHandler() {
        return new SimpleUrlAuthenticationFailureHandler("/getLoginError");
    }


    /**
     * 表达式控制器
     *
     * @return
     */
//    @Bean(name = "expressionHandler")
    public DefaultWebSecurityExpressionHandler webSecurityExpressionHandler() {
        DefaultWebSecurityExpressionHandler webSecurityExpressionHandler = new DefaultWebSecurityExpressionHandler();
        return webSecurityExpressionHandler;
    }

    /**
     * 表达式投票器
     *
     * @return
     */
    @Bean(name = "expressionVoter")
    public WebExpressionVoter webExpressionVoter() {
        WebExpressionVoter webExpressionVoter = new WebExpressionVoter();
        webExpressionVoter.setExpressionHandler(webSecurityExpressionHandler());
        return webExpressionVoter;
    }

//    // Code5----------------------------------------------
//    @Bean
//    public BCryptPasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder(4);
//    }
//
//    // Code3----------------------------------------------


    /**
     * 登录成功后跳转
     * 如果需要根据不同的角色做不同的跳转处理,那么继承AuthenticationSuccessHandler重写方法
     * @return
     */
    public SimpleUrlAuthenticationSuccessHandler authenticationSuccessHandler(){
        return new SimpleUrlAuthenticationSuccessHandler("/loginSuccess");
    }

}
