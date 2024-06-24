package com.hipop.hidada.config;

import com.zhipu.oapi.ClientV4;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Hipopaaaaa
 * @create 2024/6/11 15:53
 * ai配置类
 */

@Configuration
@ConfigurationProperties(prefix = "ai")
@Data
public class AiConfig {
     /**
      * apiKey，需要从开放平台获取
      */
     private String apiKey;

     @Bean
     public ClientV4 getClientV4(){
          return new ClientV4.Builder(this.apiKey).build();
     }
}
