package com.wanmi.sbc.order.bean.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 快递100的通知信息结构
 */
@Data
public class KuaiDiHundredNoticeLastResultDTO implements Serializable {

    /**
     * 消息体，请忽略
     */
    private String message;

    /**
     * 单号
     */
    private String nu;

    /**
     * 是否签收标记
     */
    private String ischeck;

    /**
     * 快递公司编码,一律用小写字母
     */
    private String com;

    /**
     * 通讯状态，请忽略
     */
    private String status;

    /**
     * 快递单当前状态，包括0在途，1揽收，2疑难，3签收，4退签，5派件，6退回等7个状态
     */
    private String state;

    /**
     * 快递单明细状态标记，暂未实现，请忽略
     */
    private String condition;

    /**
     * 数组，包含多个对象，每个对象字段如展开所示
     */
    private List<KuaiDiHundredNoticeResultItemDTO> data;


}
