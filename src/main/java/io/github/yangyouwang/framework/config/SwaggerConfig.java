package io.github.yangyouwang.framework.config;

import io.github.yangyouwang.common.annotation.ApiVersion;
import io.github.yangyouwang.common.constant.ApiVersionConsts;
import io.swagger.annotations.ApiOperation;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author yangyouwang
 * @title: SwaggerConfig
 * @projectName crud
 * @description: swagger配置
 * @date 2021/3/207:13 PM
 */
@Configuration
@EnableSwagger2
@ConditionalOnProperty(name ="enabled" ,prefix = "swagger",havingValue = "true",matchIfMissing = true)
public class SwaggerConfig {

    /**
     * 创建API
     */
    @Bean
    public Docket createRestApi()
    {
        return new Docket(DocumentationType.SWAGGER_2)
                // 用来创建该API的基本信息，展示在文档的页面中（自定义展示的信息）
                .apiInfo(apiInfo())
                // 设置哪些接口暴露给Swagger展示
                .select()
                // 扫描
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(globalOperation())
                //  安全上下文
                .securityContexts(Arrays.asList(securityContexts()))
                .securitySchemes(unifiedAuth());
    }

    @Bean
    public Docket appV1(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName(ApiVersionConsts.SWAGGER_API_V1)
                .select()
                .apis(input -> {
                    ApiVersion apiVersion = input.getHandlerMethod().getMethodAnnotation(ApiVersion.class);
                    return apiVersion != null && Arrays.asList(apiVersion.group()).contains(ApiVersionConsts.SWAGGER_API_V1);
                })
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(globalOperation());
    }

    private List<Parameter> globalOperation(){
        List<Parameter> pars = new ArrayList<>();
        ParameterBuilder tokenPar = new ParameterBuilder();
        tokenPar.name("Authorization").description("授权token").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        pars.add(tokenPar.build());
        ParameterBuilder versionPar = new ParameterBuilder();
        versionPar.name("version").description("版本号").modelRef(new ModelRef("string")).parameterType("path").defaultValue("v1").required(true).build();
        pars.add(versionPar.build());
        return pars;
    }

    /***
     * 构建 api文档的详细信息函数
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("crud-api接口文档")
                .description("powered by wang")
                .termsOfServiceUrl("http://www.wbd.plus/")
                .contact(new Contact("杨有旺", "http://www.wbd.plus/", "616505453@qq.com"))
                .version("1.0")
                .build();
    }

    private static List<ApiKey> unifiedAuth() {
        List<ApiKey> arrayList = new ArrayList();
        arrayList.add(new ApiKey("Authorization", "Authorization", "header"));
        return arrayList;
    }

    private SecurityContext securityContexts() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.any())
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "描述信息");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("Authorization", authorizationScopes));
    }
}