package com.wanmi.sbc.order.api.response.trade;

import com.wanmi.sbc.order.bean.vo.ProviderTradeVO;
import com.wanmi.sbc.order.bean.vo.TradeVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.security.Provider;
import java.util.List;

/**
 * zhongjichuan
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class ProviderTradeGetByIdsResponse implements Serializable {

    private static final long serialVersionUID = 7238688458136285439L;
    /**
     * 订单对象
     */
    @ApiModelProperty(value = "订单对象")
    private List<ProviderTradeVO> providerTradeVOList;


}
