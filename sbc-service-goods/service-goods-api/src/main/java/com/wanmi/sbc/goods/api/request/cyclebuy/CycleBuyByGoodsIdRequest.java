package com.wanmi.sbc.goods.api.request.cyclebuy;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

/**
 * <p>单个查询周期购活动请求参数</p>
 * @author xuyunpeng
 * @date 2021-01-21 09:15:37
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CycleBuyByGoodsIdRequest implements Serializable {

    private static final long serialVersionUID = -5261999446185761038L;

    @ApiModelProperty(value = "spuId")
    @NonNull
    private String goodsId;
}
