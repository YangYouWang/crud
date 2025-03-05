package io.github.yangyouwang.module.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.github.yangyouwang.common.base.CrudBaseController;
import io.github.yangyouwang.common.constant.ConfigConsts;
import io.github.yangyouwang.framework.security.util.SecurityUtils;
import io.github.yangyouwang.framework.util.StringUtil;
import io.github.yangyouwang.module.system.entity.SysUser;
import io.github.yangyouwang.module.system.model.vo.SysMenuVO;
import io.github.yangyouwang.module.system.model.vo.SysUserVO;
import io.github.yangyouwang.module.system.service.SysDictTypeService;
import io.github.yangyouwang.module.system.service.SysMenuService;
import io.github.yangyouwang.module.system.service.SysUserService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author yangyouwang
 * @title: SysLoginController
 * @projectName crud
 * @description: 首页控制层
 * @date 2021/3/216:40 PM
 */
@Api(tags = "首页")
@Controller
@RequiredArgsConstructor
public class SysIndexController extends CrudBaseController {

    private final SysMenuService sysMenuService;

    private final SysUserService sysUserService;

    private final SysDictTypeService sysDictTypeService;

    /**
     * 跳转到首页页面
     * @return 首页页面
     */
    @GetMapping("/")
    public String indexPage(ModelMap map) {
        // 缓存字典
        sysDictTypeService.cacheDict();
        // 用户信息
        String userName = SecurityUtils.getUserName();
        SysUser sysUser = sysUserService.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUserName, userName));
        SysUserVO sysUserVO = new SysUserVO();
        BeanUtils.copyProperties(sysUser,sysUserVO);
        map.put("sysUser",sysUserVO);
        // 菜单权限
        List<SysMenuVO> sysMenus = sysMenuService.selectMenusByUser(sysUser.getId(),userName);
        map.put("sysMenus",sysMenus);
        return "index";
    }

    /**
     * 跳转到登陆页面
     * @return 登陆页面
     */
    @GetMapping("/loginPage")
    public String loginPage(){
        return "login";
    }

    /**
     * 跳转到main页面
     * @return main页面
     */
    @GetMapping("/mainPage")
    public String mainPage(){
        return "main";
    }

    /**
     * 验证码
     */
    @RequestMapping("/getImgCode")
    public void getImgCode(HttpServletRequest request, HttpServletResponse response) {
        int width = 80;
        int height = 30;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        //获取画笔
        Graphics graphics = image.getGraphics();
        //设置画笔颜色为白色
        graphics.setColor(Color.white);
        //填充图片
        graphics.fillRect(0, 0, width, height);
        //设置画笔颜色为黑色
        graphics.setColor(Color.black);
        //设置字体的小大
        graphics.setFont(new Font("黑体", Font.BOLD, 24));
        //产生4个随机验证码
        String checkCode = StringUtil.getCheckCode();
        //将验证码放入HttpSession中
        HttpSession session = request.getSession();
        session.setAttribute(ConfigConsts.IMAGE_CODE_SESSION, checkCode);
        //向图片上写入验证码
        graphics.drawString(checkCode, 15, 25);
        //将内存中的图片输出到浏览器
        try {
            response.setContentType("image/png");
            ImageIO.write(image, "PNG", response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
