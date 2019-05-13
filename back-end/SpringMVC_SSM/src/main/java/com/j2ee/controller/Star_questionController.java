package com.j2ee.controller;

import com.auth0.jwt.interfaces.Claim;
import com.j2ee.mapper.QuestionMapper;
import com.j2ee.mapper.Star_questionMapper;
import com.j2ee.mapper.UserMapper;
import com.j2ee.po.Question;
import com.j2ee.po.Star_question;
import com.j2ee.po.User;
import com.j2ee.util.JwtUtil;
import com.j2ee.util.Time;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/star_question")
public class Star_questionController {
    @RequestMapping(value = "/sqlist",method = RequestMethod.POST)
    @ResponseBody
    public List<Star_question> starQuestionList(@RequestBody(required=true) Map<String,Object> map){
        ApplicationContext applicationContext= new ClassPathXmlApplicationContext("applicationContext.xml");
        Star_questionMapper star_questionMapper=applicationContext.getBean(Star_questionMapper.class);
        UserMapper userMapper=applicationContext.getBean(UserMapper.class);
        String token = (String) map.get("token");
        Map<String, Claim> a = JwtUtil.verifyToken(token);
        String userID = JwtUtil.getAppUID(token);
        int p= (int) map.get("p");
        int num= (int) map.get("num");
        int offSet=(p-1)*num;
        List<Star_question> star_questions=star_questionMapper.findUserStarQuestion(userID,offSet,num);
        for(int i=0;i<star_questions.size();i++){
            Star_question star_question=star_questions.get(i);
            //限制问题详情200字
            String detail=star_question.getQuestion().getDetail();
            int x=detail.length();
            if(x>200){
                x=200;
            }
            String smallDetail=detail.substring(0,x);
            star_question.getQuestion().setDetail(smallDetail);
            User user=userMapper.findUserByID(star_question.getQuestion().getUserID());
            star_question.getQuestion().setUserName(user.getName());
            star_question.getQuestion().setShowTime(Time.getShowTime(star_question.getQuestion().getCreateTime()));
        }
        return star_questions;
    }
}
