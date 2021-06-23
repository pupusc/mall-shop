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
 * <p>获取已使用的标品库响应</p>
 * author: sunkun
 * Date: 2018-11-07
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StandardGoodsGetUsedStandardResponse implements Serializable {


    private static final long serialVersionUID = -2013136629873539672L;

    @ApiModelProperty(value = "商品库Id")
    private List<String> standardIds;
}
