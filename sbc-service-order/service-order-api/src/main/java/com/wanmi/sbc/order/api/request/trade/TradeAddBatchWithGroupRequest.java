package com.wanmi.sbc.order.api.request.trade;

import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.order.bean.dto.TradeDTO;
import com.wanmi.sbc.order.bean.dto.TradeGroupDTO;
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
 * @Date: 2018-12-05 9:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class TradeAddBatchWithGroupRequest implements Serializable {

    /**
     * 交易单
     */
    @ApiModelProperty(value = "交易单")
    private List<TradeDTO> tradeDTOList;

    /**
     * 交易分组信息
     */
    @ApiModelProperty(value = "交易分组信息")
    private TradeGroupDTO tradeGroup;

    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    private Operator operator;
}
