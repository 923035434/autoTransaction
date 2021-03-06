package com.ll.autotransaction.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ll.autotransaction.service.config.BrokerageConfig;
import com.ll.autotransaction.service.BrokerageService;
import com.ll.autotransaction.service.model.*;
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
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("moneyType","RMB");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        var url = this.urlAddValidateCode(BrokerageConfig.dfcfHost+"/Com/queryAssetAndPositionV1");
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
    public StockAccountInfo getStockAccountInfo() {
        var result = new StockAccountInfo();
        var queryResult = this.queryAssetAndPositionV1();
        var balanceStr = queryResult.getString("Kyzj");
        if(StringUtils.hasText(balanceStr)){
            result.setBalance(BigDecimal.valueOf(Double.valueOf(balanceStr)));
        }
        var todayStr = queryResult.getString("Dryk");
        if(StringUtils.hasText(todayStr)){
            result.setTodayProfit(BigDecimal.valueOf(Double.valueOf(todayStr)));
        }
        var allStr = queryResult.getString("Ljyk");
        if(StringUtils.hasText(allStr)){
            result.setAllProfit(BigDecimal.valueOf(Double.valueOf(allStr)));
        }
        return result;
    }

    @Override
    public String buy(TransactionParam param) throws Exception {
        var balance = this.getBalance();
        var amount = param.getPrice().multiply(BigDecimal.valueOf(param.getCount()));
        if(amount.compareTo(balance)>0){
            throw new Exception("资金不足");
        }
        var result = "";
        var headers = this.getRequestHeader();
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("stockCode",param.getCode());
        map.add("price",param.getPrice().toString());
        map.add("amount",param.getCount().toString());
        map.add("tradeType","B");
        map.add("zqmc",param.getName());
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        var url = this.urlAddValidateCode(BrokerageConfig.dfcfHost+"/Trade/SubmitTradeV2");
        ResponseEntity<String> response = restTemplate.postForEntity(
                url,
                request, String.class);
        if(response.getStatusCode().equals(HttpStatus.OK)){
            var jsonObj = JSONObject.parseObject(response.getBody());
            var state = jsonObj.getString("Status");
            if(state.equals("-1")){
                throw new Exception(response.getBody());
            }
            var data = jsonObj.getJSONArray("Data");
            var queryResult = JSONObject.parseObject(data.get(0).toString());
            var applyCode = queryResult.getString("Wtbh");
            result = applyCode;
        }
        return result;
    }

    @Override
    public String sell(TransactionParam param) throws Exception {
        var result = "";
        var headers = this.getRequestHeader();
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("stockCode",param.getCode());
        map.add("price",param.getPrice().toString());
        map.add("amount",param.getCount().toString());
        map.add("tradeType","S");
        map.add("zqmc",param.getName());
//        map.add("gddm","0246918803");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        var url = this.urlAddValidateCode(BrokerageConfig.dfcfHost+"/Trade/SubmitTradeV2");
        ResponseEntity<String> response = restTemplate.postForEntity(
                url,
                request, String.class);
        if(response.getStatusCode().equals(HttpStatus.OK)){
            var jsonObj = JSONObject.parseObject(response.getBody());
            var state = jsonObj.getString("Status");
            if(state.equals("-1")){
                throw new Exception(response.getBody());
            }
            var data = jsonObj.getJSONArray("Data");
            var queryResult = JSONObject.parseObject(data.get(0).toString());
            var applyCode = queryResult.getString("Wtbh");
            result = applyCode;
        }
        return result;
    }

    @Override
    public boolean revokeOrders(String applyCode) {
        var headers = this.getRequestHeader();
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("revokes",LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_"))+applyCode);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        var url = this.urlAddValidateCode(BrokerageConfig.dfcfHost+"/Trade/RevokeOrders");
        ResponseEntity<String> response = restTemplate.postForEntity(
                url,
                request, String.class);
        if(response.getStatusCode().equals(HttpStatus.OK)){
            return true;
        }
        return false;
    }

    @Override
    public boolean revokeOrders() {
        var queryResult = this.getTodayOrdersData("已报");
        if(queryResult.size()>0){
            for (var item :queryResult){
                this.revokeOrders(item.getApplyCode());
            }
        }
        return true;
    }

    @Override
    public boolean revokeOrdersByCode(List<String> codeList) {
        var queryResult = this.getTodayOrdersData(null);
        if(queryResult.size()>0){
            for (var code:codeList){
                for (var item :queryResult){
                    if(code.equals(item.getCode())){
                        this.revokeOrders(item.getApplyCode());
                    }
                }
            }
        }
        return true;
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
    public List<ApplyDataInfo> getOrdersData(LocalDate startTime, LocalDate endTime) {
        var result = new ArrayList<ApplyDataInfo>();
        var headers = this.getRequestHeader();
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("st",startTime.format(dftDf));
        map.add("et",endTime.format(dftDf));
        map.add("qqhs","20");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        var url = this.urlAddValidateCode(BrokerageConfig.dfcfHost+"/Search/GetHisOrdersData");
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
    public List<DealInfo> getHisDealData(LocalDate startTime, LocalDate endTime) {
        return null;
    }

    @Override
    public List<ApplyDataInfo> getTodayOrdersData(String state) {
        var result = new ArrayList<ApplyDataInfo>();
        var headers = this.getRequestHeader();
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("qqhs","20");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        var url = this.urlAddValidateCode(BrokerageConfig.dfcfHost+"/Search/GetOrdersData");
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
                if(!item.getApplyType().equals("配售申购")){
                    if(StringUtils.hasText(state)){
                        if(item.getStatus().equals(state)){
                            result.add(item);
                        }
                    }else {
                        result.add(item);
                    }
                }
            }
        }
        return result;
    }

    @Override
    public List<DealInfo> getTodayHisDealData() {
        var result = new ArrayList<DealInfo>();
        var headers = this.getRequestHeader();
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("qqhs","20");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        var url = this.urlAddValidateCode(BrokerageConfig.dfcfHost+"/Search/GetDealData");
        ResponseEntity<String> response = restTemplate.postForEntity(
                url,
                request, String.class);
        var jsonObj = JSONObject.parseObject(response.getBody());
        var dataArray = jsonObj.getJSONArray("Data");
        if(dataArray!=null&&dataArray.size()>0){
            for (var data : dataArray){
                var dataJsonObj = JSONObject.parseObject(data.toString());
                var item = new DealInfo();
                item.setApplyCode(dataJsonObj.getString("Wtbh"));
                var dealTime = LocalDateTime.of(LocalDate.now(),
                        LocalTime.parse(dataJsonObj.getString("Cjsj").substring(0,6),DateTimeFormatter.ofPattern("HHmmss")));
                item.setDealTime(dealTime);
                item.setCode(dataJsonObj.getString("Zqdm"));
                item.setName(dataJsonObj.getString("Zqmc"));
                item.setApplyType(dataJsonObj.getString("Mmsm"));
                item.setCount(dataJsonObj.getInteger("Cjsl"));
                item.setPrice(dataJsonObj.getBigDecimal("Wtjg"));
                result.add(item);
            }
        }
        return result;
    }
}
