package com.j2ee.service.impl;

import com.j2ee.mapper.Star_answerMapper;
import com.j2ee.po.Star_answer;
import com.j2ee.service.Star_answerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class Star_answerServiceImpl implements Star_answerService {
    @Autowired
    private Star_answerMapper star_answerMapper;
    @Override
    public List<Star_answer> findUserStarAnswer(String userID, int offSet, int pageSize) {
        return this.star_answerMapper.findUserStarAnswer(userID,offSet,pageSize);
    }

    @Override
    public int addAnswerStar(Star_answer star_answer) {
        return this.star_answerMapper.addAnswerStar(star_answer);
    }

    @Override
    public int deleteAnswerStar(Star_answer star_answer) {
        return this.star_answerMapper.deleteAnswerStar(star_answer);
    }

    @Override
    public int starOrNot(Star_answer star_answer) {
        return this.star_answerMapper.deleteAnswerStar(star_answer);
    }
}
