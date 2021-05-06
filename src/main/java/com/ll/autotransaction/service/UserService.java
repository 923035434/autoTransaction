package com.ll.autotransaction.service;

import com.ll.autotransaction.service.model.SystemConfigInfo;
import com.ll.autotransaction.service.model.UserInfo;

public interface UserService {

    public UserInfo getUserByAccount(String account);

    public UserInfo getUserById(Integer id);

    public boolean addUser(String account, String password) throws Exception;


    public boolean editSystemConfig(int userId,SystemConfigInfo info) throws Exception;


    public SystemConfigInfo getSystemConfig(int userId) throws Exception;


}
