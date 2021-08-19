package com.wanmi.sbc.order.api.request.payorder;

import com.wanmi.sbc.account.bean.enums.PayOrderStatus;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;



@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SumPayOrderPriceRequest extends BaseQueryRequest {

    /**
     * 客户名称
     */
    @ApiModelProperty(value = "客户名称")
    private String customerName;

    /**
     * 订单号
     */
    @ApiModelProperty(value = "订单号")
    private String orderNo;

    /**
     * 收款流水号
     */
    @ApiModelProperty(value = "收款流水号")
    private String payBillNo;

    /**
     * 支付方式 0线上 1线下
     */
    @ApiModelProperty(value = "支付方式" ,dataType = "com.wanmi.sbc.account.bean.enums.PayType")
    private Integer payType;

    /**
     * 在支付渠道
     */
    @ApiModelProperty(value = "在支付渠道",dataType = "com.wanmi.sbc.pay.api.enums.PayGatewayEnum")
    private Integer payChannelId;

    /**
     * 付款状态
     */
    @ApiModelProperty(value = "支付状态")
    private PayOrderStatus payOrderStatus;

    /**
     * 收款开始时间
     */
    @ApiModelProperty(value = "收款开始时间")
    private String startTime;

    /**
     * 收款结束时间
     */
    @ApiModelProperty(value = "收款结束时间")
    private String endTime;

    /**
     * 收款单主键
     */
    @ApiModelProperty(value = "收款单主键")
    private List<String> payOrderIds;

    /**
     * 收款账户id
     */
    @ApiModelProperty(value = "收款账户id")
    private String accountId;

    @ApiModelProperty(value = "token")
    private String token;

    /**
     * 是否根据收款时间排序
     */
    @ApiModelProperty(value = "是否根据收款时间排序")
    private Boolean sortByReceiveTime = false;

    /**
     * 解决默认值为null导致空指针
     * @return
     */
    public Boolean getSortByReceiveTime() {
        if(this.sortByReceiveTime == null){
            return false;
        }
        return sortByReceiveTime;
    }

    /**
     * 商家名称
     */
    @ApiModelProperty(value = "商家名称")
    private String supplierName;

    /**
     * 商家id
     */
    @ApiModelProperty(value = "商家id")
    private String companyInfoId;

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

    /**
     * 收款账号账户名称
     */
    @ApiModelProperty(value = "收款账号账户名称")
    private String account;

    /**
     * 多个收款账户id
     */
    @ApiModelProperty(value = "多个收款账户id")
    private List<Long> accountIds;

    /**
     * 模糊查询order字段
     */
    @ApiModelProperty(value = "模糊查询order字段")
    private String orderCode;
}
