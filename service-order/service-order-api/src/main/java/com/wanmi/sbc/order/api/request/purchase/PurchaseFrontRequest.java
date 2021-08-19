package com.wanmi.sbc.order.api.request.purchase;

import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.goods.bean.dto.GoodsMarketingDTO;
import com.wanmi.sbc.order.bean.dto.PurchaseGoodsInfoDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 未登录时,前端采购单入参
 * @author bail
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class PurchaseFrontRequest extends BaseRequest {

    private static final long serialVersionUID = 1870025584368342290L;

    /**
     * 前端采购单缓存中的多个sku
     */
    @ApiModelProperty(value = "前端采购单缓存中的多个sku")
    @NotNull
    @Valid
    private List<PurchaseGoodsInfoDTO> goodsInfoDTOList;

    /**
     * 前端采购单勾选的skuIdList
     */
    @ApiModelProperty(value = "前端采购单勾选的skuIdList")
    @NotNull
    private List<String> goodsInfoIds;

    /**
     * 用户针对各sku选择的营销活动id信息
     */
    @ApiModelProperty(value = "用户针对各sku选择的营销活动id信息")
    @NotNull
    private List<GoodsMarketingDTO> goodsMarketingDTOList;

    /**
     * 邀请人id-会员id
     */
    @ApiModelProperty(value = "邀请人id")
    String inviteeId;


    /**
     * 门店id
     */
    @ApiModelProperty(value = "门店id")
    Long storeId;

}
