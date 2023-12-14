package demo2.config;

import demo2.jwt.JWTAuthenticationEntryPoint;
import demo2.jwt.JWTAuthenticationFilter;
import demo2.jwt.JWTAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	@Qualifier("userDetailsServiceImpl")
	private UserDetailsService userDetailsService;

	@Autowired
	private StringRedisTemplate redisTemplate;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		/*
		 * http.authorizeRequests() .antMatchers("/layui/**","/index") //表示配置请求路径
		 * .permitAll() // 指定 URL 无需保护。 .anyRequest() // 其他请求 .authenticated(); //需要认证
		 * 
		 * http.formLogin() // 表单登录 .and() .authorizeRequests() // 认证配置 .anyRequest() //
		 * 任何请求 .authenticated(); // 都需要身份验证

		// 配置认证
		http.formLogin().loginPage("/index") // 配置哪个 url 为登录页面
				.loginProcessingUrl("/login") // 设置哪个是登录的 url。
				.successForwardUrl("/success") // 登录成功之后跳转到哪个 url
				.failureForwardUrl("/fail");// 登录失败之后跳转到哪个 url
		http.authorizeRequests().antMatchers("/layui/**", "/index") // 表示配置请求路径
				.permitAll() // 指定 URL 无需保护。
				.anyRequest() // 其他请求
				.authenticated(); // 需要认证
		// 关闭 csrf
		http.csrf().disable();*/
		/*http
				.csrf().disable()
				.authorizeRequests()
				.antMatchers("/login").permitAll()
				.anyRequest().authenticated()
				.and()
				.addFilterBefore(new JWTAuthenticationFilter(authenticationManager(), redisTemplate), UsernamePasswordAuthenticationFilter.class)
				.addFilterBefore(new JWTAuthorizationFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class)
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.exceptionHandling().authenticationEntryPoint(new JWTAuthenticationEntryPoint());*/
		http.formLogin()
				.loginPage("/index") // 配置哪个 url 为登录页面
				.loginProcessingUrl("/login") // 设置哪个是登录的 url。
				.successForwardUrl("/success") // 登录成功之后跳转到哪个 url
				.failureForwardUrl("/fail")// 登录失败之后跳转到哪个 url
				.and()
				.addFilterBefore(new JWTAuthenticationFilter(authenticationManager(), redisTemplate), UsernamePasswordAuthenticationFilter.class)
				.addFilterBefore(new JWTAuthorizationFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class)
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.exceptionHandling().authenticationEntryPoint(new JWTAuthenticationEntryPoint());
		http.authorizeRequests()
				.antMatchers("/layui/**","/index") //表示配置请求路径
				.permitAll() // 指定 URL 无需保护。
				.anyRequest() // 其他请求
				.authenticated(); //需要认证
		// 关闭 csrf
		http.csrf().disable();

	}

}
