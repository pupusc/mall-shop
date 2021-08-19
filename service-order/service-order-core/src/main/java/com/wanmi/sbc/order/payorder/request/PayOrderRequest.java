package com.wanmi.sbc.order.payorder.request;


import com.wanmi.sbc.account.bean.enums.PayOrderStatus;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 收款单查询条件
 * Created by zhangjin on 2017/4/20.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PayOrderRequest extends BaseQueryRequest {

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 收款流水号
     */
    private String payBillNo;

    /**
     * 支付方式 0线上 1线下
     */
    private Integer payType;

    /**
     * 在支付渠道id
     */
    private Integer payChannelId;
    /**
     * 付款状态
     */
    private PayOrderStatus payOrderStatus;

    /**
     * 收款开始时间
     */
    private String startTime;

    /**
     * 收款结束时间
     */
    private String endTime;

    /**
     * 收款单主键
     */
    private List<String> payOrderIds;

    /**
     * 收款账户id
     */
    private String accountId;

    private String token;

    /**
     * 是否根据收款时间排序
     */
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
    private String supplierName;

    /**
     * 商家id
     */
    private String companyInfoId;

    /**
     * 多个商家ids
     */
    private List<Long> companyInfoIds;

    /**
     * 多个会员详细ids
     */
    private List<String> customerDetailIds;

    /**
     * 收款账号账户名称
     */
    private String account;

    /**
     * 多个收款账户id
     */
    private List<Long> accountIds;

    /**
     * 模糊查询order字段
     */
    private String orderCode;


    /**
     * 订单号-批量
     */
    private List<String> orderNoList;
}
