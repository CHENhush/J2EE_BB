package com.j2ee.controller;

import com.j2ee.po.User;
import com.j2ee.service.UserService;
import com.j2ee.util.JwtUtil;
import com.sun.javafx.collections.MappingChange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@CrossOrigin
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @ResponseBody
    public Map login( @RequestBody User user) throws Exception {
        Map<String,Object> map = new HashMap<String,Object>();

//        System.out.println(user.getPwd());
//        System.out.println(user.getUserID());
        User user1 = userService.findUserByID(user.getUserID());
        if(user1 == null){
            map.put("code", -1);
        }
        else if(!user1.getPwd().equals(user.getPwd())){
            map.put("code", 0);
        }
        else {
            map.put("code", 1);
            map.put("token",JwtUtil.createToken(user.getUserID()));
            map.put("name",user1.getName());
        }
        return map;
    }
    
}

//    public Map login(HttpServletRequest request){
//        String userID = request.getParameter("userID");
//        String password = request.getParameter("password");
//        Map<String,Object> map = new HashMap<String,Object>();
//        System.out.println(userID);
//        System.out.println(password);
//        map.put("userID", userID);
//        map.put("password",password);
//        System.out.println(map);
//        return map;
//    }

//    public Map login(String userID, String password){
//        Map<String,Object> map = new HashMap<String,Object>();
//        User user = userService.findUserByID(userID);
//        if(user == null){
//            map.put("code", -1);
//        }
//        else if(!user.getPwd().equals(password)){
//            map.put("code", 0);
//        }
//        else {
//            map.put("code", 1);
//            map.put("token", "1231231231");
//        }
//        return map;
//    }
