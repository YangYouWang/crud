package io.github.yangyouwang.framework.config;

import io.github.yangyouwang.framework.interceptor.ApiRestInteceptor;
import io.github.yangyouwang.framework.web.version.CustomRequestMappingHandlerMapping;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * @author yangyouwang
 * @title: WebMvcConfig
 * @projectName crud
 * @description: 静态资源文件映射
 * @date 2021/3/216:51 PM
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer, WebMvcRegistrations {

    @Resource
    private ApiRestInteceptor apiRestInteceptor;

    /**
     * 实现静态资源的映射
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html").addResourceLocations(
                "classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations(
                "classpath:/META-INF/resources/webjars/");
    }

    /**
     * 路径映射
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/sysTool/swagger","/swagger-ui.html");
        registry.addRedirectViewController("/sysTool/druid","/druid/index.html");
    }

    /**
     * 配置RestTemplate
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    /**
     * 自定义拦截规则
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        registry.addInterceptor(apiRestInteceptor).addPathPatterns("/api/**");
    }

    /**
     * 跨域配置
     */
    @Bean
    public CorsFilter corsFilter() {
        // 1.配置CORS信息
        CorsConfiguration config = new CorsConfiguration();
        // 哪些原始域需要放行
        config.addAllowedOriginPattern("*");
        // 是否发送Cookie信息
        config.setAllowCredentials(true);
        // 哪些原始域需要放行(请求方式)
        config.addAllowedMethod("*");
        // 放行哪些原始域(头部信息)
        config.addAllowedHeader("*");
        // 暴露哪些头部信息（因为跨域访问默认，所以不能获取到全部的头部信息）
        config.addExposedHeader(HttpHeaders.LOCATION);
        config.setExposedHeaders(Arrays.asList("JSESSIONID", "SESSION", "token",         HttpHeaders.LOCATION,
                HttpHeaders.ACCEPT, HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS,
                HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, HttpHeaders.COOKIE, HttpHeaders.SET_COOKIE,
                HttpHeaders.SET_COOKIE2));
        // 2.添加映射路径实例
        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        configSource.registerCorsConfiguration("/**", config);
        // 3.返回新的CorsFilter.
        return new CorsFilter(configSource);
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 将jackson序列化的处理调整到最前面
        converters.add(0, new MappingJackson2HttpMessageConverter());
        // 响应UTF-8编码
        converters.add(new StringHttpMessageConverter(StandardCharsets.UTF_8));
    }

    /**
     * API版本配置
     */
    @Override
    public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
        return new CustomRequestMappingHandlerMapping();
    }
}
