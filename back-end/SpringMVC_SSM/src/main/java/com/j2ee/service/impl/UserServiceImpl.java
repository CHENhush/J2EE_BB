package com.j2ee.service.impl;

import com.j2ee.mapper.UserMapper;
import com.j2ee.po.User;
import com.j2ee.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {
    // 注解注入 UserMapper
    @Autowired
    private UserMapper userMapper;
    // 通过ID寻找客户
    @Override
    public User findUserByID(String userID) {
        return this.userMapper.findUserByID(userID);
    }
    // 添加客户
    @Override
    public void addUser(User user) {
        this.userMapper.addUser(user);
    }

    @Override
    public void deleteUser(String userID) {
        this.userMapper.deleteUser(userID);
    }
}
