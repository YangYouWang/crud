package io.github.yangyouwang.crud.api.controller;

import io.github.yangyouwang.common.annotation.ApiVersion;
import io.github.yangyouwang.common.annotation.PassToken;
import io.github.yangyouwang.common.annotation.ResponseResultBody;
import io.github.yangyouwang.common.constant.ApiVersionConstant;
import io.github.yangyouwang.common.domain.Result;
import io.github.yangyouwang.crud.api.model.dto.MobileCodeDTO;
import io.github.yangyouwang.crud.api.service.ApiIndexService;
import io.github.yangyouwang.crud.api.service.ApiSmsCodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

/**
 * Description: 首页控制层 <br/>
 * date: 2022/8/12 12:59<br/>
 *
 * @author yangyouwang<br />
 * @version v1.0
 * @since JDK 1.8
 */
@RestController
@RequestMapping("/api/{version}/index")
@Api(tags = "首页相关接口")
@RequiredArgsConstructor
@ResponseResultBody
public class ApiIndexController {

    private final ApiIndexService apiIndexService;

    private final ApiSmsCodeService apiSmsCodeService;

    /**
     * 通知公告接口
     * @return 响应
     */
    @ApiVersion(value = ApiVersionConstant.API_V1,group = ApiVersionConstant.SWAGGER_API_V1)
    @GetMapping("/get_notice")
    @ApiOperation(value="通知公告", notes="通知公告")
    @PassToken
    public Result getNotice() {
        return Result.success(apiIndexService.getNotice());
    }

    /**
     * 发送手机验证码
     */
    @ApiVersion(value = ApiVersionConstant.API_V1,group = ApiVersionConstant.SWAGGER_API_V1)
    @PostMapping("/mobile_code")
    @ApiOperation(value="发送手机验证码", notes="发送手机验证码")
    @PassToken
    public Result mobileCode(@Valid @RequestBody MobileCodeDTO mobileCodeDTO,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new RuntimeException(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        String mobile = mobileCodeDTO.getMobile();
        boolean flag = apiSmsCodeService.sendMobileCode(mobile);
        return Result.success(flag);
    }
}
