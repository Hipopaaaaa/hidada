package com.hipop.hidada.scoring;

import com.hipop.hidada.model.entity.App;
import com.hipop.hidada.model.entity.UserAnswer;

import java.util.List;

/**
 * @author Hipopaaaaa
 * @create 2024/6/4 14:08
 * 评分策略
 */
public interface ScoringStrategy {
     /**
      * 进行评分
      * @param choices
      * @param app
      * @return
      * @throws Exception
      */
     UserAnswer doScore(List<String> choices, App app) throws Exception;
}
