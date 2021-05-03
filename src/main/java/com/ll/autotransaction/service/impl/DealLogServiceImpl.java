package com.ll.autotransaction.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ll.autotransaction.dao.DealLogDao;
import com.ll.autotransaction.dao.mode.DealLogDo;
import com.ll.autotransaction.dao.mode.StockConfigDo;
import com.ll.autotransaction.service.DealLogService;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

public class DealLogServiceImpl implements DealLogService {

    @Autowired
    DealLogDao dealLogDao;


    @Override
    public PageInfo<DealLogDo> listDealLog(int pageIndex, int pageSize) {
        PageHelper.startPage(pageIndex, pageSize);
        var wrapper = new QueryWrapper<DealLogDo>().lambda().orderByDesc(DealLogDo::getCreateTime);
        var list = dealLogDao.selectList(wrapper);
        PageInfo<DealLogDo> pageInfo = new PageInfo<DealLogDo>(list);
        return pageInfo;
    }

    @Override
    public boolean add(DealLogDo item) {
        item.setCreateTime(LocalDateTime.now());
        return dealLogDao.insert(item)>0;
    }

}
