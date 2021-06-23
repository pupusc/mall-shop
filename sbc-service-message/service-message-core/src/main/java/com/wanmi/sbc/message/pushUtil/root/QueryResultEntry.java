package com.wanmi.sbc.message.pushUtil.root;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: sbc-micro-service
 * @description: 友盟查询接口返回对象
 * @create: 2020-01-08 20:09
 * 接口说明及错误码：https://developer.umeng.com/docs/66632/detail/68343#h2--i-15
 **/
@Data
public class QueryResultEntry implements Serializable {
    // 接口状态 SUCCESS:成功 FAIL:失败
    private String ret;
    // 任务ID
    private String taskId;
    // 消息状态 0-排队中, 1-发送中，2-发送完成，3-发送失败，4-消息被撤销，5-消息过期, 6-筛选结果为空，7-定时任务尚未开始处理
    private int status;
    // iOS、android消息收到数
    private int sentCount;
    // iOS、android打开数
    private int openCount;
    // android忽略数
    private int dismissCount;
    // iOS消息，投递APNs设备数
    private int totalCount;
    // 错误码详见附录I
    private String errorCode;
    // 错误码详见附录I
    private String errorMsg;
}