package com.ll.auto_transaction.job;

import com.ll.auto_transaction.service.config.BrokerageConfig;
import com.ll.auto_transaction.service.model.ApplyDataInfo;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DfcfAutoTransactionJob {

    List<ApplyDataInfo> applyDataInfoList = new ArrayList<>();





    @Scheduled(cron= "*/2 * * * * ?") //每2秒运行一次
    public void doJob(){
        if(BrokerageConfig.enableAutoTransaction&&isTransactionTime()){
            if(isWindUp()){
                //收盘撤单

            }else {
                //正常交易时间段

            }

        }
    }


    /**
     * 是否收盘时间
     * @return
     */
    private boolean isWindUp(){
        boolean result = false;
        if(LocalTime.now().isAfter(LocalTime.of(14,49,0))&&LocalTime.now().isBefore(LocalTime.of(15,00,0))){
            result = true;
        }
        return result;
    }


    /**
     * 是否是交易时间
     * @return
     */
    private boolean isTransactionTime(){
        boolean result = false;
        if(LocalDate.now().getDayOfWeek().getValue()>=1&&LocalDate.now().getDayOfWeek().getValue()<=5){
            if((LocalTime.now().isAfter(LocalTime.of(9,20,00))&&LocalTime.now().isBefore(LocalTime.of(11,30,0)))||
                    (LocalTime.now().isAfter(LocalTime.of(13,00,00))&&LocalTime.now().isBefore(LocalTime.of(15,00,0)))){
                result = true;
            }
        }
        return result;
    }




}
