package com.wanmi.sbc.order.api.request.settlement;

import com.wanmi.sbc.order.api.request.OrderBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>单条结算明细查询条件</p>
 * Created by of628-wenzhi on 2018-10-13-下午6:56.
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SettlementDetailByParamRequest extends OrderBaseRequest {
    private static final long serialVersionUID = 6042145653592194055L;

    /**
     * 订单id
     */
    @ApiModelProperty(value = "订单id")
    private String tradeId;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    private String startDate;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private String endDate;
}
