package com.wanmi.sbc.customer.api.request.fandeng;


import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 发票推送接口
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FanDengInvoiceRequest {

    private String userId;
    private Integer businessId;

    private List<Item> orderExtendBOS = new ArrayList<>();

    @Data
    public static class Item{
        private String orderCode;
        private String product;
        private BigDecimal fee;
        private BigDecimal totalFee;
        private Integer count;
        private Date completeTime;
        private Integer productType;
        private Integer productNo;
        private Integer orderType;
        private String productIcoon;
    }

}
