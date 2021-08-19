package com.wanmi.sbc.order.api.request.purchase;

import com.wanmi.sbc.goods.bean.dto.GoodsMarketingDTO;
import com.wanmi.sbc.order.api.response.purchase.PurchaseResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ApiModel
public class ValidateAndSetGoodsMarketingsRequest {

    @ApiModelProperty(value = "采购单信息")
    @NotNull
    private PurchaseResponse response;

    @ApiModelProperty(value = "营销信息")
    private List<GoodsMarketingDTO> goodsMarketingDTOList;
}
