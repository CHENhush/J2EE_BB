package com.j2ee.service;

import com.j2ee.po.Star_question;

import java.util.List;

public interface Star_questionService {
    public List<Star_question> findUserStarQuestion(String userID, int offSet, int pageSize);
    public int addQuestionStar(Star_question star_question);
    public int deleteQuestionStar(Star_question star_question);
    public int starOrNot(Star_question star_question);

}
