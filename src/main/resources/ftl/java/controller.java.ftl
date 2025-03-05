package ${package.Controller};

import ${package.Entity}.${entity};
import ${package.Service}.${table.serviceName};
import io.github.yangyouwang.common.domain.Result;
import io.github.yangyouwang.common.domain.TableDataInfo;
import io.github.yangyouwang.common.annotation.CrudLog;
import io.github.yangyouwang.common.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.constraints.NotNull;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.ModelMap;

<#if restControllerStyle>
<#else>
import org.springframework.stereotype.Controller;
</#if>
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>

import javax.validation.Valid;
import java.util.Objects;
import java.util.List;
/**
* <p>
* ${table.comment} 前端控制器
* </p>
* @author ${author}
* @since ${date}
*/
@Api(tags = "${table.comment}")
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
@RequestMapping("/${package.ModuleName}/${table.entityPath}")
<#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
<#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
<#else>
public class ${table.controllerName} {
</#if>

  private static final String SUFFIX = "${package.ModuleName}/${table.entityPath}";

  @Autowired
  private ${table.serviceName} ${table.serviceName?uncap_first};

  @GetMapping("/listPage")
  public String listPage(){
    return SUFFIX + "/list";
  }

  @ApiOperation(value = "${table.comment}分页列表", response = ${entity}.class)
  @ApiImplicitParams({
    @ApiImplicitParam(name = "page", value = "第几页", dataType = "Integer", paramType="query"),
    @ApiImplicitParam(name = "limit", value = "每页记录数", dataType = "Integer", paramType="query")
  })
  @PreAuthorize("hasAuthority('${table.entityPath}:list')")
  @GetMapping(value = "/page")
  @ResponseBody
  public TableDataInfo page(${entity} param) {
    startPage();
    List<${entity}> data = ${table.serviceName?uncap_first}.page(param);
    return getDataTable(data);
  }

   @GetMapping("/editPage/{id}")
   public String editPage(@Valid @NotNull(message = "id不能为空") @PathVariable Long id, ModelMap map){
    Object data = ${table.serviceName?uncap_first}.info(id);
    map.put("${table.entityPath}",data);
    return SUFFIX + "/edit";
  }

  @GetMapping("/addPage")
  public String addPage(){
    return SUFFIX + "/add";
  }

  @CrudLog(title = "${table.comment}新增",businessType = BusinessType.INSERT)
  @ApiOperation(value = "${table.comment}新增")
  @PreAuthorize("hasAuthority('${table.entityPath}:add')")
  @PostMapping(value = "/add")
  @ResponseBody
  public Result add(@RequestBody @Validated ${entity} param,BindingResult bindingResult) {
    if (bindingResult.hasErrors()){
        return Result.failure(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
    }
    ${table.serviceName?uncap_first}.add(param);
    return Result.success();
  }

  @CrudLog(title = "${table.comment}修改",businessType = BusinessType.UPDATE)
  @ApiOperation(value = "${table.comment}修改")
  @PreAuthorize("hasAuthority('${table.entityPath}:edit')")
  @PostMapping(value = "/modify")
  @ResponseBody
  public Result modify(@RequestBody @Validated ${entity} param,BindingResult bindingResult) {
    if (bindingResult.hasErrors()){
        return Result.failure(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
    }
    ${table.serviceName?uncap_first}.modify(param);
    return Result.success();
  }

  @CrudLog(title = "${table.comment}删除(单个条目)",businessType = BusinessType.DELETE)
  @ApiOperation(value = "${table.comment}删除(单个条目)")
  @PreAuthorize("hasAuthority('${table.entityPath}:del')")
  @DeleteMapping(value = "/remove/{id}")
  @ResponseBody
  public Result remove(@Valid @NotNull(message = "id不能为空") @PathVariable Long id) {
    ${table.serviceName?uncap_first}.remove(id);
    return Result.success();
  }

  @CrudLog(title = "${table.comment}删除(多个条目)",businessType = BusinessType.DELETE)
  @ApiOperation(value = "${table.comment}删除(多个条目)")
  @PreAuthorize("hasAuthority('${table.entityPath}:del')")
  @PostMapping(value = "/removes")
  @ResponseBody
  public Result removes(@RequestBody @Valid List<Long> ids) {
     ${table.serviceName?uncap_first}.removes(ids);
     return Result.success();
   }
}
</#if>