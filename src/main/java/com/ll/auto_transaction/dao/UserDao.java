package com.ll.auto_transaction.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ll.auto_transaction.dao.mode.UserDo;
import org.springframework.stereotype.Repository;


@Repository
public interface UserDao extends BaseMapper<UserDo> {

}
