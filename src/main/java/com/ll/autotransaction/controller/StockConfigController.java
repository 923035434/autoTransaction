package com.ll.autotransaction.controller;

import com.github.pagehelper.PageInfo;
import com.ll.autotransaction.controller.model.CommonResult;
import com.ll.autotransaction.controller.model.vo.PageParamVO;
import com.ll.autotransaction.controller.model.vo.StockDeleteParam;
import com.ll.autotransaction.dao.mode.StockConfigDo;
import com.ll.autotransaction.service.StockConfigService;
import com.ll.autotransaction.service.model.SystemConfigInfo;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/stockConfig")
public class StockConfigController {


    @Autowired
    StockConfigService stockConfigService;

    @RequestMapping("/listStockConfig")
    public CommonResult<PageInfo<StockConfigDo>> listStockConfig(int page, int limit) throws Exception {
        var result = stockConfigService.listStockConfig(page,limit);
        return CommonResult.success(result);
    }

    @RequestMapping("/getItem")
    public CommonResult<StockConfigDo> getItem(@RequestBody StockConfigDo param) throws Exception {
        var result = stockConfigService.getItem(param.getId());
        return CommonResult.success(result);
    }


    @PostMapping("/add")
    public CommonResult<Boolean> add(@RequestBody StockConfigDo param) throws Exception {
        var result = stockConfigService.add(param);
        return CommonResult.success(result);
    }

    @PostMapping("/edit")
    public CommonResult<Boolean> edit(@RequestBody StockConfigDo param) throws Exception {
        var result = stockConfigService.edit(param);
        return CommonResult.success(result);
    }



    @PostMapping("/delete")
    public CommonResult<Boolean> delete(@RequestBody StockDeleteParam param) throws Exception {
        var result = stockConfigService.delete(param.getIdList());
        return CommonResult.success(result);
    }





}
