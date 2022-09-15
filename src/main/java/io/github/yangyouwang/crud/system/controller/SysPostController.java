package io.github.yangyouwang.crud.system.controller;

import io.github.yangyouwang.common.annotation.CrudLog;
import io.github.yangyouwang.common.enums.BusinessType;
import io.github.yangyouwang.crud.system.entity.SysPost;
import io.github.yangyouwang.crud.system.service.SysPostService;
import io.github.yangyouwang.common.domain.Result;
import io.github.yangyouwang.common.domain.TableDataInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.constraints.NotNull;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.ui.ModelMap;

 import org.springframework.stereotype.Controller;
 import io.github.yangyouwang.common.base.CrudController;

import javax.validation.Valid;
import java.util.Objects;
import java.util.List;
/**
* <p>
* 岗位 前端控制器
* </p>
* @author yangyouwang
* @since 2022-09-15
*/
@Api(tags = "岗位")
@Controller
@RequestMapping("/system/sysPost")
public class SysPostController extends CrudController {

  private static final String SUFFIX = "/system/sysPost";

  @Autowired
  private SysPostService sysPostService;

  @GetMapping("/listPage")
  public String listPage(){
    return SUFFIX + "/list";
  }

  @ApiOperation(value = "岗位分页列表", response = SysPost.class)
  @ApiImplicitParams({
  @ApiImplicitParam(name = "page", value = "第几页", dataType = "Integer"),
  @ApiImplicitParam(name = "limit", value = "每页记录数", dataType = "Integer")})
  @GetMapping(value = "/page")
  @ResponseBody
  public TableDataInfo page(SysPost param) {
    startPage();
    List<SysPost> data = sysPostService.page(param);
    return getDataTable(data);
  }

   @GetMapping("/editPage/{id}")
   public String editPage(@Valid @NotNull(message = "id不能为空") @PathVariable Long id, ModelMap map){
    Object data = sysPostService.info(id);
    map.put("sysPost",data);
    return SUFFIX + "/edit";
  }

  @GetMapping("/addPage")
  public String addPage(){
    return SUFFIX + "/add";
  }

  @ApiOperation(value = "岗位新增")
  @PostMapping(value = "/add")
  @ResponseBody
  public Result add(@RequestBody @Validated SysPost param,BindingResult bindingResult) {
    if (bindingResult.hasErrors()){
        return Result.failure(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
    }
    sysPostService.add(param);
    return Result.success();
  }

  @ApiOperation(value = "岗位修改")
  @PostMapping(value = "/modify")
  @ResponseBody
  public Result modify(@RequestBody @Validated SysPost param,BindingResult bindingResult) {
    if (bindingResult.hasErrors()){
        return Result.failure(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
    }
    sysPostService.modify(param);
    return Result.success();
  }

  @ApiOperation(value = "岗位删除(单个条目)")
  @DeleteMapping(value = "/remove/{id}")
  @ResponseBody
  public Result remove(@Valid @NotNull(message = "id不能为空") @PathVariable Long id) {
    sysPostService.remove(id);
    return Result.success();
  }

  @ApiOperation(value = "岗位删除(多个条目)")
  @PostMapping(value = "/removes")
  @ResponseBody
  public Result removes(@RequestBody @Valid List<Long> ids) {
     sysPostService.removes(ids);
     return Result.success();
   }

  /**
   * 修改岗位状态
   * @param param 修改岗位状态对象
   * @return 修改状态
   */
  @CrudLog(title = "更新岗位状态",businessType = BusinessType.UPDATE)
  @PostMapping("/changePost")
  @ResponseBody
  public Result changePost(@RequestBody @Validated SysPost param, BindingResult bindingResult) {
    if (bindingResult.hasErrors()){
      return Result.failure(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
    }
    boolean flag = sysPostService.updateById(param);
    return Result.success(flag);
  }
}
