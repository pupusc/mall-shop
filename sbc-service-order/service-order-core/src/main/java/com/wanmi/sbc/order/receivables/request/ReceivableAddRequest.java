package com.wanmi.sbc.order.receivables.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;

/**
 * 新增收款单参数
 * Created by zhangjin on 2017/4/27.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReceivableAddRequest implements Serializable {

    /**
     * 支付单id
     */
    @NotEmpty
    private String payOrderId;

    /**
     * 收款单时间
     */
    @NotEmpty
    private String createTime;

    /**
     * 评价
     */
    private String comment;

    /**
     * 收款账号
     */
    private Long accountId;

    /**
     * 线上支付渠道描述，在线支付必传
     */
    private String payChannel;

    /**
     * 线上支付渠道id，在线支付必传
     */
    private Long payChannelId;

    /**
     * 附件
     */
    private String encloses;
}
