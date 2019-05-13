package com.j2ee.controller;

import com.auth0.jwt.interfaces.Claim;
import com.j2ee.mapper.QuestionMapper;
import com.j2ee.mapper.Star_answerMapper;
import com.j2ee.mapper.Star_questionMapper;
import com.j2ee.mapper.UserMapper;
import com.j2ee.po.*;
import com.j2ee.util.JwtUtil;
import com.j2ee.util.Time;
import org.apache.ibatis.annotations.Param;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/question")
public class QuestionController {
    @RequestMapping(value = "/qstar",method = RequestMethod.POST)
    @ResponseBody
    public Map star(@RequestBody(required=true) Map<String,Object> map) throws ParseException {
        Map<String,Object> map1=new HashMap<>();
        map1.put("code",0);
        int questionID=(int) map.get("questionID");
        String token = (String) map.get("token");
        Map<String, Claim> a = JwtUtil.verifyToken(token);
        String userID = JwtUtil.getAppUID(token);
        ApplicationContext applicationContext=new ClassPathXmlApplicationContext("applicationContext.xml");
        Star_questionMapper star_questionMapper=applicationContext.getBean(Star_questionMapper.class);
        QuestionMapper questionMapper=applicationContext.getBean(QuestionMapper.class);
        //查找是否已点赞
        Star_question star_question=new Star_question();
        star_question.setQuestionID(questionID);
        star_question.setUserID(userID);
        int  starOrNot = star_questionMapper.starOrNot(star_question);
        if(starOrNot==0){
            //点赞
            star_question.setStarTime(Time.getIntTime());
            star_questionMapper.addQuestionStar(star_question);
            questionMapper.addStar(questionID);
            map1.put("code",1);
        }
        else{
            //取消
            star_questionMapper.deleteQuestionStar(star_question);
            questionMapper.deleteStar(questionID);
            map1.put("code",1);
        }
        return map1;
    }
    @RequestMapping(value = "/questionList",method = RequestMethod.POST)
    @ResponseBody
    public List<Question> questionList(@RequestBody(required=true) Map<String,Object> map){
        ApplicationContext applicationContext=new ClassPathXmlApplicationContext("applicationContext.xml");
        QuestionMapper questionMapper=applicationContext.getBean(QuestionMapper.class);
        UserMapper userMapper=applicationContext.getBean(UserMapper.class);
        int p= (int) map.get("p");
        int num= (int) map.get("num");
        int offSet=(p-1)*num;
        List<Question> question = questionMapper.findAllQuestion(offSet,num);
            for(int i=0;i<question.size();i++){
                Question question1=question.get(i);
                //限制问题详情200字
                String detail=question1.getDetail();
                int x=detail.length();
                if(x>200){
                    x=200;
                }
                String smallDetail=detail.substring(0,x);
                question1.setDetail(smallDetail);
                User user=userMapper.findUserByID(question1.getUserID());
                question1.setUserName(user.getName());
                question1.setShowTime(Time.getShowTime(question1.getCreateTime()));
            }
        return question;
    }
    @RequestMapping(value = "/questionDetail",method = RequestMethod.POST)
    @ResponseBody
    public Question questionDetail(@RequestBody(required=true) Map<String,Object> map){
        ApplicationContext applicationContext=new ClassPathXmlApplicationContext("applicationContext.xml");
        String token = (String) map.get("token");
        Map<String, Claim> a = JwtUtil.verifyToken(token);
        String userID = JwtUtil.getAppUID(token);
        //找到问题和回答
        QuestionMapper questionMapper=applicationContext.getBean(QuestionMapper.class);
        Question question=questionMapper.findQuestionWithAnswers((int)map.get("questionID"));
        //设置当前用户是否点赞
        Star_questionMapper star_questionMapper=applicationContext.getBean(Star_questionMapper.class);
        Star_question star_question=new Star_question();
        Star_answerMapper star_answerMapper=applicationContext.getBean(Star_answerMapper.class);
        Star_answer star_answer=new Star_answer();
        star_question.setQuestionID((int)map.get("questionID"));
        star_question.setUserID(userID);
        int  starOrNot = star_questionMapper.starOrNot(star_question);
        question.setStarOrNot(starOrNot);
        //设置问题用户名
        UserMapper userMapper=applicationContext.getBean(UserMapper.class);
        User user=userMapper.findUserByID(question.getUserID());
        question.setUserName(user.getName());
        //设置问题时间
        String showTime=Time.getShowTime(question.getCreateTime());
        question.setShowTime(showTime);
        //设置回答的时间、用户名、是否点赞
        List<Answer> answers=question.getAnswers();
        for(int i=0;i<answers.size();i++){
            showTime=Time.getShowTime(answers.get(i).getCreateTime());
            answers.get(i).setShowTime(showTime);
            user=userMapper.findUserByID(answers.get(i).getUserID());
            answers.get(i).setUserName(user.getName());
            star_answer.setAnswerID(answers.get(i).getAnswerID());
            star_answer.setUserID(userID);
            starOrNot=star_answerMapper.starOrNot(star_answer);
            answers.get(i).setStarOrNot(starOrNot);
        }
        question.setAnswers(answers);
        //增加浏览量
        questionMapper.updatePageviews(question.getQuestionID());
        return question;
    }
    @RequestMapping(value = "/addQuestion",method = RequestMethod.POST)
    @ResponseBody
    public Map addQuestion(@RequestBody(required=true) Map<String,Object> map) throws ParseException {
        ApplicationContext applicationContext=new ClassPathXmlApplicationContext("applicationContext.xml");
        QuestionMapper questionMapper=applicationContext.getBean(QuestionMapper.class);
        String token = (String) map.get("token");
        Map<String, Claim> a = JwtUtil.verifyToken(token);
        String userID = JwtUtil.getAppUID(token);
        Question question1=new Question();
        question1.setUserID(userID);
        String question= (String) map.get("question");
        question1.setQuestion(question);
        String detail= (String) map.get("detail");
        question1.setDetail(detail);
        int changeTime = Time.getIntTime();
        question1.setCreateTime(changeTime);
        int result=questionMapper.addQuestion(question1);
        int questionID=questionMapper.findLargestQuestionID();
        Map<String,Object> map1=new HashMap<String ,Object>();
        if(result>0){
            map1.put("code",0);
            map1.put("questionID",questionID);
        }

        else{
            map1.put("code",-1);
        }
        return map1;
    }
}
