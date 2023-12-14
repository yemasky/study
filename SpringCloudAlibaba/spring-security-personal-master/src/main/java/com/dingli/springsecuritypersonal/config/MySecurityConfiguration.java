package com.dingli.springsecuritypersonal.config;

import com.dingli.springsecuritypersonal.security.JWTProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MySecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JWTProvider jwtProvider;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth
//                .inMemoryAuthentication()
//                .withUser("admin") // 管理员，同事具有 ADMIN,USER权限，可以访问所有资源
//                .password("{noop}admin")  //
//                .roles("ADMIN", "USER")
//                .and()
//                .withUser("user")
//                .password("{noop}user") // 普通用户，只能访问 /product/**
//                .roles("USER")
//                .and()
//                .withUser("tmp")
//                .password("{noop}tmp")
//                .roles();
        auth.userDetailsService(userDetailsService)// 设置自定义的userDetailsService
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /*http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)//设置无状态
                .and()
                .authorizeRequests()
                .antMatchers("/product/**").hasRole("USER")
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/login").permitAll()
                .anyRequest().authenticated() //
                //.and()
                //.formLogin()
                //.loginProcessingUrl("/login")
                //.successHandler((request, response, authentication) -> {PrintWriter writer = response.getWriter(); writer.println(jwtProvider.createToken(authentication, true));})
                //.successForwardUrl("/product/hello")
                .and()
                .csrf().disable() // 取消csrf防护
                //.httpBasic()
                //.and()
                .logout().logoutUrl("/logout").logoutSuccessHandler((HttpServletRequest request, HttpServletResponse response, Authentication authentication) -> {
                        PrintWriter writer = response.getWriter();
                        writer.println("logout success");
                        writer.flush();
                    })
                .and().addFilterBefore(new JWTFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);*/
        http.formLogin()
                .loginPage("/index") // 配置哪个 url 为登录页面
                .loginProcessingUrl("/login") // 设置哪个是登录的 url。
                .successForwardUrl("/success") // 登录成功之后跳转到哪个 url
                .failureForwardUrl("/fail");// 登录失败之后跳转到哪个 url
        http.authorizeRequests()
                .antMatchers("/layui/**","/index") //表示配置请求路径
                .permitAll() // 指定 URL 无需保护。
                .anyRequest() // 其他请求
                .authenticated(); //需要认证
        // 关闭 csrf
        http.csrf().disable();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
//        return NoOpPasswordEncoder.getInstance();
        return new BCryptPasswordEncoder();
    }

}
