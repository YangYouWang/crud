package io.github.yangyouwang.module.system.model.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.metadata.BaseRowModel;
import io.github.yangyouwang.common.annotation.DictType;
import io.github.yangyouwang.common.constant.ConfigConsts;
import io.github.yangyouwang.framework.util.excel.BaseDictDataConverter;
import io.github.yangyouwang.framework.util.excel.DateConverter;
import io.github.yangyouwang.framework.util.excel.MyStringImageConverter;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import java.util.Date;
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
     * 编号
     */
    @ColumnWidth(30)
    @ExcelProperty(value = {"编号"}, index = 0)
    private Long id;
    /**
     * 昵称
     */
    @ColumnWidth(30)
    @ExcelProperty(value = {"昵称"}, index = 1)
    private String nickName;
    /**
     * 头像
     */
    @ColumnWidth(10)
    @ExcelProperty(value = {"头像"}, index = 2, converter = MyStringImageConverter.class)
    private String avatar;
    /**
     * 账号
     */
    @ColumnWidth(30)
    @ExcelProperty(value = {"账号"}, index = 3)
    private String userName;
    /**
     * 邮箱
     */
    @ColumnWidth(30)
    @ExcelProperty(value = {"邮箱"}, index = 4)
    private String email;
    /**
     * 手机号码
     */
    @ColumnWidth(30)
    @ExcelProperty(value = {"手机号码"}, index = 5)
    private String phonenumber;
    /**
     * 用户性别（0男 1女 2未知）
     */
    @ColumnWidth(30)
    @ExcelProperty(value = {"用户性别"}, index = 6, converter = BaseDictDataConverter.class)
    @DictType(key = ConfigConsts.DICT_KEY_SEX)
    private String sex;
    /**
     * 部门
     */
    @ColumnWidth(30)
    @ExcelProperty(value = {"部门"}, index = 7)
    private String deptName;
    /**
     * 岗位
     */
    @ColumnWidth(50)
    @ExcelProperty(value = {"岗位"}, index = 8)
    private String postName;
    /**
     * 状态
     * */
    @ColumnWidth(30)
    @ExcelProperty(value = {"状态"}, index = 9,converter = BaseDictDataConverter.class)
    @DictType(key = ConfigConsts.DICT_KEY_ENABLED)
    private String enabled;
    /**
     * 备注
     * */
    @ColumnWidth(30)
    @ExcelProperty(value = {"备注"}, index = 10)
    private String remark;

    /**
     * 创建人
     *  */
    @ColumnWidth(30)
    @ExcelProperty(value = {"创建人"}, index = 11)
    private String createBy;
    /**
     * 创建时间
     *  */
    @ColumnWidth(30)
    @ExcelProperty(value = {"创建时间"}, index = 12, converter = DateConverter.class)
    private Date createTime;
}
