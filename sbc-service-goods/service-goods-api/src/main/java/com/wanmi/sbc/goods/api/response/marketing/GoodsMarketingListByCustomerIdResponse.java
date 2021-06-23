package com.wanmi.sbc.goods.api.response.marketing;

import com.wanmi.sbc.goods.bean.vo.GoodsMarketingVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p>商品营销</p>
 * author: sunkun
 * Date: 2018-11-02
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsMarketingListByCustomerIdResponse implements Serializable {

    private static final long serialVersionUID = -4339822497881653759L;

    @ApiModelProperty(value = "商品营销")
    private List<GoodsMarketingVO> goodsMarketings;

}
