package com.xxl.job.executor.model;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;


/**
 * api接口code
 *
 * @author liuz
 * @date 2024-12-10
 */
@Data
@TableName("api_token")
public class APIToken {

    /**
     * app_code
     */

    @TableId
    private String app_code;

    /**
     * access_token
     */

    private String token;

    /**
     * 插入时间
     */
    private Date insert_time;

    /**
     * 修改时间
     */
    private Date modify_time;

    public APIToken(String app_code, String token, Date insert_time, Date modify_time) {
        this.app_code = app_code;
        this.token = token;
        this.insert_time = insert_time;
        this.modify_time = modify_time;
    }
}
