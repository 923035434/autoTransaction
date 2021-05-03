package com.ll.autotransaction.service;

import com.ll.autotransaction.service.model.UserInfo;

public interface UserService {

    public UserInfo getUserByAccount(String account);


    public boolean addUser(String account, String password) throws Exception;


}
