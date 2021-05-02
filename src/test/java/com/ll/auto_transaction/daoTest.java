package com.ll.auto_transaction;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ll.auto_transaction.dao.UserDao;
import com.ll.auto_transaction.dao.mode.UserDo;
import lombok.var;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class DaoTest {

    @Autowired
    private UserDao userDao;

    @Test
    public void getBalanceTest(){
        var result = userDao.selectList(null);
    }

}
