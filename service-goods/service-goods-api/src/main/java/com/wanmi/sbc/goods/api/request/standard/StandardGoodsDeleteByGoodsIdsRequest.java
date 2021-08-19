package com.wanmi.sbc.goods.api.request.standard;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * <p>根据商品id列表删除商品请求类</p>
 * author: sunkun
 * Date: 2018-11-07
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StandardGoodsDeleteByGoodsIdsRequest implements Serializable {

    private static final long serialVersionUID = 2060121677892719992L;

    /**
     * 商品id集合
     */
    @ApiModelProperty(value = "商品id集合")
    @NotNull
    @Size(min = 1)
    private List<String> goodsIds;
}
