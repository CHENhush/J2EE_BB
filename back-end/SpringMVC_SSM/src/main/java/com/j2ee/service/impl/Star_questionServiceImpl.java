package com.j2ee.service.impl;

import com.j2ee.mapper.Star_questionMapper;
import com.j2ee.po.Star_question;
import com.j2ee.service.Star_questionService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class Star_questionServiceImpl implements Star_questionService {
    @Autowired
    private Star_questionMapper star_questionMapper;

    @Override
    public List<Star_question> findUserStarQuestion(String userID, int offSet, int pageSize) {
        return this.star_questionMapper.findUserStarQuestion(userID,offSet,pageSize);
    }

    @Override
    public int addQuestionStar(Star_question star_question) {
        return this.star_questionMapper.addQuestionStar(star_question);
    }

    @Override
    public int deleteQuestionStar(Star_question star_question) {
        return this.star_questionMapper.deleteQuestionStar(star_question);
    }

    @Override
    public int starOrNot(Star_question star_question) {
        return this.star_questionMapper.deleteQuestionStar(star_question);
    }
}
