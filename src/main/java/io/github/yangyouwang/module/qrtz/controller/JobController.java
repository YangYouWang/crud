package io.github.yangyouwang.module.qrtz.controller;

import io.github.yangyouwang.common.annotation.CrudLog;
import io.github.yangyouwang.common.base.controller.BaseController;
import io.github.yangyouwang.common.base.domain.TableDataInfo;
import io.github.yangyouwang.common.enums.BusinessType;
import io.github.yangyouwang.module.qrtz.entity.QrtzJob;
import io.github.yangyouwang.common.base.domain.Result;
import io.github.yangyouwang.module.qrtz.service.JobService;
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

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
/**
* <p>
* 任务表 前端控制器
* </p>
* @author yangyouwang
* @since 2022-07-30
*/
@Api(tags = "任务表")
@Controller
@RequestMapping("/qrtz/job")
@RequiredArgsConstructor
public class JobController extends BaseController {

  private static final String SUFFIX = "qrtz/job";

  private final JobService jobService;

  @GetMapping("/listPage")
  public String listPage(){
    return SUFFIX + "/list";
  }

  @ApiOperation(value = "任务表分页列表", response = QrtzJob.class)
  @ApiImplicitParams({
          @ApiImplicitParam(name = "page", value = "第几页", dataType = "Integer", paramType="query"),
          @ApiImplicitParam(name = "limit", value = "每页记录数", dataType = "Integer", paramType="query")
  })
  @PreAuthorize("hasAuthority('job:list')")
  @GetMapping(value = "/page")
  @ResponseBody
  public TableDataInfo page(@Validated QrtzJob job) {
    startPage();
    List<QrtzJob> data = jobService.page(job);
    return getDataTable(data);
  }

   @GetMapping("/editPage/{id}")
   public String editPage(@Valid @NotNull(message = "id不能为空") @PathVariable Long id, ModelMap map){
    Object data = jobService.getById(id);
    map.put("job",data);
    return SUFFIX + "/edit";
  }

  @GetMapping("/addPage")
  public String addPage(){
    return SUFFIX + "/add";
  }


  @CrudLog(title = "新增任务",businessType = BusinessType.INSERT)
  @ApiOperation(value = "任务新增")
  @PreAuthorize("hasAuthority('job:add')")
  @PostMapping(value = "/add")
  @ResponseBody
  public Result add(@RequestBody @Validated QrtzJob param, BindingResult bindingResult) {
    if (bindingResult.hasErrors()){
        return Result.failure(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
    }
    jobService.add(param);
    return Result.success();
  }
  @CrudLog(title = "修改任务",businessType = BusinessType.UPDATE)
  @ApiOperation(value = "任务表修改")
  @PreAuthorize("hasAuthority('job:edit')")
  @PostMapping(value = "/modify")
  @ResponseBody
  public Result modify(@RequestBody @Validated QrtzJob param, BindingResult bindingResult) {
    if (bindingResult.hasErrors()){
        return Result.failure(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
    }
    jobService.modify(param);
    return Result.success();
  }
  @CrudLog(title = "删除任务",businessType = BusinessType.DELETE)
  @ApiOperation(value = "任务表删除(单个条目)")
  @PreAuthorize("hasAuthority('job:del')")
  @DeleteMapping(value = "/remove/{id}")
  @ResponseBody
  public Result remove(@Valid @NotNull(message = "id不能为空") @PathVariable Long id) {
    jobService.remove(id);
    return Result.success();
  }

  @CrudLog(title = "删除任务",businessType = BusinessType.DELETE)
  @ApiOperation(value = "任务表删除(多个条目)")
  @PreAuthorize("hasAuthority('job:del')")
  @PostMapping(value = "/removes")
  @ResponseBody
  public Result removes(@RequestBody @Valid List<Long> ids) {
     jobService.removes(ids);
     return Result.success();
   }

   @GetMapping("/cronPage")
   public String cronPage(){
        return "qrtz/cron/index";
    }

    /**
     * 修改任务状态
     * @param param 修改任务状态对象
     * @return 修改状态响应
     */
    @CrudLog(title = "更新任务状态",businessType = BusinessType.UPDATE)
    @PreAuthorize("hasAuthority('job:edit')")
    @PostMapping("/changeJob")
    @ResponseBody
    public Result changeJob(@RequestBody @Validated QrtzJob param, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            return Result.failure(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        boolean flag = jobService.changeJob(param);
        return Result.success(flag);
    }
}
