package com.wanmi.sbc.goods.api.request.enterprise.goods;

import com.wanmi.sbc.goods.bean.enums.EnterpriseAuditState;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 企业商品审核
 * @author by 柏建忠 on 2017/3/24.
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnterpriseAuditCheckRequest implements Serializable {

    private static final long serialVersionUID = -8312305040718761890L;
    /**
     * skuId
     */
    @ApiModelProperty(value = "skuId")
    @NotEmpty
    private String goodsInfoId;

    /**
     * 企业商品的状态
     */
    @ApiModelProperty(value = "企业商品的状态" ,notes = "0：无状态 1：待审核 2：已审核 3：审核未通过")
    @NotNull
    private EnterpriseAuditState enterpriseAuditState;

    /**
     * 企业购商品审核被驳回的原因
     */
    @ApiModelProperty(value = "企业购商品审核被驳回的原因")
    private String enterPriseGoodsAuditReason;

}
