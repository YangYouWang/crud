package io.github.yangyouwang.module.code.service;

import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import io.github.yangyouwang.common.base.CrudBaseController;
import io.github.yangyouwang.common.domain.BaseEntity;
import io.github.yangyouwang.framework.web.exception.BusinessException;
import io.github.yangyouwang.module.code.model.dto.BuildDTO;
import io.github.yangyouwang.module.code.model.vo.FieldVO;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 生成代码业务层
 */
@Service
public class CodeService {

    @Resource
    private DynamicDataSourceProperties dynamicDataSourceProperties;

    private String driverClassName;

    private String url;

    private String username;

    private String password;

    public List<String> selectTable() {
        try {
            List<String> tableNames = new ArrayList<>();
            Class.forName(driverClassName);
            Connection conn = DriverManager.getConnection(url, username, password);
            // 获取数据库元数据
            DatabaseMetaData databaseMetaData = conn.getMetaData();
            System.out.println("conn.getCatalog() = " + conn.getCatalog());
            ResultSet tableRet = databaseMetaData.getTables(conn.getCatalog(), "%", "%", new String[] { "TABLE" });
            while (tableRet.next()) {
                String tableName = (String) tableRet.getObject("TABLE_NAME");
                tableNames.add(tableName);
            }
            return tableNames;
        } catch (Exception e) {
            throw new BusinessException("数据库链接错误");
        }
    }

    public List<FieldVO> selectField(String tableName) {
        try {
            List<FieldVO> result = new ArrayList();
            Class.forName(driverClassName);
            Connection conn = DriverManager.getConnection(url, username, password);
            // 获取数据库元数据
            DatabaseMetaData databaseMetaData = conn.getMetaData();
            ResultSet resultSet = databaseMetaData.getTables(null, "%", tableName, new String[] { "TABLE" });
            while (resultSet.next()) {
                if(resultSet.getString("TABLE_NAME").equals(tableName)) {
                    ResultSet rs = conn.getMetaData().getColumns(null, conn.getMetaData().getUserName(),tableName, "%");
                    while(rs.next()) {
                        FieldVO field = new FieldVO();
                        String columnName = rs.getString("COLUMN_NAME");
                        field.setColumnName(columnName);
                        String remarks = rs.getString("REMARKS");
                        if(Strings.isBlank(remarks)){
                            remarks = columnName;
                        }
                        field.setRemarks(remarks);
                        String typeName = rs.getString("TYPE_NAME");
                        field.setTypeName(typeName);
                        String columnSize = rs.getString("COLUMN_SIZE");
                        field.setColumnSize(columnSize);
                        result.add(field);
                    }
                }
            }
            return result;
        } catch (Exception e) {
            throw new BusinessException("数据库链接错误");
        }
    }

    /**
     * 生成代码逻辑
     * @param build 生成代码
     */
    public void build(BuildDTO build) {
        String path = System.getProperty("user.dir");
        AutoGenerator mpg = new AutoGenerator();
        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(path + "/src/main/java");
        gc.setAuthor(build.getAuthor());
        gc.setOpen(false);
        //实体属性 Swagger2 注解
        gc.setSwagger2(true);
        gc.setFileOverride(true);
        // 自定义Service模板 文件名
        gc.setServiceName("%sService");
        //定义生成的实体类中日期类型
        gc.setDateType(DateType.ONLY_DATE);
        mpg.setGlobalConfig(gc);
        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(url);
        dsc.setDriverName(driverClassName);
        dsc.setUsername(username);
        dsc.setPassword(password);
        mpg.setDataSource(dsc);
        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setModuleName(build.getModuleName());
        pc.setParent("io.github.yangyouwang.module");
        mpg.setPackageInfo(pc);
        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<>(16);
                map.put("menuId", build.getMenuId());
                this.setMap(map);
            }
        };
        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig("/ftl/java/service.java.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return path + "/src/main/java/io/github/yangyouwang/module/" + pc.getModuleName()
                        + "/service/" + tableInfo.getEntityName() + "Service" + StringPool.DOT_JAVA;
            }
        });
        focList.add(new FileOutConfig("/templates/mapper.xml.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return path + "/src/main/resources/mapper/" + pc.getModuleName()
                        + StringPool.SLASH + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        focList.add(new FileOutConfig("/ftl/html/list.html.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输入文件名称
                return path + "/src/main/resources/templates/" + pc.getModuleName()
                        + StringPool.SLASH + tableInfo.getEntityPath() + StringPool.SLASH + "list.html";
            }
        });
        focList.add(new FileOutConfig("/ftl/html/add.html.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输入文件名称
                return path + "/src/main/resources/templates/" + pc.getModuleName()
                        + StringPool.SLASH + tableInfo.getEntityPath() + StringPool.SLASH + "add.html";
            }
        });
        focList.add(new FileOutConfig("/ftl/html/edit.html.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输入文件名称
                return path + "/src/main/resources/templates/" + pc.getModuleName()
                        + StringPool.SLASH + tableInfo.getEntityPath() + StringPool.SLASH + "edit.html";
            }
        });
        focList.add(new FileOutConfig("/ftl/sql/menu.sql.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输入文件名称
                return path + "/src/main/resources/sql/" + pc.getModuleName()
                        + StringPool.SLASH + tableInfo.getEntityPath() + StringPool.SLASH + "menu.sql";
            }
        });
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);
        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();
        // 配置自定义输出模板
        //指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
        templateConfig.setController("ftl/java/controller.java");
        // 关闭原有生成
        templateConfig.setService(null);
        templateConfig.setServiceImpl(null);
        templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);
        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setSuperEntityClass(BaseEntity.class);
        strategy.setSuperControllerClass(CrudBaseController.class);
        strategy.setSuperEntityColumns("id","create_by","create_time","update_by","update_time","deleted","remark");
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(false);
        String[] tableName = new String[]{build.getTableName()};
        strategy.setInclude(tableName);
        //url中驼峰转连字符
        strategy.setControllerMappingHyphenStyle(true);
        //生成实体时去掉表前缀
        strategy.setTablePrefix(pc.getModuleName() + "_");
        // lombok 模型 @Accessors(chain = true) setter链式操作
        strategy.setEntityLombokModel(true);
        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }

    @PostConstruct
    public void getDataSourceProperty() {
        Map<String, DataSourceProperty> datasource = dynamicDataSourceProperties.getDatasource();
        DataSourceProperty master = datasource.get("master");
        driverClassName = master.getDriverClassName();
        url = master.getUrl();
        username = master.getUsername();
        password = master.getPassword();
    }
}
