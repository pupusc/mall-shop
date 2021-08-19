package com.wanmi.sbc.goods.api.response.enterprise;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 删除企业购商品时返回删除的skuIds
 *
 * @author CHENLI
 * @dateTime 2019/3/26 上午9:33
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnterpriseBatchDeleteResponse implements Serializable {

    private static final long serialVersionUID = 6523387983737024542L;

    /**
     * 删除企业购商品时返回删除的skuIds
     */
    @ApiModelProperty(value = "不符合条件的skuIds")
    private List<String> goodsInfoIds = new ArrayList<>();
}
