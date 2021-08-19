package com.wanmi.sbc.order.bean.dto;

import com.wanmi.sbc.common.base.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel
public class TradePriceChangeDTO extends BaseRequest{

    /**
     * 订单总价
     */
    @ApiModelProperty(value = "订单总价", required = true)
    @NotNull
    @Min(0L)
    private BigDecimal totalPrice;

    @ApiModelProperty(value = "运费")
    @Min(0L)
    private BigDecimal freight;
}
