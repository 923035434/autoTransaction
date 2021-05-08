package com.ll.autotransaction.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ll.autotransaction.dao.StockConfigDao;
import com.ll.autotransaction.dao.mode.StockConfigDo;
import com.ll.autotransaction.service.StockConfigService;
import com.ll.autotransaction.util.PriceUtil;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Wrapper;
import java.time.LocalDateTime;
import java.util.List;


@Service
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
    public StockConfigDo getItem(int id) {
        return stockConfigDao.selectById(id);
    }

    @Override
    public List<StockConfigDo> listEnable() {
        var wrapper = new QueryWrapper<StockConfigDo>().lambda().eq(StockConfigDo::getEnable,1).orderByDesc(StockConfigDo::getCreateTime);
        var list = stockConfigDao.selectList(wrapper);
        return list;
    }

    @Override
    public boolean add(StockConfigDo item) {
        item.setLowPrice(PriceUtil.getLowPrice(item.getPrice()));
        item.setHighPrice(PriceUtil.getHighPrice(item.getPrice()));
        item.setCreateTime(LocalDateTime.now());
        return stockConfigDao.insert(item)>0;
    }

    @Override
    public boolean edit(StockConfigDo item) {
        var queryResult = stockConfigDao.selectById(item.getId());
        if(item.getPrice()!=null){
            queryResult.setPrice(item.getPrice());
            queryResult.setLowPrice(PriceUtil.getLowPrice(item.getPrice()));
            queryResult.setHighPrice(PriceUtil.getHighPrice(item.getPrice()));
        }
        if(item.getCount()!=null){
            queryResult.setCount(item.getCount());
        }
        if(item.getCode()!=null){
            queryResult.setCode(item.getCode());
        }
        if(item.getName()!=null){
            queryResult.setName(item.getName());
        }
        if(item.getEnable()!=null){
            queryResult.setEnable(item.getEnable());
        }
        queryResult.setUpdateTime(null);
        return stockConfigDao.updateById(item)>0;
    }

    @Override
    public StockConfigDo getItemByCode(String code) {
        var wrapper = new QueryWrapper<StockConfigDo>().lambda().eq(StockConfigDo::getCode,code);
        var queryResult = stockConfigDao.selectList(wrapper);
        if(queryResult.size()>0){
            return queryResult.get(0);
        }
        return null;
    }

    @Override
    public boolean editPrice(String code, BigDecimal Price) {
        var wrapper = new QueryWrapper<StockConfigDo>().lambda().eq(StockConfigDo::getCode,code);
        var queryResult = stockConfigDao.selectList(wrapper);
        if(queryResult.size()>0){
            var item = queryResult.get(0);
            item.setPrice(Price);
            item.setLowPrice(PriceUtil.getLowPrice(item.getPrice()));
            item.setHighPrice(PriceUtil.getHighPrice(item.getPrice()));
            item.setUpdateTime(null);
            return stockConfigDao.updateById(item)>0;
        }
        return false;
    }

    @Override
    public boolean delete(List<String> idList) {
        return stockConfigDao.deleteBatchIds(idList)>0;
    }
}
