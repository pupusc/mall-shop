package com.wanmi.sbc.goods.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.goods.bean.enums.ConditionType;
import com.wanmi.sbc.goods.bean.enums.DeliverWay;
import com.wanmi.sbc.goods.bean.enums.ValuationType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 单品运费模板指定包邮条件
 * Created by sunkun on 2018/5/4.
 */
@ApiModel
@Data
public class FreightTemplateGoodsFreeVO implements Serializable {

    private static final long serialVersionUID = 3737592431850476773L;

    /**
     * 主键标识
     */
    @ApiModelProperty(value = "主键标识")
    private Long id;

    /**
     * 运费模板id
     */
    @ApiModelProperty(value = "运费模板id")
    private Long freightTempId;

    /**
     * 配送地id(逗号分隔)
     */
    @ApiModelProperty(value = "配送地id", notes = "逗号分隔")
    private String destinationArea;

    /**
     * 配送地名称(逗号分隔)
     */
    @ApiModelProperty(value = "配送地名称", notes = "逗号分隔")
    private String destinationAreaName;

    /**
     * 运送方式(1:快递配送) {@link DeliverWay}
     */
    @ApiModelProperty(value = "运送方式", notes = "0: 默认, 1: 快递配送")
    private DeliverWay deliverWay;

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
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    /**
     * 是否删除(0:否,1:是)
     */
    @ApiModelProperty(value = "是否删除", notes = "0:否,1:是")
    private DeleteFlag delFlag;
}
