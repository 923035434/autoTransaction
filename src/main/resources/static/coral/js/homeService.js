function HomeService() {


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






    //获取订单数据
    this.getHomeResult = function (param){
        return this.ajaxNativeJsonBase("/home/getHomeResult",param) ;
    }




    //获取当前job配置
    this.getCurrentJobConfig = function (param){
        return this.ajaxNativeJsonBase("/home/getCurrentJobConfig",param) ;
    }


    //根据配置自动挂单
    this.pendingOrders = function (param){
        return this.ajaxNativeJsonBase("/home/pendingOrders",param) ;
    }

    //一键撤单
    this.removeApplyOrders = function (param){
        return this.ajaxNativeJsonBase("/home/removeApplyOrders",param) ;
    }



}