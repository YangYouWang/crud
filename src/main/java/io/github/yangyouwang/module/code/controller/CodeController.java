package io.github.yangyouwang.module.code.controller;

import io.github.yangyouwang.common.base.domain.Result;
import io.github.yangyouwang.module.code.model.dto.BuildDTO;
import io.github.yangyouwang.module.code.model.vo.FieldVO;
import io.github.yangyouwang.module.code.service.CodeService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

/**
 * 生成代码控制层
 * @author yangyouwang
 */
@Api(tags = "生成代码")
@Controller
@RequestMapping("/code")
@RequiredArgsConstructor
public class CodeController {

    private static final String SUFFIX = "code";

    @Resource
    private CodeService codeService;

    /**
     * 跳转代码生成页面
     * @return 代码生成页面
     */
    @GetMapping("/index")
    public String indexPage(ModelMap map) {
        return SUFFIX + "/index";
    }

    /**
     * 查询表名称列表
     * @return 表名称列表
     */
    @GetMapping("/table")
    @ResponseBody
    public Result table() {
        List<String> tables = codeService.selectTable();
        return Result.success(tables);
    }

    /**
     * 查询字段列表
     * @return 字段列表
     */
    @GetMapping("/field")
    @ResponseBody
    public Result field(String tableName) {
        List<FieldVO> fields = codeService.selectField(tableName);
        return Result.success(fields);
    }

    /**
     * 代码生成接口
     * @param build 代码生成
     * @return 结果
     */
    @PostMapping("/build")
    @ResponseBody
    public Result build(@RequestBody @Validated BuildDTO build, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            throw new RuntimeException(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        codeService.build(build);
        return Result.success();
    }
}
