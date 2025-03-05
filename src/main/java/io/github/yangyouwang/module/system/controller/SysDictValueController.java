package io.github.yangyouwang.module.system.controller;

import io.github.yangyouwang.common.annotation.CrudLog;
import io.github.yangyouwang.common.base.controller.BaseController;
import io.github.yangyouwang.common.base.domain.Result;
import io.github.yangyouwang.common.enums.BusinessType;
import io.github.yangyouwang.module.system.entity.SysDictValue;
import io.github.yangyouwang.module.system.service.SysDictValueService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * @author zhixin.yao
 * @version 1.0
 * @description: 字典控制层
 * @date 2021/4/12 11:00
 */
@Api(tags = "数据字典项")
@Controller
@RequestMapping("/system/sysDictValue")
@RequiredArgsConstructor
public class SysDictValueController extends BaseController {

    private static final String SUFFIX = "system/sysDictValue";

    private final SysDictValueService sysDictValueService;

    /**
     * 删除字典值请求
     * @param id 字典id
     * @return 删除状态
     */
    @CrudLog(title = "删除字典值",businessType = BusinessType.DELETE)
    @ApiOperation(value = "删除字典值(单个条目)")
    @PreAuthorize("hasAuthority('dictValue:del')")
    @DeleteMapping(value = "/remove/{id}")
    @ResponseBody
    public Result remove(@Valid @NotNull(message = "id不能为空") @PathVariable Long id) {
        boolean flag = sysDictValueService.removeById(id);
        return Result.success(flag);
    }
    /**
     * 跳转添加
     * @param id 字典类型id
     * @return 添加页面
     */
    @GetMapping("/addPage/{id}")
    public String addPage(@Valid @NotNull(message = "dictTypeId不能为空") @PathVariable Long id, ModelMap map){
        map.put("dictTypeId",id);
        return SUFFIX + "/add";
    }

    /**
     * 跳转编辑
     * @param id 字典值id
     * @return 编辑页面
     */
    @GetMapping("/editPage/{id}")
    public String editPage(@Valid @NotNull(message = "id能为空") @PathVariable Long id, ModelMap map){
        SysDictValue sysDictValue = sysDictValueService.getById(id);
        map.put("sysDictValue",sysDictValue);
        return SUFFIX + "/edit";
    }

    /**
     * 添加请求
     * @param sysDictValue 添加字典值对象
     * @return 添加状态
     */
    @CrudLog(title = "添加字典值",businessType = BusinessType.INSERT)
    @PreAuthorize("hasAuthority('dictValue:add')")
    @PostMapping("/add")
    @ResponseBody
    public Result add(@RequestBody @Validated SysDictValue sysDictValue, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return Result.failure(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        boolean flag = sysDictValueService.save(sysDictValue);
        return Result.success(flag);
    }

    /**
     * 编辑请求
     * @param sysDictValue 编辑字典值对象
     * @return 编辑状态
     */
    @CrudLog(title = "更新字典值",businessType = BusinessType.UPDATE)
    @PreAuthorize("hasAuthority('dictValue:edit')")
    @PostMapping("/edit")
    @ResponseBody
    public Result edit(@RequestBody @Validated SysDictValue sysDictValue, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return Result.failure(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        boolean flag = sysDictValueService.updateById(sysDictValue);
        return Result.success(flag);
    }
}
