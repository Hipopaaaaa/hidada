package com.hipop.hidada.scoring;

import com.hipop.hidada.common.ErrorCode;
import com.hipop.hidada.exception.BusinessException;
import com.hipop.hidada.model.entity.App;
import com.hipop.hidada.model.entity.UserAnswer;
import com.hipop.hidada.model.enums.AppScoringStrategyEnum;
import com.hipop.hidada.model.enums.AppTypeEnum;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Hipopaaaaa
 * @create 2024/6/4 14:52
 * 评分执行器(旧版)
 */
@Deprecated
public class ScoringStrategyContext {
    @Resource
    private CustomTestScoringStrategy customTestScoringStrategy;
    @Resource
    private CustomScoreScoringStrategy customScoreScoringStrategy;

    public UserAnswer doScore(List<String> choiceList, App app) throws Exception {
        AppTypeEnum appTypeEnum = AppTypeEnum.getEnumByValue(app.getAppType());
        AppScoringStrategyEnum appScoringStrategyEnum = AppScoringStrategyEnum.getEnumByValue(app.getScoringStrategy());
        // 根据不同的应用类别和评分策略，选择对应的策略执行
        switch (appTypeEnum){
            case SCORE:
                switch (appScoringStrategyEnum){
                    case CUSTOM:
                        return customScoreScoringStrategy.doScore(choiceList,app);
                    case AI:
                        break;
                }
                break;
            case TEST:
                switch (appScoringStrategyEnum){
                    case CUSTOM:
                        return customTestScoringStrategy.doScore(choiceList, app);
                    case AI:
                        break;
                }
                break;

        }
        throw new BusinessException(ErrorCode.SYSTEM_ERROR,"应用配置有误，未找到匹配的策略");
    }
}
