package com.wanmi.sbc.open.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class LogisticsResVO {
    /**
     * 物流配送方式编号
     */
    private String shipMethodId;

    /**
     * 物流配送方式名称
     */
    private String shipMethodName;

    /**
     * 物流号
     */
    private String logisticNo;

    /**
     * 物流费
     */
    private BigDecimal logisticFee;

    /**
     * 物流公司编号
     */
    private String logisticCompanyId;

    /**
     * 物流公司名称
     */
    private String logisticCompanyName;

    /**
     * 物流公司标准编码
     */
    private String logisticStandardCode;

    /**
     * 购买用户id
     */
    private String buyerId;
}
