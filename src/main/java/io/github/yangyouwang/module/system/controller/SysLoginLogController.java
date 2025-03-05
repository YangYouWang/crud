package io.github.yangyouwang.module.system.controller;

import io.github.yangyouwang.common.annotation.CrudLog;
import io.github.yangyouwang.common.domain.Result;
import io.github.yangyouwang.common.enums.BusinessType;
import io.github.yangyouwang.module.system.entity.SysLoginLog;
import io.github.yangyouwang.module.system.service.SysLoginLogService;
import io.github.yangyouwang.common.domain.TableDataInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import io.github.yangyouwang.common.base.CrudController;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
/**
* <p>
* 用户登录日志记录表 前端控制器
* </p>
* @author yangyouwang
* @since 2022-08-29
*/
@Api(tags = "用户登录日志记录表")
@Controller
@RequestMapping("/system/sysLoginLog")
@RequiredArgsConstructor
public class SysLoginLogController extends CrudController {

  private static final String SUFFIX = "system/sysLoginLog";

  private final SysLoginLogService sysLoginLogService;

  @GetMapping("/listPage")
  public String listPage(){
    return SUFFIX + "/list";
  }

  @ApiOperation(value = "用户登录日志记录表分页列表", response = SysLoginLog.class)
  @ApiImplicitParams({
          @ApiImplicitParam(name = "page", value = "第几页", dataType = "Integer", paramType="query"),
          @ApiImplicitParam(name = "limit", value = "每页记录数", dataType = "Integer", paramType="query")
  })
  @PreAuthorize("hasAuthority('sysLoginLog:list')")
  @GetMapping(value = "/page")
  @ResponseBody
  public TableDataInfo page(SysLoginLog param) {
    startPage();
    List<SysLoginLog> data = sysLoginLogService.page(param);
    return getDataTable(data);
  }

  @CrudLog(title = "删除登录日志",businessType = BusinessType.DELETE)
  @ApiOperation(value = "删除登录日志(单个条目)")
  @PreAuthorize("hasAuthority('sysLoginLog:del')")
  @DeleteMapping(value = "/remove/{id}")
  @ResponseBody
  public Result remove(@Valid @NotNull(message = "id不能为空") @PathVariable Long id) {
    boolean flag = sysLoginLogService.removeById(id);
    return Result.success(flag);
  }

  @CrudLog(title = "删除登录日志",businessType = BusinessType.DELETE)
  @ApiOperation(value = "删除登录日志(多个条目)")
  @PreAuthorize("hasAuthority('sysLoginLog:del')")
  @PostMapping(value = "/removes")
  @ResponseBody
  public Result removes(@RequestBody @Valid List<Long> ids) {
    boolean flag = sysLoginLogService.removeByIds(ids);
    return Result.success(flag);
  }
}
