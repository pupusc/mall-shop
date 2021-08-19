package com.wanmi.sbc.order.api.response.trade;

import com.wanmi.sbc.order.bean.vo.TradeItemGroupVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 获取客户id查询已确认订单商品快照响应结构
 * @Author: daiyitian
 * @Description:
 * @Date: 2018-11-16 16:39
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class TradeItemByCustomerIdResponse implements Serializable {

    private static final long serialVersionUID = -1076979847505660373L;

    /**
     * 店铺分组的订单商品快照列表
     */
    @ApiModelProperty(value = "店铺分组的订单商品快照列表")
    private List<TradeItemGroupVO> tradeItemGroupList;
}
