package com.wanmi.sbc.erp.api.req;

import com.wanmi.sbc.common.base.Page;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Date;

@Data
public class SaleAfterReq implements Serializable {
    /**
     * 商户ID
     */
    private Long shopId;

    /**
     * 渠道订单号
     */
    private String platformOrderId;

    /**
     * 订单号
     */
    private Long orderNumber;

    /**
     * 子订单号
     */
    private Long orderItemTid;

    /**
     * 渠道下单时间-区间-开始
     * 时间区间通常为 yyyy:MM:dd 00:00:00 - yyyy:MM:dd 23:59:59
     */
    private Date bookTimeStart;

    /**
     * 渠道下单时间-区间-结束
     * 时间区间通常为 yyyy:MM:dd 00:00:00 - yyyy:MM:dd 23:59:59
     */
    private Date bookTimeEnd;

    /**
     * 订单创建时间-区间-开始
     * 时间区间通常为 yyyy:MM:dd 00:00:00 - yyyy:MM:dd 23:59:59
     */
    private Date createTimeStart;

    /**
     * 订单创建时间-区间-结束
     * 时间区间通常为 yyyy:MM:dd 00:00:00 - yyyy:MM:dd 23:59:59
     */
    private Date createTimeEnd;

    /**
     * 售后单创建时间-区间-开始
     * 时间区间通常为 yyyy:MM:dd 00:00:00 - yyyy:MM:dd 23:59:59
     */
    private Date saCreateTimeStart;

    /**
     * 售后单创建时间-区间-结束
     * 时间区间通常为 yyyy:MM:dd 00:00:00 - yyyy:MM:dd 23:59:59
     */
    private Date saCreateTimeEnd;

    /**
     * 售后状态
     *
     * @see com.soybean.unified.order.api.enums.UnifiedSaleAfterStatusEnum
     */
    private Integer status;


    /**
     * 售后业务类型
     *
     * @see com.soybean.unified.order.api.enums.SaleAfterRefundTypeEnum
     */
    private Integer refundType;

    /**
     * 收货手机号
     */
    private String contactMobile;


    /**
     * 分页参数
     */
    private Page page = new Page(1, 10);

    /**
     * 用户ID
     */
    private Long userId;

    public String getPlatformOrderId() {
        return StringUtils.isBlank(platformOrderId) ? null : platformOrderId;
    }

    public String getContactMobile() {
        return StringUtils.isBlank(contactMobile) ? null : contactMobile;
    }
}
