package com.ll.autotransaction.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ll.autotransaction.dao.StockConfigDao;
import com.ll.autotransaction.dao.mode.StockConfigDo;
import com.ll.autotransaction.service.StockConfigService;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Wrapper;
import java.time.LocalDateTime;


public class StockConfigServiceImpl implements StockConfigService {

    @Autowired
    StockConfigDao stockConfigDao;



    @Override
    public PageInfo<StockConfigDo> listStockConfig(int pageIndex, int pageSize) {
        PageHelper.startPage(pageIndex, pageSize);
        var wrapper = new QueryWrapper<StockConfigDo>().lambda().orderByDesc(StockConfigDo::getCreateTime);
        var list = stockConfigDao.selectList(wrapper);
        PageInfo<StockConfigDo> pageInfo = new PageInfo<StockConfigDo>(list);
        return pageInfo;
    }

    @Override
    public boolean add(StockConfigDo item) {
        item.setCreateTime(LocalDateTime.now());
        return stockConfigDao.insert(item)>0;
    }

    @Override
    public boolean edit(StockConfigDo item) {
        item.setUpdateTime(null);
        return stockConfigDao.updateById(item)>0;
    }

    @Override
    public boolean delete(int id) {
        return stockConfigDao.deleteById(id)>0;
    }
}
