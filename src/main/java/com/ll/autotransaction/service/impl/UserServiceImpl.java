package com.ll.autotransaction.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ll.autotransaction.dao.UserDao;
import com.ll.autotransaction.dao.mode.UserDo;
import com.ll.autotransaction.service.UserService;
import com.ll.autotransaction.service.config.BrokerageConfig;
import com.ll.autotransaction.service.model.SystemConfigInfo;
import com.ll.autotransaction.service.model.UserInfo;
import com.ll.autotransaction.util.MD5Util;
import lombok.var;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;


    @Override
    public UserInfo getUserByAccount(String account) {
        if(!StringUtils.hasText(account)){
            return null;
        }
        var result = new UserInfo();
        var wrapper = new QueryWrapper<UserDo>().lambda()
                .eq(UserDo::getAccountNumber,account);
        var queryResult = userDao.selectOne(wrapper);
        if(queryResult==null){
            return null;
        }
        BeanUtils.copyProperties(queryResult,result);
        return result;
    }

    @Override
    public UserInfo getUserById(Integer id) {
        var result = new UserInfo();
        var queryResult = userDao.selectById(id);
        if(queryResult==null){
            return null;
        }
        BeanUtils.copyProperties(queryResult,result);
        return result;
    }

    @Override
    public boolean addUser(String account, String password) throws Exception {
        if(!StringUtils.hasText(account)||!StringUtils.hasText(password)){
            return false;
        }
        var queryResult = getUserByAccount(account);
        if(queryResult!=null){
            throw new Exception("该账号已被注册");
        }
        var userDo = new UserDo();
        userDo.setCreateTime(LocalDateTime.now());
        userDo.setAccountNumber(account);
        userDo.setStatus(0);
        userDo.setPassword(MD5Util.encode(password));
        return userDao.insert(userDo)>0;
    }

    @Override
    public boolean editSystemConfig(int userId, SystemConfigInfo info) throws Exception {
        var userInfo = userDao.selectById(userId);
        if(userInfo==null){
            throw new Exception("用户数据不存在");
        }
        if(StringUtils.hasText(info.getCookies())){
            userInfo.setCookies(info.getCookies());
        }
        if(StringUtils.hasText(info.getHost())){
            userInfo.setHost(info.getHost());
        }
        if(StringUtils.hasText(info.getValidateCode())){
            userInfo.setValidateCode(info.getValidateCode());
        }
        if(info.getEnableAutoTransaction()!=null){
            BrokerageConfig.enableAutoTransaction = info.getEnableAutoTransaction()==1;
            BrokerageConfig.resetConfig();
            userInfo.setEnableAutoTransaction(info.getEnableAutoTransaction());
        }
        return userDao.updateById(userInfo)>0;
    }

    @Override
    public SystemConfigInfo getSystemConfig(int userId) throws Exception {
        var result = new SystemConfigInfo();
        var userInfo = userDao.selectById(userId);
        if(userInfo==null){
            throw new Exception("用户数据不存在");
        }
        result.setCookies(userInfo.getCookies());
        result.setEnableAutoTransaction(userInfo.getEnableAutoTransaction());
        result.setHost(userInfo.getHost());
        result.setValidateCode(userInfo.getValidateCode());
        return result;
    }

    @Override
    public void updateConfig(){
        var userInfo = this.getUserByAccount("13226546881");
        BrokerageConfig.dfcfCookies = userInfo.getCookies();
        BrokerageConfig.dfcfValidateCode = userInfo.getValidateCode();
        BrokerageConfig.dfcfHost = userInfo.getHost();
        BrokerageConfig.enableAutoTransaction = userInfo.getEnableAutoTransaction()==1;

    }

    @Override
    public void setEnableAutoTransaction(boolean enable) throws Exception {
        var userInfo = this.getUserByAccount("13226546881");
        var configInfo = new SystemConfigInfo(){{
            setEnableAutoTransaction(enable?1:0);
        }};
        this.editSystemConfig(userInfo.getId(),configInfo);
    }
}
