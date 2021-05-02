package com.ll.autotransaction.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ll.autotransaction.dao.mode.UserDo;
import org.springframework.stereotype.Repository;


@Repository
public interface UserDao extends BaseMapper<UserDo> {

}
