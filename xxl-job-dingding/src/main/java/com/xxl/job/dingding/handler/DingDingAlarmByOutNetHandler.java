package com.xxl.job.dingding.handler;

import com.xxl.job.core.handler.DingDingAlarmHandler;
import com.xxl.job.dingding.http.HttpClient;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: wangm
 * @date: 2021/8/12 9:56
 * @Description
 */
@Slf4j
public class DingDingAlarmByOutNetHandler implements DingDingAlarmHandler{

    @Value("${dingDing-alarm-team-url}")
    private String dingUrl;

    @Value("${dingDing-alarm-isatAll}")
    private boolean isatAll;

    @Value("${dingDing-alarm-mobile}")
    private String mobiles;

    @Override
    public boolean handler(String message) {
        log.info("XXlJob调用开始");
        log.info(message);
        long start = System.currentTimeMillis();
        try {
            String code = URLDecoder.decode(message,"utf-8");
            String jsonStr = "错误调用:" +code.replace("=","");
            boolean result = DingMessageXxlJob(jsonStr);
            long end = System.currentTimeMillis();
            log.info("XXlJob调用结束,耗时:{}毫秒", end - start);
            return result;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return false;
    }

    /***
     * 推送到钉钉群
     */
    public boolean DingMessageXxlJob(String content) {
        if (isatAll) {
            content += "\n";
        }

        //消息内容
        Map<String, String> contentMap = new HashMap();
        contentMap.put("content", content);
        //通知人
        Map<String, Object> atMap = new HashMap();

        // 添加群聊@的用户
        if (StringUtils.isNotBlank(mobiles)) {

            List<String> mobile = new ArrayList<>();
            String[] mobileList = mobiles.split(",");
            for (int i = 0; i < mobileList.length; i++) {
                mobile.add(mobileList[i]);
            }
            atMap.put("atMobiles",mobile);
        }

        //是否通知所有人
        atMap.put("isAtAll", isatAll);
        JSONObject reqObj = new JSONObject();
        reqObj.put("msgtype", "text");
        reqObj.put("text", contentMap);
        reqObj.put("at", atMap);
        log.info("正在推送...");
        try {
            HttpClient.httpJsonPost(dingUrl, reqObj, false);
            log.info("推送完成");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            log.error("消息推送失败");
        }
        return false;
    }


}
