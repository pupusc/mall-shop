package com.wanmi.sbc.order.api.request.settlement;

import com.wanmi.sbc.order.bean.dto.SettlementDetailDTO;
import com.wanmi.sbc.order.api.request.OrderBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>新增单条结算明细request</p>
 * Created by of628-wenzhi on 2018-10-13-下午6:29.
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SettlementDetailAddRequest extends OrderBaseRequest {
    private static final long serialVersionUID = -3013009710823545987L;

    /**
     * 结算明细 {@link SettlementDetailDTO}
     */
    @ApiModelProperty(value = "结算明细")
    private SettlementDetailDTO settlementDetailDTO;
}
