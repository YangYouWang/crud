package io.github.yangyouwang.core.config;
import io.github.yangyouwang.core.security.filter.ValidateCodeFilter;
import io.github.yangyouwang.core.security.handler.DefaultAuthenticationFailureHandler;
import io.github.yangyouwang.core.security.handler.DefaultAuthenticationSuccessHandler;
import io.github.yangyouwang.module.system.service.SysUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * @author yangyouwang
 * @title: SecurityConfig
 * @projectName crud
 * @description: Security的配置
 * @date 2021/3/208:47 PM
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private SysUserService sysUserService;

    @Resource
    private DefaultAuthenticationSuccessHandler defaultAuthenticationSuccessHandler;

    @Resource
    private DefaultAuthenticationFailureHandler defaultAuthenticationFailureHandler;

    @Resource
    private ValidateCodeFilter validateCodeFilter;

    @Resource
    private DataSource dataSource;

    /**
     * 密码加密
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * 自定义认证数据源
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
                auth.userDetailsService(sysUserService)
                // 使用BCrypt加密密码
                .passwordEncoder(passwordEncoder());
    }

    /**
     * 配置放行的资源
     **/
    @Override
    public void configure(WebSecurity web) {
        // 不需要权限能访问的资源
        web.ignoring()
                // 接口放行
                .antMatchers("/api/**")
                // 静态资源
                .antMatchers("/static/**");
    }

    /**
     * 权限配置
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 设置哪些页面可以直接访问，哪些需要验证
        http.authorizeRequests()
                // 放过
                .antMatchers("/loginPage", "/getImgCode").permitAll()
                // 剩下的所有的地址都是需要在认证状态下才可以访问
                .anyRequest().authenticated()
        .and()
                // 过滤登录验证码
                .addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
        // 配置登录功能
        .formLogin().
                usernameParameter("userName")
                .passwordParameter("passWord")
                // 指定指定要的登录页面
                .loginPage("/loginPage")
                // 处理认证路径的请求
                .loginProcessingUrl("/login")
                .successHandler(defaultAuthenticationSuccessHandler)
                .failureHandler(defaultAuthenticationFailureHandler)
                .and()
                // 登出
                .logout()
                .invalidateHttpSession(true)
                .deleteCookies("remember-me")
                .logoutUrl("/logout")
                .logoutSuccessUrl("/loginPage")
                 .and()
                .rememberMe()
                // 有效期7天
                .tokenValiditySeconds(3600 * 24 * 7)
                // 开启记住我功能
                .rememberMeParameter("remember-me")
                .key("salt")
                .tokenRepository(jdbcTokenRepository())
                .and()
                //禁用csrf
                .csrf().disable()
                // header response的X-Frame-Options属性设置为SAMEORIGIN
                .headers().frameOptions().sameOrigin()
                .and()
                // 配置session管理
                .sessionManagement()
                //session失效默认的跳转地址
                .invalidSessionUrl("/loginPage");
    }

    @Bean
    public JdbcTokenRepositoryImpl jdbcTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }
}
