package io.github.yangyouwang.module.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.github.yangyouwang.common.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @author zhixin.yao
 * @version 1.0
 * @description: 数据字典值
 * @date 2021/4/12 11:27
 */
@Data
@TableName("sys_dict_value")
@ApiModel(value="SysDictValue对象", description="数据字典项表")
public class SysDictValue extends BaseEntity {
    /**
     * 字典类型id
     */
    @ApiModelProperty(value = "字典类型外键")
    private Long dictTypeId;

    /**
     * 字典值key
     */
    @ApiModelProperty(value = "字典值别名")
    private String dictValueKey;

    /**
     * 字典值名称
     */
    @ApiModelProperty(value = "字典值名称")
    private String dictValueName;

    /**
     * 显示顺序
     */
    @ApiModelProperty(value = "显示顺序")
    private Integer orderNum;

    /**
     * 启用
     */
    @ApiModelProperty(value = "启用")
    private String enabled;
}
