package io.github.yangyouwang.module.system.controller;

import io.github.yangyouwang.common.annotation.CrudLog;
import io.github.yangyouwang.common.base.domain.XmSelectNode;
import io.github.yangyouwang.common.enums.BusinessType;
import io.github.yangyouwang.module.system.entity.SysPost;
import io.github.yangyouwang.module.system.service.SysPostService;
import io.github.yangyouwang.common.base.domain.Result;
import io.github.yangyouwang.common.base.domain.TableDataInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.constraints.NotNull;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.ui.ModelMap;

 import org.springframework.stereotype.Controller;
 import io.github.yangyouwang.common.base.controller.BaseController;

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
@RequiredArgsConstructor
public class SysPostController extends BaseController {

  private static final String SUFFIX = "system/sysPost";

  private final SysPostService sysPostService;

  @GetMapping("/listPage")
  public String listPage(){
    return SUFFIX + "/list";
  }

  @ApiOperation(value = "岗位分页列表", response = SysPost.class)
  @ApiImplicitParams({
          @ApiImplicitParam(name = "page", value = "第几页", dataType = "Integer", paramType="query"),
          @ApiImplicitParam(name = "limit", value = "每页记录数", dataType = "Integer", paramType="query")
  })
  @PreAuthorize("hasAuthority('sysPost:list')")
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

  @CrudLog(title = "新增岗位",businessType = BusinessType.INSERT)
  @ApiOperation(value = "岗位新增")
  @PreAuthorize("hasAuthority('sysPost:add')")
  @PostMapping(value = "/add")
  @ResponseBody
  public Result add(@RequestBody @Validated SysPost param,BindingResult bindingResult) {
    if (bindingResult.hasErrors()){
        return Result.failure(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
    }
    sysPostService.add(param);
    return Result.success();
  }

  @CrudLog(title = "修改岗位",businessType = BusinessType.UPDATE)
  @ApiOperation(value = "岗位修改")
  @PreAuthorize("hasAuthority('sysPost:edit')")
  @PostMapping(value = "/modify")
  @ResponseBody
  public Result modify(@RequestBody @Validated SysPost param,BindingResult bindingResult) {
    if (bindingResult.hasErrors()){
        return Result.failure(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
    }
    sysPostService.modify(param);
    return Result.success();
  }

  @CrudLog(title = "删除岗位",businessType = BusinessType.DELETE)
  @ApiOperation(value = "岗位删除(单个条目)")
  @PreAuthorize("hasAuthority('sysPost:del')")
  @DeleteMapping(value = "/remove/{id}")
  @ResponseBody
  public Result remove(@Valid @NotNull(message = "id不能为空") @PathVariable Long id) {
    sysPostService.remove(id);
    return Result.success();
  }

  @CrudLog(title = "删除岗位",businessType = BusinessType.DELETE)
  @ApiOperation(value = "岗位删除(多个条目)")
  @PreAuthorize("hasAuthority('sysPost:del')")
  @PostMapping(value = "/removes")
  @ResponseBody
  public Result removes(@RequestBody @Valid List<Long> ids) {
     sysPostService.removes(ids);
     return Result.success();
   }

  /**
   * 根据岗位ids查询选中岗位列表
   * @param ids 岗位ids
   * @return 岗位下拉列表
   */
  @GetMapping("/xmSelect")
  @ResponseBody
  public List<XmSelectNode> xmSelect(@RequestParam(value = "ids",required = false) String ids) {
    return sysPostService.xmSelect(ids);
  }
}
