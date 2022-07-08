package com.xxl.job.admin.core.alarm.impl;

import com.xxl.job.core.handler.DingDingAlarmHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

/**
 * @author: wangm
 * @date: 2021/8/12 9:39
 * @Description xxl-job 部署到内网时需要发送处于外网系统的接实现推送
 */
@Configuration
@ConditionalOnMissingBean(name = "dingDingAlarmByOutNetHandler")
@ConditionalOnProperty(name = "dingDing-alarm-type", havingValue = "innerNet")
public class DingDingAlarmByInnerNetHandler implements DingDingAlarmHandler {

    @Value("${dingding.addressUrl}")
    private String dingDingAddress;

    @Autowired
    private RestTemplate restTemplate;

    private static final String SUCCESS_CODE = "success";

    @Override
    public boolean handler(String message) {

        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/x-www-form-urlencoded; charset=UTF-8");
        headers.setContentType(type);

        HttpEntity<String> requestEntity = new HttpEntity<String>(message,  headers);

        String msg = restTemplate.postForObject(dingDingAddress,requestEntity, String.class);

        if (SUCCESS_CODE.equals(msg)) {
            return true;
        }
        return false;
    }

}
