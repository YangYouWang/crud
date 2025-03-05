# 简约后台管理系统
   ![logo](https://gitee.com/yangyouwang/crud/raw/master/src/main/resources/static/img/logo.jpg "logo.jpg")

## 项目介绍
    1.需求定义：外包项目如雨后春笋，开发工期被迫压缩，为了开发人员专注开发项目业务，早点下班能陪老婆、孩子。
    2.产品定位: 简约后台管理系统
    3.项目特点：此项目代码清晰、界面简洁、springboot + layuiadmin 构建的单体后台管理系统。

## 联系我们
- 官方网站：[www.weibendi.cn](https://www.weibendi.cn/) 
- 联系人：杨先生
- 手机号：17515087128

## 软件架构
- 核心框架：Spring Boot
- 权限框架：SpringSecurity
- 模板引擎：Thymeleaf
- 持久层框架：Mybatis-Plus
- 日志管理：LogBack
- 工具类：Apache Commons、Hutool
- 视图框架：Spring MVC
- 定时器：Quartz
- 数据库连接池：Druid
- 页面交互：layuiAdmin
- 验证框架：hibernate-Validation
- 接口文档：Swagger

## 环境需求
    JDK >= 1.8
    MySQL >= 5.7
    Maven >= 3.0
    redis >= 6.0.6
    minio 版本无要求

## 项目搭建
    1、推荐使用IDEA开发工具运行此项目
    2、在mysql中创建crud数据库后，项目启动时候表结构会自动创建
    3、针对各环境修改对应配置文件：application-dev.yml开发环境、application-prod.yml生产环境、application-test.yml测试环境
   ![配置截图](https://gitee.com/yangyouwang/crud/raw/master/src/main/resources/static/img/13.png "13.png")
    4、运行项目中CrudApplication.java启动类
   ![配置截图](https://gitee.com/yangyouwang/crud/raw/master/src/main/resources/static/img/14.png "14.png")
## 使用说明
    
    1.控制层接口版本管理、包装响应Result返回值进行全局处理，使用@ResponseResultBody注解
    
    2.定义接口版本，在方法中配置@ApiVersion注解
    
    3.想要实现客户端不需要登录就可以访问后端接口，只需要在控制层的JAVA方法中加入@PassToken注解，然后该接口可以跳过jwt安全认证
    
    4.在JAVA方法中加入@CrudLog注解可以将用户操作后台管理系统中操作日志记录到数据库sys_log日志表中
```
// BusinessType.INSERT 新增操作
// BusinessType.UPDATE 更新操作
// BusinessType.DELETE 删除操作
@CrudLog(title = "详细描述",businessType = BusinessType.INSERT) 
```

    5.代码中设置Security权限
```
@PreAuthorize("hasAuthority('权限标识')") // JAVA代码方法上加入注解
sec:authorize="hasAuthority('权限标识')" // thymeleaf声明
```
    
    6.客户端向header处放入token值后发起请求，后端拦截客户端请求，获取并解析header中token，拿到用户的userId放入Map中，然后通过以下方法可以获取用户ID。
```
 Long userId = ApiContext.getUserId();
```
    7.将list转化tree结构方法
```
 ListToTree treeBuilder = new ListToTreeImpl();
 treeBuilder.toTree(menus);
```
    8.调用发送阿里云Email邮件方法
```
 SampleEmail.sample(邮件地址,标题,内容);
```
    9.调用上传文件到阿里云oss或minio服务器方法
```
 SampleOSS.upload(文件流, 自定义上传路径);
```
    10.调用发送阿里云短信方法
```
 SampleSms.sendSms(手机号,模版号,签名); 
```   
    11.导出Excel文件
```
   //导出Excel
   EasyExcel.write(response.getOutputStream())
              //自动关闭流
              .autoCloseStream(Boolean.FALSE)
              //指定excel文件的type
              .excelType(ExcelTypeEnum.XLSX)
              // 标题头
              .head(导出类.class)
              //给定工作表名称
              .sheet(fileName)
              //给定样式
              .registerWriteHandler(EasyExcelUtil.getStyleStrategy())
              //给定导出数据
              .doWrite(datas);
              
   // excel数据转换成系统字典
   @ExcelProperty(value = {"用户性别"}, index = 0, converter = BaseDictDataConverter.class)
   // key = 字典KEY
   @DictType(key = ConfigConsts.DICT_KEY_SEX)
```
    12.字典组件
```
   layui.config({
        base: '/static/layuiadmin/' //静态资源所在路径
    }).extend({
      index: 'lib/index' //主入口模块
   }).use(["crud"],function(){
    let crud = layui.crud; 
     // 获取枚举
     crud.getDictValue('字典类型',枚举值);
     // radio赋值
     crud.setRadio("id节点","字典类型","默认值");
     // select赋值
     crud.setSelect("id节点","字典类型",'默认值');
  })
```   
    13.下拉框多选组件
```
   crud.initXmSelect("自定义select接口","id节点");
```  
    14.图片上传组件
```
    crud.uploadFile("id节点");
```  
    15.向导页面组件
```
   按钮设置点击事件
   $('按钮ID节点').on('click', function () {
      // 树结构向导
      crud.treeWizard("自定义select接口",function (result) {
        form.val('表单的lay-filter属性值', {
          赋值id值: result.id,
          赋值name值: result.name
        });
      });
   });
```
## 部分截图
    
    登陆功能
    账号：admin 密码：123456
   ![运行截图](https://gitee.com/yangyouwang/crud/raw/master/src/main/resources/static/img/0.png "0.png")
    
    首页
   ![运行截图](https://gitee.com/yangyouwang/crud/raw/master/src/main/resources/static/img/1.png "1.png")
   
    用户管理模块
   ![运行截图](https://gitee.com/yangyouwang/crud/raw/master/src/main/resources/static/img/2.png "2.png")
   
    角色管理模块
   ![运行截图](https://gitee.com/yangyouwang/crud/raw/master/src/main/resources/static/img/3.png "3.png")

    部门管理
   ![运行截图](https://gitee.com/yangyouwang/crud/raw/master/src/main/resources/static/img/18.png "18.png")

    岗位管理
   ![运行截图](https://gitee.com/yangyouwang/crud/raw/master/src/main/resources/static/img/19.png "19.png")

    菜单管理模块
   ![运行截图](https://gitee.com/yangyouwang/crud/raw/master/src/main/resources/static/img/4.png "4.png")
   
    swagger接口文档
   ![运行截图](https://gitee.com/yangyouwang/crud/raw/master/src/main/resources/static/img/5.png "5.png")
   
    druid监控
    账号：admin 密码：admin
   ![运行截图](https://gitee.com/yangyouwang/crud/raw/master/src/main/resources/static/img/6.png "6.png")
   
    定时任务
   ![运行截图](https://gitee.com/yangyouwang/crud/raw/master/src/main/resources/static/img/8.png "8.png")
   
    字典管理
   ![运行截图](https://gitee.com/yangyouwang/crud/raw/master/src/main/resources/static/img/12.png "12.png")

    登录日志管理
   ![运行截图](https://gitee.com/yangyouwang/crud/raw/master/src/main/resources/static/img/11.png "11.png")

    操作日志管理
   ![运行截图](https://gitee.com/yangyouwang/crud/raw/master/src/main/resources/static/img/15.png "15.png")
