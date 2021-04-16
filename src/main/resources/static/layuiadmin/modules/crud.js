layui.define(['jquery'], function(exports){
    var $ = layui.jquery;
    var crud = {
        /**
         * 获取XmSelectTree id列表
         * @param select XmSelect
         * @returns {*} id列表
         */
        getXmSelectTreeIds: function (select) {
            var value = select.getValue();
            var ids = value.map(x => {return x.id});
            if (ids == 0) {
                layer.msg('请选择节点');
                return;
            }
            return ids;
        },
        /**
         * 获取字典
         */
        getDictValue: function(dictTypeKey,dictValueKey) {
            let data = [];
            // 获取字典
            $.ajax({
                type: 'get',
                url:  '/sysDict/getDictValues/' + dictTypeKey,
                contentType:'application/json;charset=UTF-8',
                dataType: 'json',
                async : false,
                success: function(result) {
                    if (result.code === 200) {
                        data = result.data;
                    }
                }
            });
            for(let i = 0; i < data.length; i++) {
                if (data[i].dictValueKey === dictValueKey) {
                    return data[i].dictValueName;
                }
            }
            return "未知";
        }
    };
    exports('crud', crud);
});