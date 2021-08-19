package com.wanmi.sbc.message.pushUtil.root;

import com.wanmi.sbc.message.bean.enums.PushPlatform;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: sbc-micro-service
 * @description: 友盟推送接口返回对象
 * @create: 2020-01-08 19:17
 **/
@Data
public class PushResultEntry implements Serializable {
    // 接口状态 SUCCESS:成功 FAIL：失败
    private String ret;
    // 任务ID
    private String taskId;

    // 失败原因
    private String errorMsg;

    // iOS、android
    private PushPlatform platform;
}