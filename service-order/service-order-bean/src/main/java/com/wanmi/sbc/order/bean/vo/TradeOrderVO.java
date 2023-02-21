package com.wanmi.sbc.order.bean.vo;


import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class TradeOrderVO implements Serializable {

    private String tradeOrderId;

    private String customerId;

    private String customerAccount;

    private Integer supplierId;

    private Integer storeId;

    private String auditState;

    private String flowState;

    private String payState;

    private String deliverStatus;

    private LocalDateTime createTime;

    private String oId;

    private String spuId;

    private String skuId;

    private String skuName;

    private String skuNo;

    private Integer cateId;

    private Integer brandId;
}
