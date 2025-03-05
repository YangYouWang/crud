package io.github.yangyouwang.module.system.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Description: 重置密码DTO <br/>
 * date: 2022/8/24 21:25<br/>
 *
 * @author yangyouwang<br />
 * @version v1.0
 * @since JDK 1.8
 */
@Data
@ApiModel("重置密码DTO")
public class ResetPassDTO {

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键")
    @NotNull(message = "id不能为空")
    private Long id;
}
