package com.ll.autotransaction.service;

import com.ll.autotransaction.dao.mode.StockConfigDo;
import com.ll.autotransaction.service.model.StockApplyConfig;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ApplyService {

    /**
     * 自动挂单
     * @return
     * @throws Exception
     */
     boolean pendingOrders() throws Exception;

    /**
     * 撤单
     * @return
     */
     boolean removeApplyOrders();

    /**
     * 根据配置挂单
     * @param enableList
     * @throws Exception
     */
    void pendingEnableOrders(List<StockApplyConfig> enableList) throws Exception;

    /**
     * 根据价格修整挂单的价
     * @param list
     * @return
     */
    List<StockApplyConfig> CalPrice(List<StockConfigDo> list);
}
