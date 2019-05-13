package com.j2ee.controller;

import com.auth0.jwt.interfaces.Claim;
import com.j2ee.mapper.AnswerMapper;
import com.j2ee.mapper.Star_answerMapper;
import com.j2ee.po.Answer;
import com.j2ee.po.Star_answer;
import com.j2ee.util.JwtUtil;
import com.j2ee.util.Time;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/answer")
public class AnswerController {
    @RequestMapping(value = "/astar",method = RequestMethod.POST)
    @ResponseBody
    public Map star(@RequestBody(required=true) Map<String,Object> map) throws ParseException {
        Map<String,Object> map1=new HashMap<>();
        map1.put("code",0);
        int answerID=(int) map.get("answerID");
        String token = (String) map.get("token");
        Map<String, Claim> a = JwtUtil.verifyToken(token);
        String userID = JwtUtil.getAppUID(token);
        ApplicationContext applicationContext=new ClassPathXmlApplicationContext("applicationContext.xml");
        Star_answerMapper star_answerMapper=applicationContext.getBean(Star_answerMapper.class);
        AnswerMapper answerMapper=applicationContext.getBean(AnswerMapper.class);
        //查找是否点赞
        Star_answer star_answer=new Star_answer();
        star_answer.setAnswerID(answerID);
        star_answer.setUserID(userID);
        int  starOrNot = star_answerMapper.starOrNot(star_answer);
        if(starOrNot==0){
            //点赞
            star_answer.setStarTime(Time.getIntTime());
            star_answerMapper.addAnswerStar(star_answer);
            answerMapper.addStar(answerID);
            map1.put("code",1);
        }
        else{
            //取消
            star_answerMapper.deleteAnswerStar(star_answer);
            answerMapper.deleteStar(answerID);
            map1.put("code",1);
        }
        return null;
    }
    @RequestMapping(value = "/addAnswer",method = RequestMethod.POST)
    @ResponseBody
    public Map addAnswer(@RequestBody(required=true) Map<String,Object> map) throws ParseException {
        ApplicationContext applicationContext=new ClassPathXmlApplicationContext("applicationContext.xml");
        AnswerMapper answerMapper=applicationContext.getBean(AnswerMapper.class);
        String token = (String) map.get("token");
        Map<String, Claim> a = JwtUtil.verifyToken(token);
        String userID = JwtUtil.getAppUID(token);
        Answer answer1=new Answer();
        answer1.setQuestionID((int)map.get("questionID"));
        answer1.setUserID(userID);
        int changeTime = Time.getIntTime();
        answer1.setCreateTime(changeTime);
        answer1.setAnswer((String)map.get("answer"));
        int result=answerMapper.addAnswer(answer1);
        int answerID=answerMapper.findLargestAnswerID();
        Map<String,Object> map1=new HashMap<String ,Object>();
        if(result>0){
            map1.put("code",0);
            map1.put("answerID",answerID);
        }
        else{
            map1.put("code",-1);
        }
        return map1;
    }
}
