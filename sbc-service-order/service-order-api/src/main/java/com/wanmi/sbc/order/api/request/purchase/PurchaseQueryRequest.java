package com.wanmi.sbc.order.api.request.purchase;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
 * <p></p>
 * author: sunkun
 * Date: 2018-12-03
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class PurchaseQueryRequest implements Serializable {

    private static final long serialVersionUID = 6498910185574338392L;

    @ApiModelProperty(value = "客户id")
    @NotBlank
    private String customerId;

    @ApiModelProperty(value = "商品ids")
    private List<String> goodsInfoIds;

    /**
     * 邀请人id-会员id
     */
    @ApiModelProperty(value = "邀请人id")
    String inviteeId;

    @ApiModelProperty(value = "活动id")
    private Long marketingId;
}
