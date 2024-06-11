package com.hipop.hidada.manager;

import com.hipop.hidada.common.ErrorCode;
import com.hipop.hidada.exception.BusinessException;
import com.zhipu.oapi.ClientV4;
import com.zhipu.oapi.Constants;
import com.zhipu.oapi.service.v4.model.ChatCompletionRequest;
import com.zhipu.oapi.service.v4.model.ChatMessage;
import com.zhipu.oapi.service.v4.model.ChatMessageRole;
import com.zhipu.oapi.service.v4.model.ModelApiResponse;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Hipopaaaaa
 * @create 2024/6/11 16:04
 * 通用ai调用
 */
@Component
public class AiManager {

    @Resource
    private ClientV4 clientV4;

    // 稳定的随机数
    private static final float STABLE_TEMPERATURE=0.05f;
    // 不稳定的随机数
    private static final float UNSTABLE_TEMPERATURE=0.99f;

    /**
     * 同步请求(答案不稳定)
     * @param systemMessage
     * @param userMessage
     * @return
     */
    public String doSyncUnStableRequest(String systemMessage,String userMessage) {
        return  doRequest(systemMessage,userMessage,Boolean.FALSE,UNSTABLE_TEMPERATURE);
    }

    /**
     * 同步请求(答案稳定)
     * @param systemMessage
     * @param userMessage
     * @return
     */
    public String doSyncStableRequest(String systemMessage,String userMessage) {
        return  doRequest(systemMessage,userMessage,Boolean.FALSE,STABLE_TEMPERATURE);
    }
    /**
     * 同步请求(简化消息传递)
     * @param systemMessage
     * @param userMessage
     * @param temperature
     * @return
     */
    public String doSyncRequest(String systemMessage,String userMessage,Float temperature) {
        return  doRequest(systemMessage,userMessage,Boolean.FALSE,temperature);
    }
    /**
     * 通用请求(简化消息传递)
     * @param systemMessage
     * @param userMessage
     * @param stream
     * @param temperature
     * @return
     */
    public String doRequest(String systemMessage,String userMessage, Boolean stream,Float temperature) {
        List<ChatMessage> chatMessageList =new ArrayList<>();
        ChatMessage sysChatMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), systemMessage);
        chatMessageList.add(sysChatMessage);
        ChatMessage userChatMessage = new ChatMessage(ChatMessageRole.USER.value(), userMessage);
        chatMessageList.add(userChatMessage);
        return  doRequest(chatMessageList,stream,temperature);
    }

    /**
     * 通用请求
     * @param messages
     * @param stream
     * @param temperature
     * @return
     */
    public String doRequest(List<ChatMessage> messages, Boolean stream,Float temperature) {
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model(Constants.ModelChatGLM4)
                .stream(stream)
                .temperature(temperature)
                .invokeMethod(Constants.invokeMethod)
                .messages(messages).build();
        try {
            ModelApiResponse invokedModelApi = clientV4.invokeModelApi(chatCompletionRequest);
            return  invokedModelApi.getData().getChoices().get(0).toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,e.getMessage());
        }
    }

}
