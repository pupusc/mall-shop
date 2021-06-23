package com.wanmi.sbc.goods.api.response.standard;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: 需要es删除的商家商品
 * @Date: Created In 下午4:01 2018/12/13
 * @Description:
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StandardGoodsDeleteProviderGoodsIdsResponse implements Serializable {

    private static final long serialVersionUID = -7968633888586690759L;

    @ApiModelProperty(value = "spu Id")
    private List<String> goodsIds;
}
