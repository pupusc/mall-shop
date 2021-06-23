package com.wanmi.sbc.account.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class SettlementViewVO extends SettlementVO implements Serializable {

    private static final long serialVersionUID = 76699713189263537L;

    /**
     * 商铺名称
     */
    @ApiModelProperty(value = "商铺名称")
    private String storeName;

    /**
     * 结算单号
     */
    @ApiModelProperty(value = "结算单号")
    private String settlementCode;

    /**
     * 商家编码
     */
    @ApiModelProperty(value = "商家编码")
    private String companyCode;

}
