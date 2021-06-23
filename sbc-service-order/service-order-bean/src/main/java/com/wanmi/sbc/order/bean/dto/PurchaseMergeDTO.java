package com.wanmi.sbc.order.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel
public class PurchaseMergeDTO implements Serializable {

    private static final long serialVersionUID = -6726648674789457548L;

    /**
     * SKU编号
     */
    @ApiModelProperty(value = "SKU编号", required = true)
    @NotBlank
    private String goodsInfoId;

    /**
     * 全局购买数
     */
    @ApiModelProperty(value = "全局购买数", required = true)
    @NotNull
    @Min(1)
    private Long goodsNum;

    /**
     * 邀请人id-会员id
     */
    @ApiModelProperty(value = "邀请人id")
    String inviteeId;
}
