package io.github.yangyouwang.framework.util.converter;

import java.util.List;

/**
 * ListToTree 一个函数式接口
 * @author yangyouwang
 */
public interface ListToTree<T extends Treeable> {

    /**
     * list转化tree结构
     * @param list 原始list
     * @return 树结构list
     */
    List<T> toTree(List<T> list);
}