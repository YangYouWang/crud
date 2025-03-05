package io.github.yangyouwang.common.base.domain;

import io.github.yangyouwang.framework.util.converter.Treeable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author yangyouwang
 * @title: TreeSelectNode
 * @projectName crud
 * @description: 树结构节点类
 * @date 2021/3/2910:47 AM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value="树结构节点", description="树结构节点")
public class TreeSelectNode extends XmSelectNode implements Treeable {
    /**
     * id
     */
    @ApiModelProperty(value = "主键")
    private Long id;
    /**
     * 父id
     */
    @ApiModelProperty(value = "上级主键")
    private Long parentId;
    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;
    /**
     * 是否选中
     */
    @ApiModelProperty(value = "是否选中")
    private Boolean checked = false;
    /**
     * 是否打开
     */
    @ApiModelProperty(value = "是否打开")
    private Boolean open = true;
    /**
     * 节点
     */
    @ApiModelProperty(value = "下级节点")
    private List<TreeSelectNode> children;

    @Override
    public Long getMapKey() {
        return parentId;
    }

    @Override
    public Long getChildrenKey() {
        return id;
    }

    @Override
    public Long getRootKey() {
        return 0L;
    }

    @Override
    public void setChildren(List children) {
        this.children = children;
    }
}
