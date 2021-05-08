package com.ll.autotransaction.controller.model.vo;

import com.ll.autotransaction.service.model.ApplyDataInfo;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class JobConfigVO {

    String dfcfCookies ;

    String dfcfValidateCode;

    String dfcfHost;

    //是否开启自动交易
    Boolean enableAutoTransaction;

    //收盘撤单日期
    LocalDate windUpDate;

    //挂单日期
    LocalDate pendingOrderDate;

    List<ApplyDataInfo> applyDataInfos;


}
