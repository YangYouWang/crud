package io.github.yangyouwang.module.system.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Description: 用户参数DTO<br/>
 * date: 2022/12/24 13:59<br/>
 *
 * @author yangyouwang<br />
 * @version v1.0
 * @since JDK 1.8
 */
@Data
@ApiModel("用户参数DTO")
public class UserParamDTO {
    /**
     * 昵称
     */
    @ApiModelProperty(value = "昵称")
    private String nickName;
    /**
     * 手机号码
     */
    @ApiModelProperty(value = "手机号码")
    private String phonenumber;
    /**
     * 部门ids
     */
    @ApiModelProperty(value = "部门数组")
    private List deptIds;
}
