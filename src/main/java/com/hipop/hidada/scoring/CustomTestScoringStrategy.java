package com.hipop.hidada.scoring;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hipop.hidada.model.dto.question.QuestionContentDTO;
import com.hipop.hidada.model.entity.App;
import com.hipop.hidada.model.entity.Question;
import com.hipop.hidada.model.entity.ScoringResult;
import com.hipop.hidada.model.entity.UserAnswer;
import com.hipop.hidada.model.vo.QuestionVO;
import com.hipop.hidada.service.AppService;
import com.hipop.hidada.service.QuestionService;
import com.hipop.hidada.service.ScoringResultService;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Hipopaaaaa
 * @create 2024/6/4 14:10
 * 自定义测评类应用评分策略
 */
@ScoringStrategyConfig(appType = 1,scoringStrategy = 0)
public class CustomTestScoringStrategy implements ScoringStrategy {

    @Resource
    private QuestionService questionService;

    @Resource
    private ScoringResultService scoringResultService;

    @Override
    public UserAnswer doScore(List<String> choices, App app) throws Exception {
        // 1.查询题目和题目结果信息
        Long appId = app.getId();
        Question question = questionService.getOne(Wrappers.lambdaQuery(Question.class).eq(Question::getAppId, appId));
        List<ScoringResult> scoringResultList = scoringResultService.list(Wrappers.lambdaQuery(ScoringResult.class).eq(ScoringResult::getAppId, appId));
        // 2.统计用户每个选择对应的属性个数
        // 初始化一个Map，用于存储每个选项的计数
        Map<String,Integer> optionCount=new HashMap<>();

        QuestionVO questionVO = QuestionVO.objToVo(question);
        List<QuestionContentDTO> questionContent = questionVO.getQuestionContent();

        // 遍历题目列表
        for (QuestionContentDTO questionContentDTO : questionContent) {
            // 遍历答案列表
            for (String answer : choices) {
                // 遍历题目中的选项
                for (QuestionContentDTO.Option option : questionContentDTO.getOptions()) {
                    // 若答案的key 与 选项key 匹配，则计数+1
                    if(option.getKey().equals(answer)){
                        String result = option.getResult();
                        if(!optionCount.containsKey(result)){
                            optionCount.put(result,0);
                        }
                        optionCount.put(result,optionCount.get(result)+1);
                    }
                }
            }
        }

        // 3.遍历每种评分结果，计算哪个结果的得分更高
        // 初始化最高分数和最高分数对应的评分结果
        int maxScore=0;
        ScoringResult maxScoringResult = scoringResultList.get(0);

        // 遍历评分结果列表
        for (ScoringResult scoringResult : scoringResultList) {
            List<String> resultPropList = JSONUtil.toList(scoringResult.getResultProp(), String.class);
            int score = resultPropList.stream()
                    .mapToInt(prop -> optionCount.getOrDefault(prop, 0))
                    .sum();
            // 更新最高分和对应的评分结果
            if(score>maxScore){
                maxScore=score;
                maxScoringResult=scoringResult;
            }
        }

        // 4.构造返回值，填充答案对象
        UserAnswer userAnswer = new UserAnswer();
        userAnswer.setAppId(appId);
        userAnswer.setAppType(app.getAppType());
        userAnswer.setScoringStrategy(app.getScoringStrategy());
        userAnswer.setChoices(JSONUtil.toJsonStr(choices));
        userAnswer.setResultId(maxScoringResult.getId());
        userAnswer.setResultName(maxScoringResult.getResultName());
        userAnswer.setResultDesc(maxScoringResult.getResultDesc());
        userAnswer.setResultPicture(maxScoringResult.getResultPicture());
        return userAnswer;
    }
}
