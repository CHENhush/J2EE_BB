package com.j2ee.mapper;

import com.j2ee.po.Star_answer;

import java.util.List;

public interface Star_answerMapper {
    public List<Star_answer> findUserStarAnswer(String userID, int offSet, int pageSize);
    public int addAnswerStar(Star_answer star_answer);
    public int deleteAnswerStar(Star_answer star_answer);
    public int starOrNot(Star_answer star_answer);

}
