package com.wanmi.sbc.order.api.request.settlement;

import com.wanmi.sbc.order.api.request.OrderBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>根据结算单id查询结算明细列表条件参数</p>
 * Created by of628-wenzhi on 2018-10-13-下午6:56.
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SettlementDetailListBySettleUuidRequest extends OrderBaseRequest {

    private static final long serialVersionUID = 6127833800267846213L;

    /**
     * 结算单id
     */
    @ApiModelProperty(value = "结算单id")
    private String settleUuid;
}
