package com.xxl.job.admin.core.alarm.impl;

import com.xxl.job.admin.core.alarm.JobAlarm;
import com.xxl.job.admin.core.conf.XxlJobAdminConfig;
import com.xxl.job.admin.core.model.XxlJobGroup;
import com.xxl.job.admin.core.model.XxlJobInfo;
import com.xxl.job.admin.core.model.XxlJobLog;
import com.xxl.job.admin.core.util.I18nUtil;
import com.xxl.job.core.handler.DingDingAlarmHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: wangm
 * @date: 2021/8/3 15:28
 * @Description
 */
@Component
public class DingDingAlarm implements JobAlarm {

    private static Logger logger = LoggerFactory.getLogger(DingDingAlarm.class);

    @Autowired
    private DingDingAlarmHandler dingDingAlarmHandler;

    @Override
    public boolean doAlarm(XxlJobInfo info, XxlJobLog jobLog) {
        logger.info(">>>>>>>>> job ={} 执行失败, 发送钉钉通知,JobLogId={}", info.getId(), jobLog.getId());

        XxlJobGroup group = XxlJobAdminConfig.getAdminConfig().getXxlJobGroupDao().load(Integer.valueOf(info.getJobGroup()));

        StringBuffer messageStr = new StringBuffer();
        messageStr.append("执行器：").append(group.getTitle());
        messageStr.append(", 任务ID：").append(jobLog.getJobId());
        messageStr.append(", 任务描述：").append(info.getJobDesc());
        messageStr.append(", 告警类型：").append(I18nUtil.getString("jobconf_monitor_alarm_type"));

        String triggerMsg = processTriggerMsg(jobLog.getTriggerMsg());
        messageStr.append(", 告警内容：").append(triggerMsg);

        logger.info("send massage:{}", messageStr.toString());

        boolean result = dingDingAlarmHandler.handler(messageStr.toString());

        if (!result) {
            logger.error(">>>>>>>>>>> xxl-job, job fail alarm dingding send error, JobLogId:{}", jobLog.getId());
        }
        return  result;
    }

    /**
     * 处理告警内容
     * @param triggerMsg 告警内容
     * @return 处理后的告警内容
     */
    public String processTriggerMsg(String triggerMsg) {
        String result = triggerMsg.replaceAll("<br><br>"," ")
                .replaceAll("<br>", ", ")
                .replaceAll("<span style=\"color:#00c0ef;\" >", "")
                .replaceAll("</span>", "");

        return result;
    }

}