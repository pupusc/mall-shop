package com.wanmi.sbc.order.api.request.trade;

import com.wanmi.sbc.common.base.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * <p>根据父订单号获取订单集合参数</p>
 * Created by of628-wenzhi on 2019-07-22-15:24.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class TradeListByParentIdRequest extends BaseRequest {
    private static final long serialVersionUID = -9143745241530996245L;

    /**
     * 交易id
     */
    @NotNull
    @ApiModelProperty(value = "父交易单id")
    private String parentTid;
}
