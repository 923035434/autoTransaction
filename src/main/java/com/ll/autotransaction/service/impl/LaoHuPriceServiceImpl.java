package com.ll.autotransaction.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.ll.autotransaction.service.PriceService;
import com.ll.autotransaction.service.config.WxMappingJackson2HttpMessageConverter;
import com.ll.autotransaction.service.model.laoHu.LaoHuResult;
import lombok.var;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Service
public class LaoHuPriceServiceImpl implements PriceService {



    private final String postUrl = "https://www.laohu8.com/proxy/stock/astock/stock_info/detail/";



    @Override
    public BigDecimal getLastPrice(String code) {
        BigDecimal result = null;
        try{
            var queryResult = this.queryData(code);
            if(queryResult!=null){
                result = queryResult.getItems().get(0).getLatestPrice();
            }
        }catch (Exception e){

        }
        return result;
    }



    private LaoHuResult queryData(String code){
        var restTemplate = restTemplateCreate();
        var url = postUrl + code;
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        var resStr = response.getBody();
        LaoHuResult result = JSONArray.parseObject(resStr, LaoHuResult.class);
        return result;
    }





    public RestTemplate restTemplateCreate(){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new WxMappingJackson2HttpMessageConverter());
        return restTemplate;
    }

}
