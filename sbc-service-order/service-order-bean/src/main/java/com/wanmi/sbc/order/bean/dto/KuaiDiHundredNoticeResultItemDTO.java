package com.wanmi.sbc.order.bean.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 快递100的通知信息结构
 */
@Data
public class KuaiDiHundredNoticeResultItemDTO implements Serializable {

    /**
     * 内容
     */
    private String context;

    /**
     * 时间，原始格式
     */
    private String time;

    /**
     * 格式化后时间
     */
    private String ftime;

    /**
     * 本数据元对应的签收状态。
     */
    private String status;

    /**
     * 本数据元对应的行政区域的编码
     */
    private String areaCode;

    /**
     * 本数据元对应的行政区域的名称
     */
    private String areaName;
}
