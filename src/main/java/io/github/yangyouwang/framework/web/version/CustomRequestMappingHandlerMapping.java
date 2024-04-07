package io.github.yangyouwang.framework.web.version;

import io.github.yangyouwang.common.annotation.ApiVersion;
import lombok.extern.java.Log;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author yangyouwang
 * @title: CustomRequestMappingHandlerMapping
 * @projectName crud
 * @description: 重写RequestMappingHandlerMapping方法
 * @date 2020/10/24下午11:49
 */
@Log
public class CustomRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

    public CustomRequestMappingHandlerMapping() {
        log.info("CustomRequestMappingHandlerMapping Init...");
    }

    @Override
    protected RequestCondition<ApiVersionCondition> getCustomTypeCondition(Class<?> handlerType) {
        // 扫描类上的 @ApiVersion
        ApiVersion apiVersion = AnnotationUtils.findAnnotation(handlerType, ApiVersion.class);
        return createRequestCondition(apiVersion);
    }

    @Override
    protected RequestCondition<ApiVersionCondition> getCustomMethodCondition(Method method) {
        // 扫描方法上的 @ApiVersion
        ApiVersion apiVersion = AnnotationUtils.findAnnotation(method, ApiVersion.class);
        return createRequestCondition(apiVersion);
    }

    private RequestCondition<ApiVersionCondition> createRequestCondition(ApiVersion apiVersion) {
        if (Objects.isNull(apiVersion)) {
            return null;
        }
        int value = apiVersion.value();
        Assert.isTrue(value >= 1, "Api Version Must be greater than or equal to 1");
        return new ApiVersionCondition(value);
    }
}