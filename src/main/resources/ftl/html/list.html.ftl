<!DOCTYPE html>
<html lang="zh" xmlns:th="http://www.thymeleaf.org" xmlns:sec="http://www.thymeleaf.org/thymeleaf-extras-springsecurity4">
<head>
    <title>${table.comment!}</title>
    <div th:replace="common/link::header"></div>
</head>
<body>
<div class="layui-fluid">
    <div class="layui-card">
        <form class="layui-form layui-card-header layuiadmin-card-header-auto">
            <div class="layui-form-item">
            <#list table.fields as field>
                <div class="layui-inline">
                    <label class="layui-form-label">${field.comment}</label>
                    <div class="layui-input-block">
                        <input type="text" name="${field.propertyName}" placeholder="请输入${field.comment}" class="layui-input">
                    </div>
                </div>
            </#list>
                <div class="layui-inline">
                    <button type="button" class="layui-btn layuiadmin-btn-useradmin" lay-submit lay-filter="search">
                        <i class="layui-icon layui-icon-search layuiadmin-button-btn"></i>
                    </button>
                    <button type="reset" class="layui-btn layui-btn-primary" lay-submit lay-filter="reset">重置</button>
                </div>
            </div>
        </form>
        <div class="layui-card-body">
            <table id="${table.entityPath}Table" lay-filter="${table.entityPath}Table"></table>
            <script type="text/html" id="toolbar">
                <div class="layui-btn-container">
                    <a sec:authorize="hasAuthority('${table.entityPath}:add')" class="layui-btn layui-btn-xs" lay-event="add"><i class="layui-icon layui-icon-add-1"></i>添加</a>
                    <a sec:authorize="hasAuthority('${table.entityPath}:del')" class="layui-btn layui-btn-danger layui-btn-xs" lay-event="removes"><i class="layui-icon layui-icon-delete"></i>删除</a>
                </div>
            </script>
            <script type="text/html" id="tableBar">
                <a sec:authorize="hasAuthority('${table.entityPath}:edit')" class="layui-btn layui-btn-normal layui-btn-xs" lay-event="edit"><i class="layui-icon layui-icon-edit"></i>编辑</a>
                <a sec:authorize="hasAuthority('${table.entityPath}:del')" class="layui-btn layui-btn-danger layui-btn-xs" lay-event="remove"><i class="layui-icon layui-icon-delete"></i>删除</a>
            </script>
        </div>
    </div>
</div>
<div th:replace="common/script::footer"></div>
<script th:inline="javascript">
    layui.config({
        base: '/static/layuiadmin/' //静态资源所在路径
    }).extend({
        index: 'lib/index' //主入口模块
    }).use(['index', 'useradmin', 'table', 'crud'], function() {
        let $ = layui.$,
            form = layui.form,
            table = layui.table,
            crud = layui.crud;
        //监听搜索
        form.on('submit(search)', function(data) {
            let field = data.field;
            //执行重载
            table.reload('${table.entityPath}Table',{
                where: field
            });
        });
        //监听重置
        form.on('submit(reset)', function(data) {
            Object.keys(data.field).forEach(key => (data.field[key] = ''));
            table.reload('${table.entityPath}Table',{
                where: data.field
            });
        });
        // 查询列表接口
        table.render({
            elem: '#${table.entityPath}Table'
            ,toolbar: '#toolbar'
            ,height: 'full-110'
            ,url:  ctx + '<#if package.ModuleName??>/${package.ModuleName}</#if>/${table.entityPath}/page'
            ,cellMinWidth: 80
            ,page: true //开启分页
            ,cols: [[ //表头
                {type: 'checkbox', fixed: 'left'},
                {field: 'id', title: 'ID', hide:true},
            <#list table.fields as field>
                {field: '${field.propertyName}', title: '${field.comment}'},
            </#list>
                {toolbar: '#tableBar', title: '操作', width: 300, align:'center',fixed: 'right'}
            ]]
        });
        //头工具栏事件
        table.on('toolbar(${table.entityPath}Table)', function(obj) {
            let checkStatus = table.checkStatus(obj.config.id)
                ,data = checkStatus.data;
            switch(obj.event) {
                case 'add':
                    active.addView();
                    break;
                case 'removes':
                    if (data.length === 0) {
                        layer.msg('请选择需要删除的行')
                    } else {
                        active.removes(data);
                    }
                    break;
            }
        });
        //监听行工具事件
        table.on('tool(${table.entityPath}Table)', function(obj) {
            let data = obj.data //获得当前行数据
                ,layEvent = obj.event;
            if(layEvent === 'edit') {
                active.editView(data.id);
            } else if(layEvent === 'remove') {
                active.remove(data.id);
            }
        });
        /* 触发弹层 */
        let active = {
            /**
             * 添加
             * */
            addView: function() {
                layer.open({
                    type: 2
                    ,shade: 0.3
                    ,title: "添加${table.comment!}"
                    ,content: ctx + '<#if package.ModuleName??>/${package.ModuleName}</#if>/${table.entityPath}/addPage'
                    ,maxmin: true
                    ,area: ['100%', '100%']
                    ,btn: ['确定', '取消']
                    ,yes: function(index, layero) {
                        let submit = layero.find('iframe').contents().find('#save-submit');
                        submit.trigger('click');
                    }
                });
            },
            /**
             * 编辑
             * @param id id
             * */
            editView: function(id) {
                layer.open({
                    type: 2
                    ,shade: 0.3
                    ,title: '修改${table.comment!}'
                    ,content: ctx + '<#if package.ModuleName??>/${package.ModuleName}</#if>/${table.entityPath}/editPage/' + id
                    ,maxmin: true
                    ,area: ['100%', '100%']
                    ,btn: ['确定', '取消']
                    ,yes: function(index, layero) {
                        let submit = layero.find('iframe').contents().find('#save-submit');
                        submit.trigger('click');
                    }
                });
            },
            /**
             * 删除单个条目
             * @param id id
             */
            remove: function(id) {
                layer.confirm('真的删除行么', function(index) {
                    //向服务端发送删除指令
                    $.ajax({
                        type: 'DELETE',
                        url:  ctx + '<#if package.ModuleName??>/${package.ModuleName}</#if>/${table.entityPath}/remove/' + id,
                        contentType:'application/json;charset=UTF-8',
                        dataType: 'json',
                        success: function(result) {
                            layer.msg(result.message);
                            if (result.code === 200) {
                                layer.close(index);
                                table.reload('${table.entityPath}Table');
                            }
                        }
                    });
                });
            },
            /**
             * 批量删除
             * @param obj 对象
             */
            removes: function(obj) {
                let ids = fun.objToArrIds(obj);
                layer.confirm('真的删除行么', function(index) {
                    //向服务端发送删除指令
                    $.ajax({
                        type: 'POST',
                        url:  ctx + '<#if package.ModuleName??>/${package.ModuleName}</#if>/${table.entityPath}/removes',
                        data: JSON.stringify(ids),
                        contentType:'application/json;charset=UTF-8',
                        dataType: 'json',
                        success: function(result) {
                            layer.msg(result.message);
                            if (result.code === 200) {
                                layer.close(index);
                                table.reload('${table.entityPath}Table');
                            }
                        }
                    });
                });
            },
        }
    });
</script>
</body>
</html>
