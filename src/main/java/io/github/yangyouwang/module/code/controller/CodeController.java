package io.github.yangyouwang.module.code.controller;

import io.github.yangyouwang.common.domain.Result;
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
     * 查询表列表
     * @return 表
     */
    @GetMapping("/table")
    @ResponseBody
    public List<String> table() {
        return codeService.selectTable();
    }

    /**
     * 查询字段列表
     * @return 字段列表
     */
    @GetMapping("/field")
    @ResponseBody
    public List<FieldVO> field(String tableName) {
        return codeService.selectField(tableName);
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
        return Result.success("生成代码在项目工程中");
    }
}
