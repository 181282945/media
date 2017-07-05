package com.aisino.configuration;

import com.aisino.core.security.MyConcurrentSessionControlAuthenticationStrategy;
import com.aisino.core.security.MyFilterSecurityInterceptor;
import com.aisino.core.security.MySessionRegistryImpl;
import com.aisino.core.security.MyUsernamePasswordAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AbstractAccessDecisionManager;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.ConcurrentSessionFilter;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.security.web.session.SimpleRedirectSessionInformationExpiredStrategy;
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
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private UserDetailsService userDetailsService;

    @Resource
    private FilterInvocationSecurityMetadataSource securityMetadataSource;

    @Resource
    private SessionRegistry sessionRegistry;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/components/**");
        web.ignoring().antMatchers("/css/**");
        web.ignoring().antMatchers("/images/**");
        web.ignoring().antMatchers("/img/**");
        web.ignoring().antMatchers("/js/**");
        web.ignoring().antMatchers("/favicon.ico");
        web.ignoring().antMatchers("/autoconfig");
        //二维码开票地址不拦截
        web.ignoring().antMatchers("/base/invoice/order/qrcodeorder/**");

        //注册地址不拦截
//        web.ignoring().antMatchers("/reg","/login");


    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //解决不允许显示在iframe的问题
        http.headers().frameOptions().disable();
        http.addFilterAt(myUsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterAt(new ConcurrentSessionFilter(sessionRegistry,sessionInformationExpiredStrategy()),ConcurrentSessionFilter.class);
        http.addFilterAfter(myFilterSecurityInterceptor(securityMetadataSource, accessDecisionManager(), authenticationManagerBean()), FilterSecurityInterceptor.class);


        // 开启默认登录页面
        http.exceptionHandling().authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login")).and().logout().logoutUrl("/logout").permitAll().logoutSuccessUrl("/index").permitAll().and().exceptionHandling().accessDeniedPage("/accessDenied");

        http.authorizeRequests().antMatchers("/base/**","/center/**").fullyAuthenticated();

        // 关闭csrf
        http.csrf().disable();

        /**
         * 以下配置无效
         */
//        //session管理
//        //session失效后跳转
//        http.sessionManagement().invalidSessionUrl("/index");
//        //只允许一个用户登录,如果同一个账户两次登录,那么第一个账户将被踢下线,跳转到登录页面
//        http.sessionManagement().maximumSessions(1).maxSessionsPreventsLogin(true).expiredUrl("/index");

    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        // 自定义UserDetailsService,设置加密算法
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());

        //不删除凭据，以便记住用户
        auth.eraseCredentials(false);
    }

    @Bean
    public MyUsernamePasswordAuthenticationFilter myUsernamePasswordAuthenticationFilter() throws Exception {
        MyUsernamePasswordAuthenticationFilter myUsernamePasswordAuthenticationFilter = new MyUsernamePasswordAuthenticationFilter();
        myUsernamePasswordAuthenticationFilter.setPostOnly(true);
        myUsernamePasswordAuthenticationFilter.setAuthenticationManager(this.authenticationManager());
        myUsernamePasswordAuthenticationFilter.setUsernameParameter("name_key");
        myUsernamePasswordAuthenticationFilter.setPasswordParameter("pwd_key");
        myUsernamePasswordAuthenticationFilter.setVerificationCodeParameter("verification_code");
        myUsernamePasswordAuthenticationFilter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/checkLogin", "POST"));
        myUsernamePasswordAuthenticationFilter.setAuthenticationFailureHandler(simpleUrlAuthenticationFailureHandler());
        myUsernamePasswordAuthenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler());
        myUsernamePasswordAuthenticationFilter.setSessionAuthenticationStrategy(new MyConcurrentSessionControlAuthenticationStrategy(sessionRegistry));
        return myUsernamePasswordAuthenticationFilter;
    }

//    private SessionAuthenticationStrategy sessionAuthenticationStrategy(){
//        ConcurrentSessionControlAuthenticationStrategy sessionAuthenticationStrategy = new ConcurrentSessionControlAuthenticationStrategy(sessionRegistry);
////        sessionAuthenticationStrategy.setMaximumSessions(1);//默认就是只允许同一个账户登陆一次
//        //设置为FALSE 为不抛异常,把另一个T下线
//        sessionAuthenticationStrategy.setExceptionIfMaximumExceeded(false);
//        return sessionAuthenticationStrategy;
//    }


//    @Bean
//    public ConcurrentSessionControlAuthenticationStrategy concurrentSessionControlAuthenticationStrategy(){
//        return new MyConcurrentSessionControlAuthenticationStrategy(sessionRegistry);
//    }


    private SessionInformationExpiredStrategy sessionInformationExpiredStrategy(){
        return new SimpleRedirectSessionInformationExpiredStrategy("/invalidSession");
    }

//    @Bean
//    public ConcurrentSessionFilter concurrentSessionFilter(){
//        return new ConcurrentSessionFilter(sessionRegistry,sessionInformationExpiredStrategy());
//    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new MySessionRegistryImpl();
    }

//    @Bean
//    public HttpSessionEventPublisher httpSessionEventPublisher() {
//        return new HttpSessionEventPublisher();
//    }

//    @Bean
//    public LoggerListener loggerListener() {
//        System.out.println("org.springframework.security.authentication.event.LoggerListener");
//        return new LoggerListener();
//    }
//
//    @Bean
//    public org.springframework.security.access.event.LoggerListener eventLoggerListener() {
//        System.out.println("org.springframework.security.access.event.LoggerListener");
//        return new org.springframework.security.access.event.LoggerListener();
//    }

    /**
     * 投票器
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	private AbstractAccessDecisionManager accessDecisionManager() {
        List<AccessDecisionVoter<? extends Object>> decisionVoters = new ArrayList();
        decisionVoters.add(new AuthenticatedVoter());
        decisionVoters.add(new RoleVoter());//角色投票器,默认前缀为ROLE_
        RoleVoter AuthVoter = new RoleVoter();
        AuthVoter.setRolePrefix("AUTH_");//特殊权限投票器,修改前缀为AUTH_
        decisionVoters.add(AuthVoter);

//        decisionVoters.add(webExpressionVoter());// 启用表达式投票器

        AbstractAccessDecisionManager accessDecisionManager = new AffirmativeBased(decisionVoters);

        return accessDecisionManager;
    }

    @Override
    public AuthenticationManager authenticationManagerBean() {
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
    private SimpleUrlAuthenticationFailureHandler simpleUrlAuthenticationFailureHandler() {
        return new SimpleUrlAuthenticationFailureHandler("/getLoginError");
    }


//    /**
//     * 表达式控制器
//     *
//     * @return
//     */
//    private DefaultWebSecurityExpressionHandler webSecurityExpressionHandler() {
//        DefaultWebSecurityExpressionHandler webSecurityExpressionHandler = new DefaultWebSecurityExpressionHandler();
//        return webSecurityExpressionHandler;
//    }
//
//    /**
//     * 表达式投票器
//     *
//     * @return
//     */
//    private WebExpressionVoter webExpressionVoter() {
//        WebExpressionVoter webExpressionVoter = new WebExpressionVoter();
//        webExpressionVoter.setExpressionHandler(webSecurityExpressionHandler());
//        return webExpressionVoter;
//    }

    // Code5  官方推荐加密算法
    @Bean("passwordEncoder")
    public BCryptPasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder;
    }

//    // Code3----------------------------------------------


    /**
     * 登录成功后跳转
     * 如果需要根据不同的角色做不同的跳转处理,那么继承AuthenticationSuccessHandler重写方法
     *
     * @return
     */
    private SimpleUrlAuthenticationSuccessHandler authenticationSuccessHandler() {
        return new SimpleUrlAuthenticationSuccessHandler("/loginSuccess");
    }

    /**
     * 自定义过滤器
     */
    private MyFilterSecurityInterceptor myFilterSecurityInterceptor(FilterInvocationSecurityMetadataSource securityMetadataSource, AccessDecisionManager accessDecisionManager, AuthenticationManager authenticationManager) {
        MyFilterSecurityInterceptor filterSecurityInterceptor = new MyFilterSecurityInterceptor();
        filterSecurityInterceptor.setSecurityMetadataSource(securityMetadataSource);
        filterSecurityInterceptor.setAccessDecisionManager(accessDecisionManager);
        filterSecurityInterceptor.setAuthenticationManager(authenticationManager);
        return filterSecurityInterceptor;
    }

}
