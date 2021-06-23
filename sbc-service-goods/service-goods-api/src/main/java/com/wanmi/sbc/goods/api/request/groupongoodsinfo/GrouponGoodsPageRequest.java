package com.wanmi.sbc.goods.api.request.groupongoodsinfo;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * <p>拼团活动商品信息表分页查询请求参数</p>
 *
 * @author groupon
 * @date 2019-05-15 14:49:12
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrouponGoodsPageRequest extends BaseQueryRequest {
    private static final long serialVersionUID = 5794652344512329593L;

    /**
     * 拼团分类ID
     */
    @ApiModelProperty(value = "拼团分类ID")
    private String grouponCateId;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    /**
     * 是否精选
     */
    @ApiModelProperty(value = "是否精选")
    private Boolean sticky = Boolean.FALSE;

    @ApiModelProperty(value = "店铺Id")
    private Long storeId;

}