package com.hipop.hidada.model.dto.question;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Hipopaaaaa
 * @create 2024/6/11 16:47
 * AI 生成题目请求
 */
@Data
public class AiGenerateQuestionRequest implements Serializable {
    private Long appId;
    private int questionNumber=10;
    private int optionNumber=2;

    private static final long serialVersionUID = 1L;
}
