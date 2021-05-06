function ConfigService() {


    this.ajaxNativeJsonBase = function (url,param) {
        var result = null;
        $.ajax({
            type: "post",
            url: url,
            async:false, // 异步请求
            cache:false, // 设置为 false 将不缓存此页面
            contentType: "application/json; charset=utf-8",
            dataType: 'json', // 返回对象
            data: JSON.stringify(param),
            success: function(res) {
                result =res;
            },
            error: function(res) {
                // 请求失败函数
                console.error(res);
            }
        })
        return result;
    }






    //获取api配置数据
    this.getSystemConfig = function (param){
        return this.ajaxNativeJsonBase("/home/getSystemConfig",param) ;
    }



    //修改api配置数据
    this.editSystemConfig = function (param){
        return this.ajaxNativeJsonBase("/home/editSystemConfig",param) ;
    }

    this.getItem = function (param) {
        return this.ajaxNativeJsonBase("/stockConfig/getItem",param) ;
    }


    this.addStockConfig = function (param) {
        return this.ajaxNativeJsonBase("/stockConfig/add",param) ;
    }


    this.editStockConfig = function (param) {
        return this.ajaxNativeJsonBase("/stockConfig/edit",param) ;
    }


    this.deleteStockConfig = function (param) {
        return this.ajaxNativeJsonBase("/stockConfig/delete",param) ;
    }



}