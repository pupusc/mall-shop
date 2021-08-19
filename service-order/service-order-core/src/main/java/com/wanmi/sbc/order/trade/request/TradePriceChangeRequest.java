package com.wanmi.sbc.order.trade.request;

import com.wanmi.sbc.common.base.BaseRequest;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * <p>订单改价请求参数结构</p>
 * Created by of628-wenzhi on 2018-05-31-下午3:25.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TradePriceChangeRequest extends BaseRequest{
    private static final long serialVersionUID = -3172243547609640679L;

    /**
     * 订单总价
     */
    @NotNull
    @Min(0L)
    private BigDecimal totalPrice;

    @Min(0L)
    private BigDecimal freight;
}
