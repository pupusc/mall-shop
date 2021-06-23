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
 * 添加企业购商品时返回的不符合条件的结果
 *
 * @author CHENLI
 * @dateTime 2019/3/26 上午9:33
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnterpriseGoodsAddResponse implements Serializable {

    private static final long serialVersionUID = 6523387983737024542L;

    /**
     * 添加企业购商品时返回的不符合条件的结果，skuIds
     */
    @ApiModelProperty(value = "不符合条件的skuIds")
    private List<String> goodsInfoIds = new ArrayList<>();
}
