package com.ll.auto_transaction.service.config;

import java.math.BigDecimal;

public class BrokerageConfig {

    public  static String dfcfCookies ="eastmoney_txzq_zjzh=NTQwMzMwMDUzNjQ5fA%3D%3D; st_si=50432057573442; st_asi=delete; Yybdm=5403; Uid=VKo17csbVtbpLYOEuDBEkQ%3d%3d; Khmc=%e5%88%98%e5%88%a9; st_pvi=78984111720195; st_sp=2021-04-03%2019%3A33%3A30; st_inirUrl=https%3A%2F%2Fwww.eastmoney.com%2F; st_sn=8; st_psi=20210411191420263-11923323313501-0161883660; mobileimei=52f033a4-2100-4d4f-bf1c-d6d187088ec1; Uuid=44fd35eb0ae04300a218662cd99ca70b";

    public static String dfcfValidateCode = "d86d377b-7123-4497-b603-dff31e651753";

    //触发挂单的阈值
    public  static BigDecimal applyThresholdRate = BigDecimal.valueOf(0.5);

    //是否开启自动交易
    public static Boolean enableAutoTransaction = true;



}
