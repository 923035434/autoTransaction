package com.ll.autotransaction.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ll.autotransaction.dao.mode.UserDo;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserDao extends BaseMapper<UserDo> {

}
