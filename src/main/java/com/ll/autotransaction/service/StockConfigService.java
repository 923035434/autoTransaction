package com.ll.autotransaction.service;

import com.github.pagehelper.PageInfo;
import com.ll.autotransaction.dao.mode.StockConfigDo;

import java.util.List;

public interface StockConfigService {


    public PageInfo<StockConfigDo> listStockConfig(int pageIndex, int pageSize);

    public boolean add(StockConfigDo item);

    public boolean edit(StockConfigDo item);

    public boolean delete(int id);



}
