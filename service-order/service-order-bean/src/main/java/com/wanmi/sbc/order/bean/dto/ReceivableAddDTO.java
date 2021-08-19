package com.wanmi.sbc.order.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel
public class ReceivableAddDTO implements Serializable {

    /**
     * 支付单id
     */
    @ApiModelProperty(value = "支付单id", required = true)
    @NotEmpty
    private String payOrderId;

    /**
     * 收款单时间
     */
    @ApiModelProperty(value = "收款单时间", required = true)
    @NotEmpty
    private String createTime;

    /**
     * 评价
     */
    @ApiModelProperty(value = "评价")
    private String comment;

    /**
     * 收款账号
     */
    @ApiModelProperty(value = "收款账号")
    private Long accountId;

    /**
     * 线上支付渠道描述，在线支付必传
     */
    @ApiModelProperty(value = "线上支付渠道描述，在线支付必传")
    private String payChannel;

    /**
     * 线上支付渠道id，在线支付必传
     */
    @ApiModelProperty(value = "线上支付渠道id，在线支付必传")
    private Long payChannelId;

    /**
     * 附件
     */
    @ApiModelProperty(value = "附件")
    private String encloses;
}
