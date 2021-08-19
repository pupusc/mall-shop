package com.wanmi.sbc.pay.api.response;

import com.wanmi.sbc.pay.bean.enums.TradeStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>支付结果查询response</p>
 * Created by of628-wenzhi on 2018-08-14-下午6:41.
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayResultResponse implements Serializable{
    private static final long serialVersionUID = -6197336208494247645L;

    /**
     * 支付状态
     */
    @ApiModelProperty(value = "支付状态")
    private TradeStatus tradeStatus;
}
