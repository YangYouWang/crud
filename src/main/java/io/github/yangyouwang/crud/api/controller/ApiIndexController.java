package io.github.yangyouwang.crud.api.controller;

import io.github.yangyouwang.common.annotation.ApiVersion;
import io.github.yangyouwang.common.annotation.PassToken;
import io.github.yangyouwang.common.annotation.ResponseResultBody;
import io.github.yangyouwang.common.constant.ApiVersionConstant;
import io.github.yangyouwang.crud.api.model.IndexVO;
import io.github.yangyouwang.crud.api.service.ApiIndexService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    /**
     * 首页接口
     * @return 响应
     */
    @ApiVersion(value = ApiVersionConstant.API_V1,group = ApiVersionConstant.SWAGGER_API_V1)
    @PostMapping
    @ApiOperation(value="首页", notes="首页")
    @PassToken
    public IndexVO index() {
        return apiIndexService.getIndexData();
    }
}
