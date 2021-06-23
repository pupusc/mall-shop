package com.wanmi.sbc.goods.api.request.livegoods;

import com.wanmi.sbc.goods.api.request.GoodsBaseRequest;
import lombok.*;
import javax.validation.constraints.NotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>单个删除直播商品请求参数</p>
 * @author zwb
 * @date 2020-06-10 11:05:45
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LiveGoodsDelByIdRequest extends GoodsBaseRequest {
private static final long serialVersionUID = 1L;

    /**
     * 商品id
     */
    @ApiModelProperty(value = "商品id")
    private Long goodsId;

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    private Long id;

    /**
     * accessToken
     */
    @ApiModelProperty(value = "accessToken")
    private String accessToken;


}
