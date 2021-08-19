package com.wanmi.sbc.goods.api.request.marketing;

import com.wanmi.sbc.goods.bean.dto.GoodsMarketingDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * <p>商品营销</p>
 * author: sunkun
 * Date: 2018-11-02
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsMarketingBatchAddRequest implements Serializable {

    private static final long serialVersionUID = -2898793108096844642L;

    /**
     * 商品营销实体
     */
    @ApiModelProperty(value = "商品营销实体")
    @NotNull
    private List<GoodsMarketingDTO> goodsMarketings;
}
