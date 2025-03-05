package io.github.yangyouwang.module.system.model.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.metadata.BaseRowModel;
import io.github.yangyouwang.common.annotation.DictType;
import io.github.yangyouwang.common.constant.ConfigConsts;
import io.github.yangyouwang.framework.util.excel.BaseDictDataConverter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yangyouwang
 * @title: SysUserDTO
 * @projectName crud
 * @description: 用户响应
 * @date 2021/3/254:43 PM
 */
@Data
@ApiModel("用户响应")
public class SysUserVO extends BaseRowModel {

    /**
     * 账号
     */
    @ApiModelProperty(value = "账号")
    @ColumnWidth(20)
    @ExcelProperty(value = {"账号"}, index = 1)
    private String userName;
    /**
     * 昵称
     */
    @ApiModelProperty(value = "昵称")
    @ColumnWidth(20)
    @ExcelProperty(value = {"昵称"}, index = 2)
    private String nickName;
    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱")
    @ColumnWidth(20)
    @ExcelProperty(value = {"邮箱"}, index = 3)
    private String email;
    /**
     * 手机号码
     */
    @ApiModelProperty(value = "手机号码")
    @ColumnWidth(20)
    @ExcelProperty(value = {"手机号码"}, index = 4)
    private String phonenumber;
    /**
     * 用户性别（0男 1女 2未知）
     */
    @ApiModelProperty(value = "用户性别（0男 1女 2未知）")
    @ColumnWidth(20)
    @ExcelProperty(value = {"用户性别"}, index = 5, converter = BaseDictDataConverter.class)
    @DictType(key = ConfigConsts.DICT_KEY_SEX)
    private String sex;
    /**
     * 部门
     */
    @ApiModelProperty(value = "部门")
    @ColumnWidth(20)
    @ExcelProperty(value = {"部门"}, index = 6)
    private String deptName;
    /**
     * 岗位
     */
    @ApiModelProperty(value = "岗位")
    @ColumnWidth(20)
    @ExcelProperty(value = {"岗位"}, index = 7)
    private String postName;
    /**
     * 备注
     * */
    @ApiModelProperty(value = "备注")
    @ColumnWidth(20)
    @ExcelProperty(value = {"备注"}, index = 8)
    private String remark;
}
