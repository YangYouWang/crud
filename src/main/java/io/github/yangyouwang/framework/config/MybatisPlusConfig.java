package io.github.yangyouwang.framework.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.extension.injector.methods.InsertBatchSomeColumn;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import io.github.yangyouwang.framework.util.SecurityUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;
import java.util.List;

/**
 * @author yangyouwang
 * @title: MybatisPlusConfig
 * @projectName crud
 * @description: MybatisPlus 配置
 * @date 2021/4/12:32 PM
 */
@Configuration
@MapperScan("io.github.yangyouwang.module.**.mapper")
public class MybatisPlusConfig implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        String userName = SecurityUtils.getUserName();
        this.setFieldValByName("createBy",userName,metaObject);
        this.setFieldValByName("createTime",new Date(),metaObject);
    }
    @Override
    public void updateFill(MetaObject metaObject) {
        String userName = SecurityUtils.getUserName();
        this.setFieldValByName("updateBy", userName, metaObject);
        this.setFieldValByName("updateTime", new Date(), metaObject);
    }

    /**
     * 新的分页插件,一缓和二缓遵循mybatis的规则,需要设置 MybatisConfiguration#useDeprecatedExecutor = false 避免缓存出现问题(该属性会在旧插件移除后一同移除)
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 分页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        // 防全表更新与删除插件
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        // 乐观锁插件
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        return interceptor;
    }

    @Bean
    public CrudSqlInjector easySqlInjector() {
        return new CrudSqlInjector();
    }

    /**
     * 自定义数据注入
     */
    static class CrudSqlInjector extends DefaultSqlInjector {
        @Override
        public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
            List<AbstractMethod> methodList = super.getMethodList(mapperClass);
            methodList.add(new InsertBatchSomeColumn());
            return methodList;
        }
    }
}
