package com.wanmi.sbc.goods.bean.dto;

import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 单品运费模板运费保存数据
 * Created by sunkun on 2018/5/4.
 */
@ApiModel
@Getter
@Setter
public class FreightTemplateGoodsExpressSaveDTO implements Serializable {

    private static final long serialVersionUID = -9949873906465794L;

    /**
     * 主键标识
     */
    @ApiModelProperty(value = "主键标识")
    private Long id;

    /**
     * 配送地id(逗号分隔)
     */
    @ApiModelProperty(value = "配送地id(逗号分隔)")
    private String[] destinationArea;

    /**
     * 配送地名称(逗号分隔)
     */
    @ApiModelProperty(value = "配送地名称(逗号分隔)")
    private String[] destinationAreaName;

    /**
     * 首件/重/体积
     */
    @ApiModelProperty(value = "首件/重/体积")
    private BigDecimal freightStartNum;

    /**
     * 对应于首件/重/体积的起步价
     */
    @ApiModelProperty(value = "对应于首件/重/体积的起步价")
    private BigDecimal freightStartPrice;

    /**
     * 续件/重/体积
     */
    @ApiModelProperty(value = "续件/重/体积")
    private BigDecimal freightPlusNum;

    /**
     * 对应于续件/重/体积的价格
     */
    @ApiModelProperty(value = "对应于续件/重/体积的价格")
    private BigDecimal freightPlusPrice;

    /**
     * 是否默认(0:否,1:是)
     */
    @ApiModelProperty(value = "是否默认", notes = "0:否,1:是")
    private DefaultFlag defaultFlag;

    /**
     * 删除标识
     */
    @ApiModelProperty(value = "删除标识", notes = "0:否,1:是")
    private DeleteFlag delFlag;
}
