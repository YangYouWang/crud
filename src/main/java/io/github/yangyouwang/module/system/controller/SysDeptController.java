package io.github.yangyouwang.module.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.github.yangyouwang.common.base.domain.TreeSelectNode;
import io.github.yangyouwang.module.system.entity.SysDept;
import io.github.yangyouwang.module.system.service.SysDeptService;
import io.github.yangyouwang.common.base.domain.Result;
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
* 部门表 前端控制器
* </p>
* @author yangyouwang
* @since 2022-09-03
*/
@Api(tags = "部门表")
@Controller
@RequestMapping("/system/sysDept")
@RequiredArgsConstructor
public class SysDeptController extends BaseController {

  private static final String SUFFIX = "system/sysDept";

  private final SysDeptService sysDeptService;

  @GetMapping("/listPage")
  public String listPage(){
    return SUFFIX + "/list";
  }

  @ApiOperation(value = "部门表分页列表", response = SysDept.class)
  @ApiImplicitParams({
          @ApiImplicitParam(name = "page", value = "第几页", dataType = "Integer", paramType="query"),
          @ApiImplicitParam(name = "limit", value = "每页记录数", dataType = "Integer", paramType="query")
  })
  @PreAuthorize("hasAuthority('sysDept:list')")
  @GetMapping(value = "/page")
  @ResponseBody
  public Result page() {
    List<SysDept> list = sysDeptService.list(new LambdaQueryWrapper<SysDept>()
            .orderByAsc(SysDept::getParentId,SysDept::getOrderNum));
    return Result.success(list);
  }

   @GetMapping("/editPage/{id}")
   public String editPage(@Valid @NotNull(message = "id不能为空") @PathVariable Long id, ModelMap map){
    Object data = sysDeptService.info(id);
    map.put("sysDept",data);
    return SUFFIX + "/edit";
  }

  @GetMapping("/addPage")
  public String addPage(SysDept sysDept, ModelMap map) {
    map.put("parentId",sysDept.getId());
    map.put("parentName",sysDept.getDeptName());
    return SUFFIX + "/add";
  }

  @ApiOperation(value = "部门表新增")
  @PreAuthorize("hasAuthority('sysDept:add')")
  @PostMapping(value = "/add")
  @ResponseBody
  public Result add(@RequestBody @Validated SysDept param,BindingResult bindingResult) {
    if (bindingResult.hasErrors()){
        return Result.failure(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
    }
    sysDeptService.add(param);
    return Result.success();
  }

  @ApiOperation(value = "部门表修改")
  @PreAuthorize("hasAuthority('sysDept:edit')")
  @PostMapping(value = "/modify")
  @ResponseBody
  public Result modify(@RequestBody @Validated SysDept param,BindingResult bindingResult) {
    if (bindingResult.hasErrors()){
        return Result.failure(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
    }
    sysDeptService.modify(param);
    return Result.success();
  }

  @ApiOperation(value = "部门表删除(单个条目)")
  @PreAuthorize("hasAuthority('sysDept:del')")
  @DeleteMapping(value = "/remove/{id}")
  @ResponseBody
  public Result remove(@Valid @NotNull(message = "id不能为空") @PathVariable Long id) {
    sysDeptService.remove(id);
    return Result.success();
  }

  @ApiOperation(value = "部门表删除(多个条目)")
  @PreAuthorize("hasAuthority('sysDept:del')")
  @PostMapping(value = "/removes")
  @ResponseBody
  public Result removes(@RequestBody @Valid List<Long> ids) {
     sysDeptService.removes(ids);
     return Result.success();
   }

  /**
   * 查询部门树结构
   * @return 部门树结构
   */
  @GetMapping("/treeSelect")
  @ResponseBody
  public Result treeSelect() {
    List<TreeSelectNode> treeSelectNodes = sysDeptService.treeSelect();
    return Result.success(treeSelectNodes);
  }
}
