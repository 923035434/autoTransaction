package com.ll.autotransaction.service.config;

import java.math.BigDecimal;

public class BrokerageConfig {

    public  static String dfcfCookies ="eastmoney_txzq_zjzh=NTQwMzMwMDUzNjQ5fA%3D%3D; st_si=50097380203570; st_pvi=38904729139094; st_sp=2021-04-29%2022%3A07%3A45; st_inirUrl=https%3A%2F%2Fwww.eastmoney.com%2F; st_sn=1; st_psi=20210429220744164-11923323313501-6587932567; st_asi=delete; Yybdm=5403; Uid=VKo17csbVtbpLYOEuDBEkQ%3d%3d; Khmc=%e5%88%98%e5%88%a9; mobileimei=6d5030cc-28a2-4e57-84b9-9be3149d9ed0; Uuid=7ea589eb9f1e4e2cbc7864aa2add1e1c";

    public static String dfcfValidateCode = "9df38e7b-f400-4e7a-9e3f-cee25ca78703";

    public static String dfcfHost = "https://jy.xzsec.com";


    //触发挂单的阈值
    public  static BigDecimal applyThresholdRate = BigDecimal.valueOf(0.5);

    //是否开启自动交易
    public static Boolean enableAutoTransaction = true;



}
