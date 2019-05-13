package com.j2ee.controller;

import com.auth0.jwt.interfaces.Claim;
import com.j2ee.mapper.UserMapper;
import com.j2ee.po.User;
import com.j2ee.service.UserService;
import com.j2ee.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@CrossOrigin
@RequestMapping("/user")
public class UserController {
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @ResponseBody
    public Map login(@RequestBody User user) throws Exception {
        ApplicationContext applicationContext=new ClassPathXmlApplicationContext("applicationContext.xml");
        UserMapper userMapper=applicationContext.getBean(UserMapper.class);
        Map<String,Object> map = new HashMap<String,Object>();
        User user1=userMapper.findUserByID(user.getUserID());
        String pwd=MD5.getHash(user.getPwd());
        if(user1 == null||!pwd.equals(user1.getPwd())){
            map.put("code", -1);
        }
        else {
            map.put("code", 0);
            map.put("token", JwtUtil.createToken(user.getUserID()));
            map.put("name",user1.getName());
        }
        return map;
    }

    @RequestMapping(value = "/changePwd",method = RequestMethod.POST)
    @ResponseBody
    public Map changePwd(@RequestBody(required = true) Map<String,Object> map){
        ApplicationContext applicationContext=new ClassPathXmlApplicationContext("applicationContext.xml");
        UserMapper userMapper=applicationContext.getBean(UserMapper.class);
        String token = (String) map.get("token");
        Map<String, Claim> a = JwtUtil.verifyToken(token);
        String userID = JwtUtil.getAppUID(token);
        User user=userMapper.findUserByID(userID);
        Map<String,Object> map1=new HashMap<>();
        String newpwd=MD5.getHash((String)map.get("newpwd"));
        String pwd=MD5.getHash((String)map.get("pwd"));
        if(newpwd==null||newpwd.length()==0||!pwd.equals(user.getPwd())){
            map1.put("code",-1);
        }
        else{
            user.setPwd(newpwd);
            userMapper.updateUserPwd(user);
            map1.put("code",0);
        }
        return map1;
    }
}
