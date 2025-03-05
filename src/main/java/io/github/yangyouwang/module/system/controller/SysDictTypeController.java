package io.github.yangyouwang.module.system.controller;

import io.github.yangyouwang.common.annotation.CrudLog;
import io.github.yangyouwang.common.base.CrudBaseController;
import io.github.yangyouwang.common.domain.Result;
import io.github.yangyouwang.common.domain.TableDataInfo;
import io.github.yangyouwang.common.enums.BusinessType;
import io.github.yangyouwang.module.system.entity.SysDictType;
import io.github.yangyouwang.module.system.service.SysDictTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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
import java.util.List;
import java.util.Objects;

/**
 * @author zhixin.yao
 * @version 1.0
 * @description: 字典控制层
 * @date 2021/4/12 11:00
 */
@Api(tags = "数据字典类型")
@Controller
@RequestMapping("/system/sysDictType")
@RequiredArgsConstructor
public class SysDictTypeController extends CrudBaseController {

    private static final String SUFFIX = "system/sysDictType";

    private final SysDictTypeService sysDictTypeService;

    /**
     * 跳转列表
     * @return 列表页面
     */
    @GetMapping("/listPage")
    public String listPage(){
        return SUFFIX + "/list";
    }

    /**
     * 跳转添加
     * @return 添加页面
     */
    @GetMapping("/addPage")
    public String addPage(){
        return SUFFIX + "/add";
    }

    /**
     * 跳转编辑
     * @param id 字典id
     * @return 编辑页面
     */
    @GetMapping("/editPage/{id}")
    public String editPage(@Valid @NotNull(message = "id能为空") @PathVariable Long id, ModelMap map){
        SysDictType sysDict = sysDictTypeService.getById(id);
        map.put("sysDictType",sysDict);
        return SUFFIX + "/edit";
    }

    /**
     * 列表请求
     * @param sysDictType 请求字典列表参数
     * @return 请求列表
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", dataType = "Integer", paramType="query"),
            @ApiImplicitParam(name = "limit", value = "每页记录数", dataType = "Integer", paramType="query")
    })
    @PreAuthorize("hasAuthority('dictType:list')")
    @GetMapping("/page")
    @ResponseBody
    public TableDataInfo page(SysDictType sysDictType) {
        startPage();
        List<SysDictType> list = sysDictTypeService.list(sysDictType);
        return getDataTable(list);
    }

    /**
     * 添加请求
     * @param sysDictType 添加字典参数
     * @return 添加状态
     */
    @CrudLog(title = "添加字典类型",businessType = BusinessType.INSERT)
    @PreAuthorize("hasAuthority('dictType:add')")
    @PostMapping("/add")
    @ResponseBody
    public Result add(@RequestBody @Validated SysDictType sysDictType, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return Result.failure(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        boolean flag = sysDictTypeService.add(sysDictType);
        return Result.success(flag);
    }

    /**
     * 编辑请求
     * @param sysDictType 编辑字典参数
     * @return 编辑状态
     */
    @CrudLog(title = "更新字典类型",businessType = BusinessType.UPDATE)
    @PreAuthorize("hasAuthority('dictType:edit')")
    @PostMapping("/edit")
    @ResponseBody
    public Result edit(@RequestBody @Validated SysDictType sysDictType, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return Result.failure(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        boolean flag = sysDictTypeService.updateById(sysDictType);
        return Result.success(flag);
    }

    /**
     * 删除请求
     * @param id 字典id
     * @return 删除状态
     */
    @CrudLog(title = "删除字典类型",businessType = BusinessType.DELETE)
    @ApiOperation(value = "删除字典类型(单个条目)")
    @PreAuthorize("hasAuthority('dictType:del')")
    @DeleteMapping(value = "/remove/{id}")
    @ResponseBody
    public Result remove(@Valid @NotNull(message = "id不能为空") @PathVariable Long id) {
        sysDictTypeService.remove(id);
        return Result.success();
    }
}
