package com.ll.autotransaction.service;

import com.github.pagehelper.PageInfo;
import com.ll.autotransaction.dao.mode.DealLogDo;
import com.ll.autotransaction.dao.mode.StockConfigDo;

public interface DealLogService {

    public PageInfo<DealLogDo> listDealLog(int pageIndex, int pageSize);

    public boolean add(DealLogDo item);


}
