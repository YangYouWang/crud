package io.github.yangyouwang.module.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 用户岗位关联表
 * </p>
 *
 * @author yangyouwang
 * @since 2022-09-15
 */
@Data
@TableName("sys_user_post")
@ApiModel(value="SysUserPost对象", description="用户岗位关联表")
public class SysUserPost {

    @ApiModelProperty(value = "用户外键")
    private Long userId;

    @ApiModelProperty(value = "岗位外键")
    private Long postId;
}
