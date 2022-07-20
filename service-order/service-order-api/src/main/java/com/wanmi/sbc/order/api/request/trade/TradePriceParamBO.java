package com.wanmi.sbc.order.api.request.trade;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Liang Jun
 * @date 2022-06-28 13:40:00
 */
@Data
public class TradePriceParamBO {
    private String customerId;
    private String couponId;
    @NotEmpty
    private List<GoodsInfo> goodsInfos;

    @Data
    public static class GoodsInfo {
        @NotBlank
        private String goodsInfoId;
        @NotNull
        private Long buyCount;
        private Long marketingId;
    }
}
