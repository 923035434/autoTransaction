var loadingIndex;
window.showLoading = function () {
    loadingIndex = layer.load(1, {shade: [0.1, '#fff']});
}
window.hideLoading = function () {
    layer.close(loadingIndex);
}
window.redirect = function (url) {
    window.location.href = url;
}

window.errorMsg = function (msg) {
    if (msg == null || msg == undefined) {
        msg = "错误";
    }
    if (msg.length == 0) {
        return;
    }
    layer.msg(msg);
}

window.GetQueryStringValue = function (name) {
    var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
    var r = window.location.search.substr(1).match(reg);
    if (r !== null) {
        return decodeURI(decodeURIComponent(r[2]));
    }
    return null;
}

/**
 * 修改某个字段，如删除、恢复删除、或其他快捷操作
 * @param data 行数据
 * @param fieldName 需要修改的字段的字段名
 * @param fieldValue 需要修改的字段的字段值
 * @param confirmMsg 询问框标题
 * @param url 接口
 * @param callBack 操作完成后执行的函数
 */
window.doFastAction = function (args) {
    // data,
    //     fieldName,
    //     fieldValue,confirmMsg,url,callBack,isConfirm
    if (!args) {
        errorMsg("请传递参数");
        return;
    }

    if (!args.isConfirm || args.isConfirm == false) {
        var loadIndex = layer.load(2);
        args.data[args.fieldName] = args.fieldValue;
        $.post({
            url: args.url,
            data: JSON.stringify(args.data),
            contentType: 'application/json',
            success: function (res) {
                if (res.success) {
                    layer.close(loadIndex);
                    layer.msg(res.msg, {icon: 1});
                    args.callBack && args.callBack();

                } else {
                    errorMsg(res.msg);
                }
            }
        });
        return;
    }
    layer.confirm(args.confirmMsg, {
        skin: 'layui-layer-admin',
        shade: .1
    }, function (index) {
        args.data[args.fieldName] = args.fieldValue;
        layer.close(index);
        layer.load(2);
        $.post({
            url: args.url,
            data: JSON.stringify(args.data),
            contentType: 'application/json',
            success: function (res) {
                if (res.success) {
                    layer.msg(res.msg, {icon: 1});
                    args.callBack && args.callBack();

                } else {
                    errorMsg(res.msg);
                }
            }
        });

    });


}
//重写$.ajax
// 以下代码是配置layui扩展模块的目录，每个页面都需要引入


window.uiPermissions = [];
layui.config({
    version: '317',
    base: getProjectUrl() + 'assets/module/'
}).extend({
    steps: 'steps/steps',
    notice: 'notice/notice',
    cascader: 'cascader/cascader',
    dropdown: 'dropdown/dropdown',
    fileChoose: 'fileChoose/fileChoose',
    treeTable: 'treeTable/treeTable',
    Split: 'Split/Split',
    Cropper: 'Cropper/Cropper',
    tagsInput: 'tagsInput/tagsInput',
    citypicker: 'city-picker/city-picker',
    introJs: 'introJs/introJs',
    iconPicker: 'iconPicker/iconPicker'
}).use(['layer', 'admin'], function () {
    var $ = layui.jquery;
    var layer = layui.layer;
    var admin = layui.admin;

    // 移除loading动画
    setTimeout(function () {
        admin.removeLoading();
    }, window === top ? 300 : 0);
    (function ($) {
        //首先备份下jquery的ajax方法
        var _ajax = $.ajax;
        //重写jquery的ajax方法
        $.ajax = function (opt) {
            //备份opt中error和success方法
            var fn = {
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                },
                success: function (data, textStatus) {
                }
            }
            if (opt.error) {
                fn.error = opt.error;
            }
            if (opt.success) {
                fn.success = opt.success;
            }
            var _opt = $.extend(opt, {
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    hideLoading();//必须放在这里，不能放在complete
                    var obj = XMLHttpRequest.responseJSON;
                    if (XMLHttpRequest.status == 401) {
                        layer.msg('登录失效，请重新登录', {icon: 2});
                        setTimeout(function () {
                            window.top.location.href = '/login';
                        }, 1000);
                    } else if (XMLHttpRequest.status == 404) {
                        layer.msg(opt.url + " 该接口不存在", {icon: 2});
                    } else if (XMLHttpRequest.status == 400) {
                        layer.msg(obj.errorMessage, {icon: 2});
                    }
                    fn.error(XMLHttpRequest, textStatus, errorThrown);
                },
                success: function (data, textStatus) {

                    hideLoading();//必须放在这里，不能放在complete
                    if (data.IsSuccess != undefined && !data.IsSuccess) {
                        //do something
                    }

                    fn.success(data, textStatus);
                },
                beforeSend: function (XHR) {

                    if (opt.showLoading == undefined || opt.showLoading == true) {
                        showLoading();
                    }
                }
            });
            return _ajax(_opt);
        };
    })(layui.jquery);

    window.executeFilterPer = function executeFilterPer() {
        var permissions = $('[permission]');
        if (window.uiPermissions) {
            permissions.each(function () {
                var flag = false;
                for (var i = 0; i < window.uiPermissions.length; i++) {

                    var flagArr = [];
                    if (window.uiPermissions[i].moduleFlag.toString().indexOf(",") > -1) {
                        flagArr = window.uiPermissions[i].moduleFlag.toString().split(",");
                    }
                    if (flagArr && flagArr.length > 0) {
                        if (flagArr.indexOf($(this).attr("permission")) > -1) {
                            flag = true;
                            break;
                        }
                    } else {
                        if ($(this).attr("permission") === uiPermissions[i].moduleFlag) {
                            flag = true;
                            break;
                        }
                    }

                }
                ;

                if (!flag) {
                    $(this).hide();
                } else {
                    $(this).show();
                }
            });
        }
    }

});

// 获取当前项目的根路径，通过获取layui.js全路径截取assets之前的地址
function getProjectUrl() {
    var layuiDir = layui.cache.dir;
    if (!layuiDir) {
        var js = document.scripts, last = js.length - 1, src;
        for (var i = last; i > 0; i--) {
            if (js[i].readyState === 'interactive') {
                src = js[i].src;
                break;
            }
        }
        var jsPath = src || js[last].src;
        layuiDir = jsPath.substring(0, jsPath.lastIndexOf('/') + 1);
    }
    return layuiDir.substring(0, layuiDir.indexOf('assets'));
}
