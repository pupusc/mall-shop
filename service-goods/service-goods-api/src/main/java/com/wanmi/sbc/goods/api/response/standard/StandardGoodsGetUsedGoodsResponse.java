package com.wanmi.sbc.goods.api.response.standard;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p></p>
 * author: sunkun
 * Date: 2018-11-07
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StandardGoodsGetUsedGoodsResponse implements Serializable {


    private static final long serialVersionUID = -2013136629873539672L;

    @ApiModelProperty(value = "spu Id")
    private List<String> goodsIds;
}
