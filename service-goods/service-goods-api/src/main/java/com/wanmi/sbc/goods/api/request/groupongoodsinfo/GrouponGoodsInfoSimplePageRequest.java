package com.wanmi.sbc.goods.api.request.groupongoodsinfo;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

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
public class GrouponGoodsInfoSimplePageRequest extends BaseQueryRequest {
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

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺Id")
    private Long storeId;

    /**
     * 是否需要显示规格明细
     */
    @ApiModelProperty(value = "是否显示规格")
    private Boolean havSpecTextFlag;

    /**
     * 商品skuId
     */
    @ApiModelProperty(value = "批量商品skuId")
    private List<String> goodsInfoIds;

    /**
     * 排序标识
     * 0:销量倒序
     * 1:好评数倒序
     * 2:评论率倒序
     * 3:排序号倒序
     * 4:排序号倒序
     * 5:成才数倒序
     */
    @ApiModelProperty(value = "排序标识")
    private Integer sortFlag;

}