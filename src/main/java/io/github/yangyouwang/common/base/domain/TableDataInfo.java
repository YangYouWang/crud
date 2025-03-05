package io.github.yangyouwang.common.base.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Description: 分页数据响应<br/>
 * date: 2022/8/1 18:51<br/>
 *
 * @author yangyouwang<br />
 * @version v1.0
 * @since JDK 1.8
 */
@Data
@ApiModel(value="分页数据", description="分页数据")
public class TableDataInfo {

    /** 总记录数 */
    @ApiModelProperty(value = "总记录数")
    private long count;

    /** 列表数据 */
    @ApiModelProperty(value = "列表数据")
    private List<?> data;

    /** 消息状态码 */
    @ApiModelProperty(value = "消息状态码")
    private int code;
}
