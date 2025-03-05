package io.github.yangyouwang.module.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.github.yangyouwang.common.annotation.CrudLog;
import io.github.yangyouwang.common.base.CrudController;
import io.github.yangyouwang.common.domain.Result;
import io.github.yangyouwang.common.domain.TableDataInfo;
import io.github.yangyouwang.common.enums.BusinessType;
import io.github.yangyouwang.module.system.entity.SysLog;
import io.github.yangyouwang.module.system.service.SysLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author yangyouwang
 * @title: SysLogController
 * @projectName crud
 * @description: 日志控制层
 * @date 2021/4/111:06 AM
 */
@Api(tags = "日志表")
@Controller
@RequestMapping("/system/sysLog")
@RequiredArgsConstructor
public class SysLogController extends CrudController {

    private static final String SUFFIX = "system/sysLog";

    private final SysLogService sysLogService;

    /**
     * 跳转列表
     * @return 列表页面
     */
    @GetMapping("/listPage")
    public String listPage(){
        return SUFFIX + "/list";
    }

    /**
     * 列表请求
     * @param sysLog 日志列表对象
     * @return 请求列表
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", dataType = "Integer", paramType="query"),
            @ApiImplicitParam(name = "limit", value = "每页记录数", dataType = "Integer", paramType="query")
    })
    @PreAuthorize("hasAuthority('log:list')")
    @GetMapping("/page")
    @ResponseBody
    public TableDataInfo page(SysLog sysLog) {
        startPage();
        List<SysLog> list = sysLogService.list(new LambdaQueryWrapper<SysLog>()
                .like(StringUtils.isNotBlank(sysLog.getTitle()), SysLog::getTitle , sysLog.getTitle())
                .like(StringUtils.isNotBlank(sysLog.getClassName()), SysLog::getClassName , sysLog.getClassName())
                .like(StringUtils.isNotBlank(sysLog.getMethodName()), SysLog::getMethodName , sysLog.getMethodName())
                .orderByDesc(SysLog::getCreateTime));
        return getDataTable(list);
    }

    @CrudLog(title = "删除日志",businessType = BusinessType.DELETE)
    @ApiOperation(value = "删除日志(单个条目)")
    @PreAuthorize("hasAuthority('log:del')")
    @DeleteMapping(value = "/remove/{id}")
    @ResponseBody
    public Result remove(@Valid @NotNull(message = "id不能为空") @PathVariable Long id) {
        boolean flag = sysLogService.removeById(id);
        return Result.success(flag);
    }

    @CrudLog(title = "删除日志",businessType = BusinessType.DELETE)
    @ApiOperation(value = "删除日志(多个条目)")
    @PreAuthorize("hasAuthority('log:del')")
    @PostMapping(value = "/removes")
    @ResponseBody
    public Result removes(@RequestBody @Valid List<Long> ids) {
        boolean flag = sysLogService.removeByIds(ids);
        return Result.success(flag);
    }

    @GetMapping("/infoPage/{id}")
    public String infoPage(@Valid @NotNull(message = "id不能为空") @PathVariable Long id, ModelMap map){
        SysLog sysLog = sysLogService.getById(id);
        map.put("sysLog",sysLog);
        return SUFFIX + "/info";
    }
}
