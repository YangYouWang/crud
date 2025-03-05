package io.github.yangyouwang.module.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.github.yangyouwang.common.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 岗位表
 * </p>
 *
 * @author yangyouwang
 * @since 2022-09-15
 */
@Data
@TableName("sys_post")
@ApiModel(value="SysPost对象", description="岗位表")
public class SysPost extends BaseEntity {

    @ApiModelProperty(value = "岗位编码")
    private String postCode;

    @ApiModelProperty(value = "岗位名称")
    private String postName;

    @ApiModelProperty(value = "显示顺序")
    private Integer orderNum;

    @ApiModelProperty(value = "是否启用 Y 启用 N 禁用")
    private String enabled;


}
