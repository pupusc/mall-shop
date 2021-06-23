package com.wanmi.sbc.order.bean.dto;

import com.wanmi.sbc.common.base.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: gaomuwei
 * @Date: Created In 下午3:40 2019/3/4
 * @Description: 下单分销单品信息
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
public class TradeDistributeItemDTO extends BaseRequest {

    private static final long serialVersionUID = 3487204390777682101L;

    /**
     * 单品id
     */
    @ApiModelProperty(value = "单品id")
    private String goodsInfoId;

    /**
     * 购买数量
     */
    @ApiModelProperty(value = "购买数量")
    private Long num;

    /**
     * 总sku的实付金额
     */
    @ApiModelProperty(value = "总sku的实付金额")
    private BigDecimal actualPaidPrice;

    /**
     * 返利人佣金比例
     */
    @ApiModelProperty(value = "返利人佣金比例")
    private BigDecimal commissionRate;

    /**
     * 返利人佣金
     */
    @ApiModelProperty(value = "返利人佣金")
    private BigDecimal commission;

    /**
     * 总佣金(返利人佣金 + 提成人佣金)
     */
    @ApiModelProperty(value = "总佣金(返利人佣金 + 提成人佣金)")
    private BigDecimal totalCommission;

    /**
     * 提成人佣金列表
     */
    @ApiModelProperty(value = "提成人佣金列表")
    private List<TradeDistributeItemCommissionDTO> commissions = new ArrayList<>();

}
