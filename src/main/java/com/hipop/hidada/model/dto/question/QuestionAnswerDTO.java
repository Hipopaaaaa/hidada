package com.hipop.hidada.model.dto.question;

import lombok.Data;

/**
 * @author Hipopaaaaa
 * @create 2024/6/11 21:55
 * 题目答案封装类
 */
@Data
public class QuestionAnswerDTO {

    /**
     * 题目
     */
    private String title;
    /**
     * 用户答案
     */
    private String userAnswer;
}
