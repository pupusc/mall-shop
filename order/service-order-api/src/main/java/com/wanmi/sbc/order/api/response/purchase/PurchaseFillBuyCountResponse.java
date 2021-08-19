package com.wanmi.sbc.order.api.response.purchase;

import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p></p>
 * author: sunkun
 * Date: 2018-12-05
 */
@Data
@ApiModel
public class PurchaseFillBuyCountResponse implements Serializable {

    private static final long serialVersionUID = -757639658290142239L;

    @ApiModelProperty(value = "单品信息列表")
    private List<GoodsInfoVO> goodsInfoList;
}
