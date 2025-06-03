package com.xxl.job.dingding.config;

import com.xxl.job.core.handler.DingDingAlarmHandler;
import com.xxl.job.dingding.handler.DingDingAlarmByOutNetHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: wangm
 * @date: 2021/8/12 9:55
 * @Description
 */
@Configuration
public class XxlJobDingDingAutoConfiguration {

    @Bean("dingDingAlarmByOutNetHandler")
    @ConditionalOnProperty(name = "dingDing-alarm-type", havingValue = "outNet")
    public DingDingAlarmHandler dingDingAlarmHandler(){
        return new DingDingAlarmByOutNetHandler();
    }

}
