package com.hipop.hidada;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hipop.hidada.model.entity.UserAnswer;
import com.hipop.hidada.service.UserAnswerService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Hipopaaaaa
 * @create 2024/6/14 11:33
 */
@SpringBootTest
public class UserAnswerShardingTest {

    @Resource
    private UserAnswerService userAnswerService;

    @Test
    void test(){
        UserAnswer userAnswer3 = new UserAnswer();
        userAnswer3.setAppId(3L);
        userAnswer3.setUserId(3L);
        userAnswer3.setChoices("3");
        userAnswerService.save(userAnswer3);

        UserAnswer userAnswer4 = new UserAnswer();
        userAnswer4.setAppId(4L);
        userAnswer4.setUserId(1L);
        userAnswer4.setChoices("4");
        userAnswerService.save(userAnswer4);

        List<UserAnswer> userAnswerList = userAnswerService.list();
        System.out.println(JSONUtil.toJsonStr(userAnswerList));

        UserAnswer one = userAnswerService.getOne(Wrappers.lambdaQuery(UserAnswer.class).eq(UserAnswer::getAppId, 3L));
        System.out.println(JSONUtil.toJsonStr(one));


        UserAnswer two = userAnswerService.getOne(Wrappers.lambdaQuery(UserAnswer.class).eq(UserAnswer::getAppId, 4L));
        System.out.println(JSONUtil.toJsonStr(two));
    }
}
