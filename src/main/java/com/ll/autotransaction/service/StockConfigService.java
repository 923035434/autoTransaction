package com.ll.autotransaction.service;

import com.github.pagehelper.PageInfo;
import com.ll.autotransaction.dao.mode.StockConfigDo;
import org.omg.CORBA.PUBLIC_MEMBER;

import java.util.List;

public interface StockConfigService {


    public PageInfo<StockConfigDo> listStockConfig(int pageIndex, int pageSize);

    public StockConfigDo getItem(int id);

    public List<StockConfigDo> listEnable();

    public boolean add(StockConfigDo item);

    public boolean edit(StockConfigDo item);

    public boolean delete(List<String> idList);



}
