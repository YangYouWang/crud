package ${package.Service};

import ${package.Entity}.${entity};
import ${package.Mapper}.${table.mapperName};
import ${superServiceImplClassPackage};
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
* <p>
 * ${table.comment!} 服务实现类
 * </p>
*
* @author ${author}
* @since ${date}
*/
@Service
<#if kotlin>
open class ${table.serviceName} : ${superServiceImplClass}<${table.mapperName}, ${entity}>() {

}
<#else>
public class ${table.serviceName} extends ${superServiceImplClass}<${table.mapperName}, ${entity}> {

  /**
  * ${table.comment!}分页列表
  * @param param 参数
  * @return 结果
  */
  public List<${entity}> page(${entity} param) {
    QueryWrapper<${entity}> queryWrapper = new QueryWrapper<>();
    queryWrapper.lambda()
    <#list table.fields as field>
      // ${field.comment}
      <#if !entityLombokModel>
        <#if field.propertyType == "Boolean">
          <#assign getprefix="is"/>
        <#else>
          <#assign getprefix="get"/>
        </#if>
        <#if field.propertyType == "String">
          .eq(!StringUtils.isEmpty(param.${getprefix}${field.capitalName}()), ${entity}::${getprefix}${field.capitalName}, param.${getprefix}${field.capitalName}())
        <#else>
          .eq(param.${getprefix}${field.capitalName}() != null, ${entity}::${getprefix}${field.capitalName}, param.${getprefix}${field.capitalName}())
        </#if>
      <#else>
        <#if field.propertyType == "String">
          .eq(!StringUtils.isEmpty(param.get${field.capitalName}()), ${entity}::get${field.capitalName}, param.get${field.capitalName}())
        <#else>
          .eq(param.get${field.capitalName}() != null, ${entity}::get${field.capitalName}, param.get${field.capitalName}())
        </#if>
      </#if>
    </#list>.orderByDesc(${entity}::getCreateTime);
    return list(queryWrapper);
  }

  /**
  * ${table.comment!}详情
  * @param id 主键
  * @return 结果
  */
  public ${entity} info(Long id) {
    return getById(id);
  }

  /**
  * ${table.comment!}新增
  * @param param 根据需要进行传值
  */
  public void add(${entity} param) {
    save(param);
  }

  /**
  * ${table.comment!}修改
  * @param param 根据需要进行传值
  */
  public void modify(${entity} param) {
    updateById(param);
  }

  /**
  * ${table.comment!}删除(单个条目)
  * @param id 主键
  */
  public void remove(Long id) {
    removeById(id);
  }

  /**
  * ${table.comment!}删除(多个条目)
  * @param ids 主键数组
  */
  public void removes(List<Long> ids) {
     removeByIds(ids);
   }
}
</#if>