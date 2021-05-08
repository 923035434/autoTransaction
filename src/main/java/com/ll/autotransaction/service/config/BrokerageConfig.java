package com.ll.autotransaction.service.config;

import com.ll.autotransaction.service.model.ApplyDataInfo;
import lombok.Data;
import lombok.var;
import org.omg.CORBA.PUBLIC_MEMBER;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class BrokerageConfig {

    public  static String dfcfCookies ="eastmoney_txzq_zjzh=NTQwMzMwMDUzNjQ5fA%3D%3D; st_si=97657355616226; st_pvi=78984111720195; st_sp=2021-04-03%2019%3A33%3A30; st_inirUrl=https%3A%2F%2Fwww.eastmoney.com%2F; st_sn=1; st_psi=20210506200154764-11923323313501-0091301740; st_asi=delete; Yybdm=5403; Uid=VKo17csbVtbpLYOEuDBEkQ%3d%3d; Khmc=%e5%88%98%e5%88%a9; mobileimei=2b4b52c9-f7a6-44af-9f85-2f389a001bb1; Uuid=bca2b8585da842fa829391619f0b93e4";

    public static String dfcfValidateCode = "7678e6f5-9aff-4a18-aa13-ff1374cd4159";

    public static String dfcfHost = "https://jywg.18.cn";

//
//    //触发挂单的阈值
//    public  static BigDecimal applyThresholdRate = BigDecimal.valueOf(0.5);

    //是否开启自动交易
    public static Boolean enableAutoTransaction = false;

    //收盘撤单日期
    public static LocalDate windUpDate = LocalDate.now().plusDays(-1);

    //挂单日期
    public static LocalDate pendingOrderDate = LocalDate.now().plusDays(-1);

    public static List<ApplyDataInfo> applyDataInfos = new ArrayList<>();


    public static void resetConfig(){
        windUpDate = LocalDate.now().plusDays(-1);
        pendingOrderDate = LocalDate.now().plusDays(-1);
        applyDataInfos = new ArrayList<>();
    }


    public static void removeApplyDataInfoForCode(String code){
        var filterList = applyDataInfos.stream().filter(a->a.getCode().equals(code)).collect(Collectors.toList());
        if(filterList.size()>0){
            applyDataInfos.removeAll(filterList);
        }
    }



}
