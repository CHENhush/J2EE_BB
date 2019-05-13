package com.j2ee.controller;

import com.auth0.jwt.interfaces.Claim;
import com.j2ee.mapper.Star_answerMapper;
import com.j2ee.mapper.UserMapper;
import com.j2ee.po.Star_answer;
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

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/star_answer")
public class Star_answerController {
    @RequestMapping(value = "/salist",method = RequestMethod.POST)
    @ResponseBody
    public List<Star_answer> starAnswerList(@RequestBody(required=true) Map<String,Object> map) {
        ApplicationContext applicationContext= new ClassPathXmlApplicationContext("applicationContext.xml");
        UserMapper userMapper=applicationContext.getBean(UserMapper.class);
        Star_answerMapper star_answerMapper=applicationContext.getBean(Star_answerMapper.class);
        String token = (String) map.get("token");
        Map<String, Claim> a = JwtUtil.verifyToken(token);
        String userID = JwtUtil.getAppUID(token);
        int p= (int) map.get("p");
        int num= (int) map.get("num");
        int offSet=(p-1)*num;
        List<Star_answer> star_answers=star_answerMapper.findUserStarAnswer(userID,offSet,num);
        for(int i=0;i<star_answers.size();i++){
            Star_answer star_answer=star_answers.get(i);
            //限制问题详情200字
            String detail=star_answer.getAnswer().getAnswer();
            int x=detail.length();
            if(x>200){
                x=200;
            }
            String smallDetail=detail.substring(0,x);
            star_answer.getAnswer().setAnswer(smallDetail);
            User user=userMapper.findUserByID(star_answer.getAnswer().getUserID());
            star_answer.getAnswer().setUserName(user.getName());
            star_answer.getAnswer().setShowTime(Time.getShowTime(star_answer.getAnswer().getCreateTime()));
        }
        return star_answers;

    }

}
