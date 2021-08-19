package com.wanmi.sbc.order.orderinvoice.request;


import com.wanmi.sbc.account.bean.enums.InvoiceType;
import lombok.Data;

import java.io.Serializable;

/**
 * 订单发票传参
 * Created by zhangjin on 2017/5/7.
 */
@Data
public class OrderInvoiceSaveRequest implements Serializable{

    /**
     * 订单开票ID
     */
    private String orderInvoiceId;

    /**
     * 发票类型
     */
    private InvoiceType invoiceType;

    /**
     * 开票项id
     */
    private String projectId;

    /**
     * 用户id
     */
    private String customerId;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 发票title
     */
    private String invoiceTitle;

    /**
     * 发票地址
     */
    private String invoiceAddress;

    /**
     * 开票时间
     */
    private String invoiceTime;

    /**
     * 收货人
     */
    private String contacts;

    /**
     * 收货人联系号码
     */
    private String phone;

    /**
     * 收货地址
     */
    private String address;

    /**
     * 商家id
     */
    private Long companyInfoId = 0L;

    /**
     * 店铺id
     */
    private Long storeId = 0L;

    /**
     * 纳税人识别号
     */
    private String taxpayerNumber;

    /**
     * 订单开票收货地址
     */
    private String addressInfoId;

}
