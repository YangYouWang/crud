package io.github.yangyouwang.framework.util.converter.impl;
import io.github.yangyouwang.framework.util.converter.ListToTree;
import io.github.yangyouwang.framework.util.converter.Treeable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * list转tree的实现（有两处判断是否为空的，TODO 根据自己的业务自行实现）
 * @author yangyouwang
 */
public class ListToTreeImpl implements ListToTree {

    @Override
    public List toTree(List list) {
        // TODO list 空判断，做处理
        Map<Object, List> map = new HashMap<>(16);
        Object rootKye = null;
        for(Object o : list) {
            Treeable t = (Treeable) o;
            Object key = t.getMapKey();
            if(map.containsKey(key)) {
                map.get(key).add(o);
            } else {
                List mapValue = new ArrayList();
                mapValue.add(o);
                map.put(key, mapValue);
            }
            // 获取根节点key值
            if(rootKye == null) {
                rootKye = t.getRootKey();
            }
        }
        List tree = map.get(rootKye);
        recursionToTree(tree, map);
        return tree;
    }

    private void recursionToTree(List list, Map<Object, List> map){
        // TODO list 空判断，做处理
        for(Object o : list){
            Treeable t = (Treeable) o;
            Object key = t.getChildrenKey();
            if(map.containsKey(key)) {
                List children = map.get(key);
                t.setChildren(children);
                recursionToTree(children, map);
            }
        }
    }

}