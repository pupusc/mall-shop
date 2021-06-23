package com.wanmi.sbc.goods.bean.dto;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.goods.bean.enums.ConditionType;
import com.wanmi.sbc.goods.bean.enums.DeliverWay;
import com.wanmi.sbc.goods.bean.enums.ValuationType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by sunkun on 2018/5/4.
 */
@ApiModel
@Getter
@Setter
public class FreightTemplateGoodsFreeSaveDTO implements Serializable {

    private static final long serialVersionUID = 539976977665579005L;

    /**
     * 主键标识
     */
    @ApiModelProperty(value = "主键标识")
    private Long id;

    /**
     * 配送地id(逗号分隔)
     */
    @ApiModelProperty(value = "配送地id", notes = "逗号分隔")
    private String[] destinationArea;

    /**
     * 配送地名称(逗号分隔)
     */
    @ApiModelProperty(value = "配送地名称", notes = "逗号分隔")
    private String[] destinationAreaName;

    /**
     * 运送方式(1:快递配送) {@link DeliverWay}
     */
    @ApiModelProperty(value = "运送方式", notes = "0: 其他, 1: 快递")
    private DeliverWay deliverWay = DeliverWay.EXPRESS;

    /**
     * 计价方式(0:按件数,1:按重量,2:按体积) {@link ValuationType}
     */
    @ApiModelProperty(value = "计价方式", notes = "0:按件数,1:按重量,2:按体积")
    private ValuationType valuationType;

    /**
     * 包邮条件类别(0:件/重/体积计价方式,1:金额,2:计价方式+金额) {@link ConditionType}
     */
    @ApiModelProperty(value = "包邮条件类别", notes = "0:件/重/体积计价方式,1:金额,2:计价方式+金额")
    private ConditionType conditionType;

    /**
     * 包邮条件1(件/重/体积)
     */
    @ApiModelProperty(value = "包邮条件1", notes = "件/重/体积")
    private BigDecimal conditionOne;

    /**
     * 包邮条件2(金额)
     */
    @ApiModelProperty(value = "包邮条件2", notes = "金额")
    private BigDecimal conditionTwo;

    /**
     * 删除标识
     */
    @ApiModelProperty(value = "删除标识", notes = "0: 否, 1: 是")
    private DeleteFlag delFlag;
}
