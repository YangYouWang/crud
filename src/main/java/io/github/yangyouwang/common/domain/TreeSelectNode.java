package io.github.yangyouwang.common.domain;

import io.github.yangyouwang.framework.util.converter.Treeable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author yangyouwang
 * @title: TreeSelectNode
 * @projectName crud
 * @description: 节点类（必须继承Treeable）
 * @date 2021/3/2910:47 AM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TreeSelectNode extends XmSelectNode implements Treeable {
    /**
     * id
     */
    private Long id;
    /**
     * 父id
     */
    private Long parentId;
    /**
     * 名称
     */
    private String name;
    /**
     * 是否选中
     */
    private Boolean checked = false;
    /**
     * 是否打开
     */
    private Boolean open = true;
    /**
     * 节点
     */
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
