package com.wanmi.sbc.order.bean.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 快递100的通知信息结构
 */
@Data
public class KuaidiHundredNoticeDTO implements Serializable {

    /**
     * id
     */
    private String id;


    /**
     * 监控状态:polling:监控中，shutdown:结束，abort:中止，updateall：重新推送。
     * 其中当快递单为已签收时status=shutdown，
     * 当message为“3天查询无记录”或“60天无变化时”status= abort ，
     * 对于stuatus=abort的状度，需要增加额外的处理逻辑
     */
    private String status;

    /**
     * 包括got、sending、check三个状态，由于意义不大，已弃用，请忽略
     */
    private String billstatus;

    /**
     * 监控状态相关消息，如:3天查询无记录，60天无变化
     */
    private String message;

    /**
     * 快递公司编码是否出错
     */
    private String autoCheck;

    /**
     * 贵司提交的原始的快递公司编码
     */
    private String comOld;

    /**
     * 我司纠正后的新的快递公司编码
     */
    private String comNew;

    /**
     * 最新查询结果体
     */
    private KuaiDiHundredNoticeLastResultDTO lastResult;
}
