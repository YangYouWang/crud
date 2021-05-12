package io.github.yangyouwang.crud.system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author yangyouwang
 * @title: SysToolController
 * @projectName crud
 * @description: 系统工具控制层
 * @date 2021/3/2510:41 AM
 */
@Controller
@RequestMapping("/sysTool")
public class SysToolController {

    /**
     * 跳转到swagger页面
     * @return swagger页面
     */
    @GetMapping("swagger")
    public String swagger(){
        return "redirect:/swagger-ui.html";
    }

    /**
     * 跳转到druid页面
     * @return druid页面
     */
    @GetMapping("druid")
    public String druid(){
        return "redirect:/druid/index.html";
    }
}
