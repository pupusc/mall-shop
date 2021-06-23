package com.wanmi.sbc.goods.api.request.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 根据编号批量查询商品信息请求对象
 * @author daiyitian
 * @dateTime 2018/11/19 上午9:40
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoodsListByIdsRequest implements Serializable {

    private static final long serialVersionUID = 5594325220431537194L;

    @ApiModelProperty(value = "商品Id")
    private List<String> goodsIds;
}
