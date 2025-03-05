package io.github.yangyouwang.module.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.github.yangyouwang.common.annotation.CrudLog;
import io.github.yangyouwang.common.base.controller.BaseController;
import io.github.yangyouwang.common.base.domain.Result;
import io.github.yangyouwang.common.base.domain.TableDataInfo;
import io.github.yangyouwang.common.base.domain.XmSelectNode;
import io.github.yangyouwang.common.enums.BusinessType;
import io.github.yangyouwang.module.system.entity.SysRole;
import io.github.yangyouwang.module.system.service.SysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
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
 * @author yangyouwang
 * @title: SysRoleController
 * @projectName crud
 * @description: 角色控制层
 * @date 2021/3/2112:38 AM
 */
@Api(tags = "角色表")
@Controller
@RequestMapping("/system/sysRole")
@RequiredArgsConstructor
public class SysRoleController extends BaseController {

    private static final String SUFFIX = "system/sysRole";

    private final SysRoleService sysRoleService;

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
     * @param id 角色id
     * @return 编辑页面
     */
    @GetMapping("/editPage/{id}")
    public String editPage(@Valid @NotNull(message = "id不能为空") @PathVariable Long id, ModelMap map){
        SysRole sysRole = sysRoleService.detail(id);
        map.put("sysRole",sysRole);
        return SUFFIX + "/edit";
    }


    /**
     * 列表请求
     * @param sysRole 请求角色列表对象
     * @return 请求列表
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", dataType = "Integer", paramType="query"),
            @ApiImplicitParam(name = "limit", value = "每页记录数", dataType = "Integer", paramType="query")
    })
    @PreAuthorize("hasAuthority('role:list')")
    @GetMapping("/page")
    @ResponseBody
    public TableDataInfo page(@Validated SysRole sysRole) {
        startPage();
        List<SysRole> list = sysRoleService.list(new LambdaQueryWrapper<SysRole>()
                .like(StringUtils.isNotBlank(sysRole.getRoleName()), SysRole::getRoleName , sysRole.getRoleName())
                .like(StringUtils.isNotBlank(sysRole.getRoleKey()), SysRole::getRoleKey , sysRole.getRoleKey())
                .orderByDesc(SysRole::getCreateTime));
        return getDataTable(list);
    }

    /**
     * 添加请求
     * @param sysRole 添加角色对象
     * @return 添加状态
     */
    @CrudLog(title = "添加角色",businessType = BusinessType.INSERT)
    @PreAuthorize("hasAuthority('role:add')")
    @PostMapping("/add")
    @ResponseBody
    public Result add(@RequestBody @Validated SysRole sysRole, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return Result.failure(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        sysRoleService.add(sysRole);
        return Result.success();
    }

    /**
     * 编辑请求
     * @param sysRole 编辑角色对象
     * @return 编辑状态
     */
    @CrudLog(title = "更新角色",businessType = BusinessType.UPDATE)
    @PreAuthorize("hasAuthority('role:edit')")
    @PostMapping("/edit")
    @ResponseBody
    public Result edit(@RequestBody @Validated SysRole sysRole, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return Result.failure(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        sysRoleService.edit(sysRole);
        return Result.success();
    }

    /**
     * 删除角色
     * @param id 角色id
     * @return 删除状态
     */
    @ApiOperation(value = "删除角色(单个条目)")
    @CrudLog(title = "删除角色",businessType = BusinessType.DELETE)
    @PreAuthorize("hasAuthority('role:del')")
    @DeleteMapping("/remove/{id}")
    @ResponseBody
    public Result remove(@Valid @NotNull(message = "id不能为空") @PathVariable Long id) {
        sysRoleService.remove(id);
        return Result.success();
    }

    /**
     * 删除角色(多个条目)
     * @param ids 角色ids
     * @return 删除状态
     */
    @ApiOperation(value = "删除角色(多个条目)")
    @PostMapping(value = "/removes")
    @PreAuthorize("hasAuthority('role:del')")
    @ResponseBody
    @CrudLog(title = "删除角色",businessType = BusinessType.DELETE)
    public Result removes(@RequestBody @Valid List<Long> ids) {
        sysRoleService.removes(ids);
        return Result.success();
    }

    /**
     * 根据角色ids查询选中角色列表
     * @param ids 角色ids
     * @return 角色列表
     */
    @GetMapping("/xmSelect")
    @ResponseBody
    public Result xmSelect(@RequestParam(value = "ids",required = false) String ids) {
        List<XmSelectNode> xmSelectNodes = sysRoleService.xmSelect(ids);
        return Result.success(xmSelectNodes);
    }
}
