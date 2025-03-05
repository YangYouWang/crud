package io.github.yangyouwang.module.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.github.yangyouwang.common.annotation.CrudLog;
import io.github.yangyouwang.common.base.CrudController;
import io.github.yangyouwang.common.domain.Result;
import io.github.yangyouwang.common.domain.TreeSelectNode;
import io.github.yangyouwang.common.enums.BusinessType;
import io.github.yangyouwang.module.system.entity.SysMenu;
import io.github.yangyouwang.module.system.service.SysMenuService;
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
 * @author yangyouwang
 * @title: SysMenuController
 * @projectName crud
 * @description: 菜单管理
 * @date 2021/3/2610:22 PM
 */
@Api(tags = "菜单表")
@Controller
@RequestMapping("/system/sysMenu")
@RequiredArgsConstructor
public class SysMenuController extends CrudController {

    private static final String SUFFIX = "system/sysMenu";

    private final SysMenuService sysMenuService;

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
    public String addPage(SysMenu sysMenu,ModelMap map) {
        map.put("parentId",sysMenu.getId());
        map.put("parentName",sysMenu.getMenuName());
        return SUFFIX + "/add";
    }

    /**
     * 跳转编辑
     * @param id 菜单id
     * @return 编辑页面
     */
    @GetMapping("/editPage/{id}")
    public String editPage(@Valid @NotNull(message = "id不能为空") @PathVariable Long id, ModelMap map){
        SysMenu sysMenu = sysMenuService.info(id);
        map.put("sysMenu",sysMenu);
        return SUFFIX + "/edit";
    }

    /**
     * 列表请求
     * @return 请求列表
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", dataType = "Integer", paramType="query"),
            @ApiImplicitParam(name = "limit", value = "每页记录数", dataType = "Integer", paramType="query")
    })
    @PreAuthorize("hasAuthority('menu:list')")
    @GetMapping("/page")
    @ResponseBody
    public Result page() {
        List<SysMenu> list = sysMenuService.list(new LambdaQueryWrapper<SysMenu>()
                .orderByAsc(SysMenu::getParentId,SysMenu::getOrderNum));
        return Result.success(list);
    }
    /**
     * 添加请求
     * @param sysMenu 添加菜单对象
     * @return 添加状态
     */
    @CrudLog(title = "添加菜单",businessType = BusinessType.INSERT)
    @PreAuthorize("hasAuthority('menu:add')")
    @PostMapping("/add")
    @ResponseBody
    public Result add(@RequestBody @Validated SysMenu sysMenu, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return Result.failure(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        boolean flag = sysMenuService.save(sysMenu);
        return Result.success(flag);
    }

    /**
     * 编辑请求
     * @param sysMenu 编辑菜单对象
     * @return 编辑状态
     */
    @CrudLog(title = "更新菜单",businessType = BusinessType.UPDATE)
    @PreAuthorize("hasAuthority('menu:edit')")
    @PostMapping("/edit")
    @ResponseBody
    public Result edit(@RequestBody @Validated SysMenu sysMenu, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return Result.failure(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        boolean flag = sysMenuService.updateById(sysMenu);
        return Result.success(flag);
    }

    /**
     * 删除请求
     * @param id 删除id
     * @return 删除状态
     */
    @CrudLog(title = "删除菜单",businessType = BusinessType.DELETE)
    @ApiOperation(value = "删除菜单(单个条目)")
    @PreAuthorize("hasAuthority('menu:del')")
    @DeleteMapping(value = "/remove/{id}")
    @ResponseBody
    public Result remove(@Valid @NotNull(message = "id不能为空") @PathVariable Long id) {
        sysMenuService.remove(id);
        return Result.success();
    }

    /**
     * 查询菜单树结构
     * @return 菜单树结构
     */
    @GetMapping("/treeSelect")
    @ResponseBody
    public List<TreeSelectNode> treeSelect() {
        return sysMenuService.treeSelect();
    }

    /**
     * 根据菜单ids查询选中菜单树结构
     * @param ids 菜单id列表
     * @return 菜单树结构
     */
    @GetMapping("/xmSelect")
    @ResponseBody
    public List<TreeSelectNode> xmSelect(@RequestParam(value = "ids",required = false) String ids) {
        return sysMenuService.xmSelect(ids);
    }
}
