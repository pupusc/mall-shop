package com.wanmi.sbc.order.api.response.trade;

import com.wanmi.sbc.order.bean.vo.PayOrderVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2018-12-06 16:17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class TradeAddReceivableResponse implements Serializable {

    /**
     *支付单
     */
    @ApiModelProperty(value = "支付单")
    private PayOrderVO payOrder;

}
