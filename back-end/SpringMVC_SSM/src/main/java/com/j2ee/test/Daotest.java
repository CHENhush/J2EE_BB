package com.j2ee.test;

import com.j2ee.mapper.UserMapper;
import com.j2ee.po.User;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Daotest {
    @Test
    public void findUserByIDTest() {
        ApplicationContext applicationContext=new ClassPathXmlApplicationContext
                                ("applicationContext.xml");
        UserMapper userMapper=applicationContext.getBean(UserMapper.class);
        User user=userMapper.findUserByID("31601112");
        System.out.println(user.getName());
    }

}
