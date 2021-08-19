package com.wanmi.sbc.order.bean.vo;

import com.wanmi.sbc.account.bean.vo.SettleGoodVO;
import com.wanmi.sbc.account.bean.vo.SettleTradeVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by hht on 2017/12/6.
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SettlementDetailVO {

    /**
     * 主键
     */
    @ApiModelProperty(value = "结算id")
    private String id;

    /**
     * 结算明细Id
     */
    @ApiModelProperty(value = "结算明细Id")
    private String settleUuid;

    /**
     * 账期开始时间
     */
    @ApiModelProperty(value = "账期开始时间")
    private String startTime;

    /**
     * 账期结束时间
     */
    @ApiModelProperty(value = "账期结束时间")
    private String endTime;

    /**
     * 店铺Id
     */
    @ApiModelProperty(value = "店铺Id")
    private Long storeId;

    /**
     * 是否特价
     */
    @ApiModelProperty(value = "是否特价")
    private boolean isSpecial;

    /**
     * 订单信息
     */
    @ApiModelProperty(value = "订单信息")
    private SettleTradeVO settleTrade;

    /**
     * 订单商品信息
     */
    @ApiModelProperty(value = "订单商品信息")
    private List<SettleGoodVO> settleGoodList;

    /**
     * 订单和退单是否属于同一个账期
     */
    @ApiModelProperty(value = "订单和退单是否属于同一个账期")
    private boolean tradeAndReturnInSameSettle;

}
