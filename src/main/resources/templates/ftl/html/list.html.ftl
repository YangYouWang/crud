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
                    <button type="button" class="layui-btn layui-btn-primary" data-type="resetForm">重置</button>
                    <button type="button" sec:authorize="hasAuthority('${table.entityPath}:add')" class="layui-btn layuiadmin-btn-useradmin" data-type="addView">添加</button>
                </div>
            </div>
        </form>
        <div class="layui-card-body">
            <table id="${table.entityPath}Table" lay-filter="${table.entityPath}Table"></table>
            <script type="text/html" id="tableBar">
                <a sec:authorize="hasAuthority('${table.entityPath}:edit')" class="layui-btn layui-btn-normal layui-btn-xs" lay-event="edit"><i class="layui-icon layui-icon-edit"></i>编辑</a>
                <a sec:authorize="hasAuthority('${table.entityPath}:del')" class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del"><i class="layui-icon layui-icon-delete"></i>删除</a>
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
    }).use(['index', 'useradmin', 'table'], function() {
        let $ = layui.$,
            form = layui.form,
            table = layui.table;
        //监听搜索
        form.on('submit(search)', function(data){
            let field = data.field;
            //执行重载
            table.reload('${table.entityPath}Table', {
                where: field
            });
        });
        // 查询列表接口
        table.render({
            elem: '#${table.entityPath}Table'
            ,height: 'full-110'
            ,url:  ctx + '<#if package.ModuleName??>/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>/page'
            ,page: true //开启分页,
            ,limit: 10 // 多少条
            ,request: {
                pageName: 'pageNum', //页码的参数名称，默认：page
                limitName: 'pageSize' //每页数据量的参数名，默认：limit
            }
            ,parseData: function (result) {
                return {
                    "code": result.code,
                    "count": result.data.total,
                    "data": result.data.records
                }
            }
            ,response: {
                statusCode: 200 //成功的状态码，默认：0
            }
            ,cols: [[ //表头
                {field: 'id', title: 'ID', sort: true, width: 80, fixed: 'left'},
            <#list table.fields as field>
                {field: '${field.propertyName}', title: '${field.comment}'},
            </#list>
                {toolbar: '#tableBar', title: '操作', width: 300, align:'center',fixed: 'right'}
            ]]
        });
        table.on('tool(${table.entityPath}Table)', function(obj){
            let data = obj.data //获得当前行数据
                ,layEvent = obj.event;
            if(layEvent === 'edit'){
                active.editView(data.id);
            } else if(layEvent === 'del'){
                active.del(data.id);
            }
        });
        $('.layui-btn').on('click', function(){
            let type = $(this).data('type');
            active[type] && active[type].call(this);
        });
        /* 触发弹层 */
        let active = {
            /**
             * 重置
             */
            resetForm: function () {
                $(".layui-form")[0].reset();
                form.render();
                table.reload('${table.entityPath}Table', {
                    where: {
                     <#list table.fields as field>
                        ${field.propertyName}: ""<#if field_has_next>,</#if>
                     </#list>
                    }
                });
            },
            /**
             * 添加
             * */
            addView: function(){
                layer.open({
                    type: 2
                    ,shade: 0.3
                    ,title: "添加${table.comment!}"
                    ,content: ctx + '<#if package.ModuleName??>/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>/addPage'
                    ,maxmin: true
                    ,area: ['50%', '80%']
                    ,btn: ['确定', '取消']
                    ,yes: function(index, layero){
                        let submit = layero.find('iframe').contents().find('#save-submit');
                        submit.trigger('click');
                    }
                });
            },
            /**
             * 编辑
             * @param id id
             * */
            editView: function(id){
                layer.open({
                    type: 2
                    ,shade: 0.3
                    ,title: '修改${table.comment!}'
                    ,content: ctx + '<#if package.ModuleName??>/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>/editPage/' + id
                    ,maxmin: true
                    ,area: ['50%', '80%']
                    ,btn: ['确定', '取消']
                    ,yes: function(index, layero){
                        let submit = layero.find('iframe').contents().find('#save-submit');
                        submit.trigger('click');
                    }
                });
            },
            /**
             * 删除
             * @param id id
             */
            del: function(id){
                layer.confirm('真的删除行么', function(index){
                    //向服务端发送删除指令
                    $.ajax({
                        type: 'DELETE',
                        url:  ctx + '<#if package.ModuleName??>/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>/remove/' + id,
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
            }
        }
    });
</script>
</body>
</html>