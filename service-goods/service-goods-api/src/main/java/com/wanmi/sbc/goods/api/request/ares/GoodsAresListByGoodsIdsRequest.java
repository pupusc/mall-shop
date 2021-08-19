package com.wanmi.sbc.goods.api.request.ares;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author: wanggang
 * @createDate: 2018/11/5 10:52
 * @version: 1.0
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsAresListByGoodsIdsRequest implements Serializable {

    private static final long serialVersionUID = 229167585289472297L;

    @ApiModelProperty(value = "商品Id集合")
    @NotNull
    private List<String> goodsIds;
}
