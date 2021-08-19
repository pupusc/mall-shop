package com.wanmi.sbc.goods.api.response.presellsale;

import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.PresellSaleVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@ApiModel
@Data
public class PresellSaleDetailsResponse extends PresellSaleVO implements Serializable {

    /**
     * 预售活动关联商户信息
     */
    @ApiModelProperty(value = "预售活动关联商户信息")
    private List<GoodsInfoVO> goodsInfoVOList;
}
