package io.github.yangyouwang.module.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.github.yangyouwang.common.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author zhixin.yao
 * @version 1.0
 * @description: 数据字典类型
 * @date 2021/4/12 11:27
 */
@Data
@TableName("sys_dict_type")
@ApiModel(value="SysDictType对象", description="数据字典类型表")
public class SysDictType extends BaseEntity {
    /**
     * 类型key
     */
    @ApiModelProperty(value = "类型key")
    private String dictKey;
    /**
     * 类型名称
     */
    @ApiModelProperty(value = "类型名称")
    private String dictName;
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
    /**
     * 字典类型与字典项 一对多
     */
    @ApiModelProperty(value = "字典类型与字典项")
    @TableField(exist = false)
    private List<SysDictValue> dictValues;
}
