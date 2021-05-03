package com.ll.autotransaction;


import com.ll.autotransaction.service.UserService;
import lombok.var;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    UserService userService;

    @Test
    public void addUserTest() throws Exception {
        var result = userService.addUser("13226546881","1233210a");
        System.out.println(result);
    }


    @Test
    public void getUserByAccountTest(){
        var result = userService.getUserByAccount("13226546881");
        System.out.println(result);
    }


}
