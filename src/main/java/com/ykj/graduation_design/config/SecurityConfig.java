package com.ykj.graduation_design.config;

import com.ykj.graduation_design.Filter.JwtFilter;
import com.ykj.graduation_design.Filter.RequestReplaceFilter;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author Ayelsh.ye
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Resource
    JwtFilter jwtFilter;

    @Resource
    RequestReplaceFilter requestReplaceFilter;

    /**
     * 查询默认使用loadUserByUsername
     *
     * @return
     */
    @Bean
    public UserDetailsService userDetailsService() {
        // 调用 JwtUserDetailService实例执行实际校验
        return username -> userDetailsService.loadUserByUsername(username);
    }

    /**
     * 注册密码加密Bean
     *
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.formLogin(AbstractHttpConfigurer::disable)
                //取消默认登出页面的使用
                .logout(AbstractHttpConfigurer::disable)
                //将自己配置的PasswordEncoder放入SecurityFilterChain中
                .authenticationProvider(authenticationProvider())
                //禁用csrf保护，前后端分离不需要
                .csrf(AbstractHttpConfigurer::disable)
                //禁用session，因为已经使用了JWT
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                //禁用httpBasic，因为我们传输数据用的是post，而且请求体是JSON
                .httpBasic(AbstractHttpConfigurer::disable)
                //开放两个接口的post请求，一个注册，一个登录，其余anyRequest均要身份认证authenticated
                .authorizeHttpRequests(request ->
                        request
                                .requestMatchers("/user/login", "/user/register","/druid/**","/user/code","/Article/newArticleContent").permitAll()
                                .anyRequest()
                                .authenticated())
                //将用户授权时用到的JWT校验过滤器添加进SecurityFilterChain中，并放在UsernamePasswordAuthenticationFilter的前面
                .addFilterBefore(requestReplaceFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtFilter, RequestReplaceFilter.class);
//                .oauth2Login();
        return httpSecurity.build();
    }

    @Bean
    @ConditionalOnMissingBean(AuthenticationEventPublisher.class)
    DefaultAuthenticationEventPublisher defaultAuthenticationEventPublisher(ApplicationEventPublisher delegate) {
        return new DefaultAuthenticationEventPublisher(delegate);
    }

    /**
     * @return
     * 验证提供者,要设定号加密方式和验证方式
     *
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    /**
     * 注入管理器
     *
     * @param configuration
     * @return
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

}
