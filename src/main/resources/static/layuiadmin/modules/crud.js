layui.extend({
    xmSelect: 'xm-select'
}).define(['jquery','cookie','upload','xmSelect','treetable'], function(exports){
    let $ = layui.jquery,
        cookie = layui.cookie,
        upload = layui.upload,
        xmSelect = layui.xmSelect,
        treetable = layui.treetable;
    let options = function(name) {
        let list = [];
        if (name && $.cookie(name)) {
            // 获取字典
            list = JSON.parse(decodeURI($.cookie(name)));
        }
        return list;
    }
    // 富文本上传文件适配器
    class UploadAdapter {
        constructor(loader) {
            this.loader = loader;
        }
        upload() {
            return new Promise((resolve, reject) => {
                let file = [];
                this.loader.file.then(res=>{
                    const data = new FormData();
                    file = res;
                    //文件流
                    data.append('file', file);
                    $.ajax({
                        url: ctx + '/common/uploadImgMinIo',
                        type: 'POST',
                        data: data,
                        dataType: 'json',
                        processData: false,
                        contentType: false,
                        success: function (res) {
                            if (res.code === 200) {
                                resolve({
                                    default: res.data.url
                                });
                            } else {
                                reject(res.message);
                            }
                        }
                    });
                })
            });
        }
        abort() {
        }
    }
    let crud = {
        /**
         * 树结构搜索高亮展示
         * @param dom id节点
         * @param keyword 检索字段
         */
        treeTableSearch(dom, keyword) {
            // 检索字段
            if (!keyword) {
                layer.msg("未输入关键字");
                return;
            }
            // 统计检索匹配数量
            let searchCount = 0;
            // 获取td
            let $tds = $('#' + dom).next('.treeTable').find('.layui-table-body tbody tr td');
            // 查询
            $tds.each(function () {
                $(this).css('background-color', 'transparent');
                if (keyword && $(this).text().indexOf(keyword.trim()) >= 0) {
                    $(this).css('background-color', 'rgba(250,230,160,0.5)');
                    //火狐 ie不支持body,谷歌支持的是body，所以为了兼容写body和html   stop()方法停止当前正在运行的动画
                    $('body,html').stop(true);
                    $('body,html').animate({scrollTop: $(this).offset().top - 150}, 500);
                    searchCount++;
                }
            });
            if(searchCount === 0) {
                layer.msg("无匹配");
                treetable.foldAll('#' + dom);
                return;
            }
            treetable.expandAll('#' + dom);
        },
        /**
         * 多选下拉框
         * @param url 地址
         * @param obj dom节点
         * @param obj obj对象
         */
        initXmSelect(url,dom,obj) {
            let select = xmSelect.render({
                el: "#" + dom,
                tips: "请选择",
                name: dom,
                filterable: true,
                tree: {
                    //是否显示树状结构
                    show: true,
                    //是否展示三角图标
                    showFolderIcon: true,
                    //是否显示虚线
                    showLine: true,
                    //间距
                    indent: 20,
                    //默认展开节点的数组, 为 true 时, 展开所有节点
                    expandedKeys: [ -3 ],
                    //是否严格遵守父子模式
                    strict: false,
                    //是否开启极简模式
                    simple: false,
                    //点击节点是否展开
                    clickExpand: true,
                    //点击节点是否选中
                    clickCheck: true
                },
                toolbar: {
                    show: false
                },
                height: 'auto',
                data: [],
                //分页
                paging: true,
                //每页条数
                pageSize: 10,
            });
            if (obj) {
                url = url + '?ids=' + obj[dom];
            }
            $.ajax({
                type: 'get',
                url: ctx + url,
                success: function(res) {
                    select.update({
                        data: res
                    })
                }
            });
        },
        /**
         * 树结构向导
         * @param url 请求tree接口
         * @param callback 回调函数
         */
        treeWizard: function (url,callback) {
            layer.open({
                type: 2
                ,shade: 0.3
                ,title: "选择"
                ,content: ctx + '/common/treePage?url=' +url
                ,maxmin: true
                ,scrollbar: false
                ,area: ['30%', '80%']
                ,btn: ['确定', '取消']
                ,yes: function(index, layero){
                    let result = window["layui-layer-iframe" + index].getData();
                    layer.close(index);
                    callback(result);
                }
            });
        },
        /**
         * 普通视频上传
         */
        uploadVideo: function(obj) {
            let uploadInst = upload.render({
                elem: '#upload-video-btn'
                ,url: ctx + '/common/uploadVideoMinIo'
                ,accept: 'video'
                ,before: function(obj){
                    //预读本地文件示例，不支持ie8
                    obj.preview(function(index, file, result){
                        $('#upload-video').attr('src', result);
                    });
                }
                ,choose: function(obj) {
                    let files = obj.pushFile();
                    console.log(files)
                }
                ,progress: function(e , percent) {
                    console.log(e);
                }
                ,done: function(res){
                    layer.msg(res.message);
                    if (res.code === 200) {
                        $("#" + obj).val(res.data.url);
                    }
                }
                ,error: function(){
                    //演示失败状态，并实现重传
                    let uploadText = $('#upload-video-text');
                    uploadText.html('<span style="color: #FF5722;">上传失败</span> <a class="layui-btn layui-btn-xs demo-reload">重试</a>');
                    uploadText.find('.demo-reload').on('click', function(){
                        uploadInst.upload();
                    });
                }
            });
            $('#clear-video-btn').on('click',function () {
                $('#upload-video').attr('src', '');
                $("#" + obj).val('');
                layer.msg('清除成功');
            });
        },
        /**
         * 普通图片上传
         */
        uploadImg: function(obj) {
            let uploadInst = upload.render({
                elem: '#upload-img-btn'
                ,url: ctx + '/common/uploadImgMinIo'
                ,before: function(obj){
                    //预读本地文件示例，不支持ie8
                    obj.preview(function(index, file, result){
                        $('#upload-img').attr('src', result); //图片链接（base64）
                    });
                }
                ,done: function(res){
                    layer.msg(res.message);
                    if (res.code === 200) {
                        $("#" + obj).val(res.data.url);
                    }
                }
                ,error: function(){
                    //演示失败状态，并实现重传
                    let uploadText = $('#upload-img-text');
                    uploadText.html('<span style="color: #FF5722;">上传失败</span> <a class="layui-btn layui-btn-xs demo-reload">重试</a>');
                    uploadText.find('.demo-reload').on('click', function(){
                        uploadInst.upload();
                    });
                }
            });
            $('#clear-img-btn').on('click',function () {
                $('#upload-img').attr('src', '');
                $("#" + obj).val('');
                layer.msg('清除成功');
            });
        },
        /**
         * 普通文件上传
         */
        uploadFile: function(obj) {
            let uploadInst = upload.render({
                elem: '#upload-file-btn'
                ,url: ctx + '/common/uploadFileMinIo'
                ,before: function(obj){
                    obj.preview(function(index, file, result){
                        $('#upload-file').attr('href',result);//文件链接
                    });
                }
                ,done: function(res){
                    layer.msg(res.message);
                    if (res.code === 200) {
                        $("#" + obj).val(res.data.url);
                    }
                }
                ,error: function(){
                    //演示失败状态，并实现重传
                    let uploadText = $('#upload-file-text');
                    uploadText.html('<span style="color: #FF5722;">上传失败</span> <a class="layui-btn layui-btn-xs demo-reload">重试</a>');
                    uploadText.find('.demo-reload').on('click', function(){
                        uploadInst.upload();
                    });
                }
            });
            $('#clear-file-btn').on('click',function () {
                $('#upload-file').attr('href','');
                $("#" + obj).val('');
                layer.msg('清除成功');
            });
        },
        /**
         * 初始化富文本编辑器
         * @param obj dom对象
         * @param data 回显数据
         */
        initEditor: function(obj,data) {
            ClassicEditor.create(document.querySelector("#" + obj), {
                toolbar: {
                    items: [
                        'heading',
                        '|',
                        'bold',
                        'italic',
                        'link',
                        'bulletedList',
                        'numberedList',
                        '|',
                        'indent',
                        'outdent',
                        '|',
                        'imageUpload',
                        'blockQuote',
                        'insertTable',
                        'mediaEmbed',
                        'undo',
                        'redo'
                    ]
                },
                language: 'zh-cn',
                image: {
                    toolbar: [
                        'imageTextAlternative',
                        'imageStyle:full',
                        'imageStyle:side'
                    ]
                },
                table: {
                    contentToolbar: [
                        'tableColumn',
                        'tableRow',
                        'mergeTableCells'
                    ]
                },
                licenseKey: '',
            }).then(editor => {
                // 加载了适配器
                editor.plugins.get('FileRepository').createUploadAdapter = (loader)=>{
                    return new UploadAdapter(loader);
                };
                window.editor = editor;
                if (data) {
                    window.editor.setData(data);
                }
            }).catch(err => {
                console.error(err.stack);
            });
        },
        /**
         * 获取富文本编辑器数据
         */
        getEditorData: function() {
            return window.editor.getData();
        },
        /**
         * 播放视频
         */
        showVideo: function (video) {
            if (video) {
                return `<button data-video="${video}" class="layui-btn layui-btn-xs" onclick="fun.playVideo(this)">播放</button>`
            }
            return '暂无';
        },
        /**
         * 显示图片
         */
        showImg: function (img) {
            if (img) {
                return `<img onclick="fun.preview(this)" width="100%" height="100%" src="${img}">`
            }
            return '暂无';
        },
        /**
         * post请求
         * @param url 地址
         * @param data 数据
         * @param callback 回调
         */
        post: function (url, data, callback) {
            $.ajax({
                url: url,
                type: 'POST',
                contentType:'application/json;charset=UTF-8',
                dataType: 'json',
                data: JSON.stringify(data),
                success: function (result) {
                    layer.msg(result.message,{
                        offset:['50%'],
                        time: 3000
                    },function() {
                        if (result.code === 200) {
                            callback();
                        }
                    });
                }
            });
        },
        /**
         * get请求
         * @param url 地址
         * @param callback 回调
         */
        get: function (url, callback) {
            $.ajax({
                url: url,
                type: 'GET',
                success: function (result) {
                    layer.msg(result.message,{
                        offset:['50%'],
                        time: 3000
                    },function() {
                        if (result.code === 200) {
                            callback();
                        }
                    });
                }
            });
        },
        /**
         * 获取字典
         */
        getDictValue: function(name,defaultVal) {
            let list = options(name);
            for(let i = 0; i < list.length; i++) {
                if (list[i].dictValueKey == defaultVal) {
                    return list[i].dictValueName;
                }
            }
            return "未知";
        },
        /**
         * 设置文字回显
         * @param name
         */
        setText:function (id, name, defaultVal) {
            let list = options(name);
            if(!list){
                return;
            }
            $.each(list,function(index,item){
                if(item.dictValueKey == defaultVal) {
                    $("#"+id).html(item.dictValueName);
                    return false;
                }
            });
        },
        /**
         * 设置tab名称
         * @param name
         */
        setTabName:function (name) {
            $(".layui-tab-title").empty();
            let list = options(name);
            if(!list){
                return;
            }
            $.each(list,function(index,item) {
                if (index == 0) {
                    $(".layui-tab-title").append(`<li class="layui-this" data-id="${item.dictValueKey}">${item.dictValueName}</li>`)
                } else {
                    $(".layui-tab-title").append(`<li data-id="${item.dictValueKey}">${item.dictValueName}</li>`)
                }
            });
        },
        /**
         * layui 动态渲染select
         * @param id
         * @param name
         * @param defaultVal
         */
        setSelect:function (id,name,defaultVal) {
            $("#"+id).empty();
            $("#"+id).append(new Option("请选择",""));
            let list = options(name);
            if(!list){
                return;
            }
            $.each(list,function(index,item){
                let option = new Option(item.dictValueName,item.dictValueKey);
                if(item.dictValueKey == defaultVal) {
                    option.selected=true;
                }
                $("#"+id).append(option)
            });
            layui.form.render("select");
            layui.element.render();
        },
        /**
         * layui 动态渲染radio
         * @param id
         * @param name
         * @param defaultVal
         */
        setRadio:function (id,name,defaultVal) {
            $("#"+id).html("");
            let list = options(name);
            if(!list){
                return;
            }
            $.each(list,function(index,item){
                let radioHtml = '<input type="radio" name="'+id+'" value="'+item.dictValueKey+'" title="'+item.dictValueName+'" ';
                if(item.dictValueKey == defaultVal) {
                    radioHtml+='checked';
                }
                radioHtml+='>';
                $("#"+id).append(radioHtml);
            });
            layui.form.render();
            layui.element.render();
        }
    };
    exports('crud', crud);
});