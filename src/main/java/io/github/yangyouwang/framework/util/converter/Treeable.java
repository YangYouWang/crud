package io.github.yangyouwang.framework.util.converter;

import java.util.List;

/**
 * 节点类继承接口，节点类必须继承此类
 * @author yangyouwang
 */
public interface Treeable<T> {

    /**
     * 获取存入map key
     * @return
     */
    Object getMapKey();

    /**
     * 获取孩子节点的key值
     * @return
     */
    Object getChildrenKey();

    /**
     * 根节点key值
     * @return
     */
    Object getRootKey();

    /**
     * 子节点赋值
     * @param children
     */
    void setChildren(List<T> children);

}