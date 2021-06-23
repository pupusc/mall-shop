package com.wanmi.sbc.order.api.response.trade;

import com.wanmi.sbc.order.bean.vo.TradeDeliverVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p></p>
 * author: sunkun
 * Date: 2018-12-11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel
public class TradeDeliverRecordResponse implements Serializable {

    private static final long serialVersionUID = 8094523587595462766L;

    /**
     * 发货记录
     */
    @ApiModelProperty(value = "发货记录列表")
    private List<TradeDeliverVO> tradeDeliver = new ArrayList<>();

    /**
     * 订单总体状态
     */
    @ApiModelProperty(value = "订单总体状态")
    private String status;

    /**
     * 有赞订单id
     */
    @ApiModelProperty(value = "有赞订单id")
    private String yzTid;

    /**
     * 是否为周期购的订单
     */
    @ApiModelProperty(value = "是否为周期购的订单")
    private Boolean cycleBuyFlag;

}
