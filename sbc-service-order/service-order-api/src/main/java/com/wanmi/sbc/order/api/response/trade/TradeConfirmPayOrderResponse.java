package com.wanmi.sbc.order.api.response.trade;

import com.wanmi.sbc.order.bean.vo.PayOrderVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2018-12-06 16:34
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class TradeConfirmPayOrderResponse implements Serializable {

    /**
     * 支付单列表
     */
    @ApiModelProperty(value = "支付单列表")
    private List<PayOrderVO> payOrderVOList;

}
