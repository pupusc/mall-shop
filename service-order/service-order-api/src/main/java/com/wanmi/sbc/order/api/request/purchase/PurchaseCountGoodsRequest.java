package com.wanmi.sbc.order.api.request.purchase;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * <p></p>
 * author: sunkun
 * Date: 2018-12-07
 */
@Data
@ApiModel
public class PurchaseCountGoodsRequest implements Serializable {

    private static final long serialVersionUID = -1501153196040911906L;

    @ApiModelProperty(value = "客户id")
    @NotBlank
    private String customerId;

    /**
     * 邀请人id-会员id
     */
    @ApiModelProperty(value = "邀请人id")
    String inviteeId;

    /**
     * 公司信息ID
     */
    @ApiModelProperty(value = "公司信息ID")
    private Long companyInfoId;

    /**
     * 商品渠道：1商城h5；2商城小程序
     */
    private String goodsChannelType;
}
