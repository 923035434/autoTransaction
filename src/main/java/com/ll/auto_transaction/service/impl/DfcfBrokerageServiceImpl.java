package com.ll.auto_transaction.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ll.auto_transaction.service.config.BrokerageConfig;
import com.ll.auto_transaction.service.BrokerageService;
import com.ll.auto_transaction.service.model.ApplyDataInfo;
import com.ll.auto_transaction.service.model.DealInfo;
import com.ll.auto_transaction.service.model.StockInfo;
import com.ll.auto_transaction.service.model.TransactionParam;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class DfcfBrokerageServiceImpl implements BrokerageService {

    @Autowired
    private RestTemplate restTemplate;

    DateTimeFormatter dftDf = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    private HttpHeaders getRequestHeader(){
        HttpHeaders headers = new HttpHeaders();
        List<String> cookies = Arrays.asList(BrokerageConfig.dfcfCookies.split(";"));
        headers.put(HttpHeaders.COOKIE, cookies);        //将cookie存入头部
        return headers;
    }

    private String urlAddValidateCode(String url){
        return String.format(url+"?validatekey=%s",BrokerageConfig.dfcfValidateCode);
    }



    /**
     * 获取用户资金持仓数据
     * @return
     */
    private JSONObject queryAssetAndPositionV1(){
        var headers = this.getRequestHeader();
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(null, headers);
        var url = this.urlAddValidateCode("https://jywg.18.cn/Com/queryAssetAndPositionV1");
        ResponseEntity<String> response = restTemplate.postForEntity(
                url,
                request, String.class);
        var jsonObj = JSONObject.parseObject(response.getBody());
        var data = jsonObj.getJSONArray("Data");
        var result = JSONObject.parseObject(data.get(0).toString());
        return result;
    }




    @Override
    public BigDecimal getBalance() {
        var result = BigDecimal.ZERO;
        var queryResult = this.queryAssetAndPositionV1();
        var str = queryResult.getString("Kyzj");
        if(StringUtils.hasText(str)){
            result = BigDecimal.valueOf(Double.valueOf(str));
        }
        return result;
    }

    @Override
    public String buy(TransactionParam param) {
        var result = "";
        var headers = this.getRequestHeader();
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("stockCode",param.getCode());
        map.add("price",param.getPrice().toString());
        map.add("amount",param.getCount().toString());
        map.add("tradeType","B");
        map.add("zqmc",param.getName());
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        var url = this.urlAddValidateCode("https://jywg.18.cn/Trade/SubmitTradeV2");
        ResponseEntity<String> response = restTemplate.postForEntity(
                url,
                request, String.class);
        if(response.getStatusCode().equals(HttpStatus.OK)){
            var jsonObj = JSONObject.parseObject(response.getBody());
            var data = jsonObj.getJSONArray("Data");
            var queryResult = JSONObject.parseObject(data.get(0).toString());
            var applyCode = queryResult.getString("Wtbh");
            result = applyCode;
        }
        return result;
    }

    @Override
    public String sell(TransactionParam param) {
        var result = "";
        var headers = this.getRequestHeader();
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("stockCode",param.getCode());
        map.add("price",param.getPrice().toString());
        map.add("amount",param.getCount().toString());
        map.add("tradeType","S");
        map.add("zqmc",param.getName());
        map.add("gddm","A249723551");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        var url = this.urlAddValidateCode("https://jywg.18.cn/Trade/SubmitTradeV2");
        ResponseEntity<String> response = restTemplate.postForEntity(
                url,
                request, String.class);
        if(response.getStatusCode().equals(HttpStatus.OK)){
            var jsonObj = JSONObject.parseObject(response.getBody());
            var data = jsonObj.getJSONArray("Data");
            var queryResult = JSONObject.parseObject(data.get(0).toString());
            var applyCode = queryResult.getString("Wtbh");
            result = applyCode;
        }
        return result;
    }

    @Override
    public boolean RevokeOrders(String applyCode) {
        var headers = this.getRequestHeader();
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("revokes",LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_"))+applyCode);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        var url = this.urlAddValidateCode("https://jywg.18.cn/Trade/RevokeOrders");
        ResponseEntity<String> response = restTemplate.postForEntity(
                url,
                request, String.class);
        if(response.getStatusCode().equals(HttpStatus.OK)){
            return true;
        }
        return false;
    }


    @Override
    public List<StockInfo> getStockList() {
        var result = new ArrayList<StockInfo>();
        var queryResult = this.queryAssetAndPositionV1();
        var jsonArray = queryResult.getJSONArray("positions");
        if(jsonArray!=null&&jsonArray.size()>0){
            for (var json :jsonArray){
                var jsonObj = JSONObject.parseObject(json.toString());
                var item = new StockInfo();
                item.setCode(jsonObj.getString("Zqdm"));
                item.setName(jsonObj.getString("Zqmc"));
                item.setCount(jsonObj.getInteger("Zqsl"));
                item.setAvailableCount(jsonObj.getInteger("Kysl"));
                item.setCost(jsonObj.getBigDecimal("Ckcbj"));
                item.setNowPrice(jsonObj.getBigDecimal("Zxjg"));
                result.add(item);
            }
        }
        return result;
    }

    @Override
    public List<ApplyDataInfo> GetOrdersData(LocalDate startTime, LocalDate endTime) {
        var result = new ArrayList<ApplyDataInfo>();
        var headers = this.getRequestHeader();
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("st",startTime.format(dftDf));
        map.add("et",endTime.format(dftDf));
        map.add("qqhs","20");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        var url = this.urlAddValidateCode("https://jywg.18.cn/Search/GetHisOrdersData");
        ResponseEntity<String> response = restTemplate.postForEntity(
                url,
                request, String.class);
        var jsonObj = JSONObject.parseObject(response.getBody());
        var dataArray = jsonObj.getJSONArray("Data");
        if(dataArray!=null&&dataArray.size()>0){
            for (var data : dataArray){
                var dataJsonObj = JSONObject.parseObject(data.toString());
                var item = new ApplyDataInfo();
                item.setApplyCode(dataJsonObj.getString("Wtbh"));
                var applyTime = LocalDateTime.of(LocalDate.parse(dataJsonObj.getString("Wtrq"),DateTimeFormatter.ofPattern("yyyyMMdd")),
                        LocalTime.parse(dataJsonObj.getString("Wtsj").substring(0,6),DateTimeFormatter.ofPattern("HHmmss")));
                item.setApplyTime(applyTime);
                item.setApplyType(dataJsonObj.getString("Mmsm"));
                item.setCode(dataJsonObj.getString("Zqdm"));
                item.setName(dataJsonObj.getString("Zqmc"));
                item.setPrice(dataJsonObj.getBigDecimal("Wtjg"));
                item.setCount(dataJsonObj.getInteger("Wtsl"));
                item.setDealCount(dataJsonObj.getInteger("Cjsl"));
                item.setStatus(dataJsonObj.getString("Wtzt"));
                result.add(item);
            }
        }
        return result;
    }

    @Override
    public List<DealInfo> GetHisDealData(LocalDate startTime, LocalDate endTime) {
        return null;
    }
}
