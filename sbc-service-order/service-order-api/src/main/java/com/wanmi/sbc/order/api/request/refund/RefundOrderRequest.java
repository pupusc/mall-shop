package com.wanmi.sbc.order.api.request.refund;


import com.wanmi.sbc.account.bean.enums.RefundStatus;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


/**
 * 退款单
 * Created by zhangjin on 2017/4/21.
 */
@Data
@ApiModel
public class RefundOrderRequest extends BaseQueryRequest {
    /**
     * 客户名称
     */
    @ApiModelProperty(value = "客户名称")
    private String customerName;

    /**
     * 退单编号
     */
    @ApiModelProperty(value = "退单编号")
    private String returnOrderCode;

    /**
     * 退单编号列表
     */
    @ApiModelProperty(value = "退单编号列表")
    private List<String> returnOrderCodes;

    /**
     * 收款流水号
     */
    @ApiModelProperty(value = "收款流水号")
    private String refundBillCode;

    /**
     * 支付渠道id
     */
    @ApiModelProperty(value = "支付渠道id",dataType = "com.wanmi.sbc.pay.api.enums.PayGatewayEnum")
    private Integer payChannelId;

    /**
     * 0 在线支付 1线下支付
     */
    @ApiModelProperty(value = "支付方式",dataType = "com.wanmi.sbc.account.bean.enums.PayType")
    private Integer payType;

    /**
     * 退款单状态
     */
    private RefundStatus refundStatus;

    /**
     * 账号id
     */
    @ApiModelProperty(value = "账号id")
    private String accountId;

    /**
     * 退款单主键
     */
    @ApiModelProperty(value = "退款单主键")
    private List<String> refundIds;

    /**
     * token
     */
    @ApiModelProperty(value = "token")
    private String token;

    /**
     * 查询退款开始时间，精确到天
     */
    @ApiModelProperty(value = "查询退款开始时间，精确到天")
    private String beginTime;

    /**
     * 查询退款结束时间，精确到天
     */
    @ApiModelProperty(value = "查询退款结束时间，精确到天")
    private String endTime;

    /**
     * 商家名称
     */
    @ApiModelProperty(value = "商家名称")
    private String supplierName;

    /**
     * 商家编码
     */
    @ApiModelProperty(value = "商家编码")
    private Long supplierId;

    /**
     * 多个商家ids
     */
    @ApiModelProperty(value = "多个商家ids")
    private List<Long> companyInfoIds;

    /**
     * 多个会员详细ids
     */
    @ApiModelProperty(value = "多个会员详细ids")
    private List<String> customerDetailIds;
}
