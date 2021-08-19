package com.wanmi.sbc.marketing.api.response.coupon;

import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.marketing.bean.enums.FullBuyType;
import com.wanmi.sbc.marketing.bean.enums.ScopeType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2018-11-23 16:08
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CouponGoodsListResponse {

    /**
     *  是否全部商品
     */
    @ApiModelProperty(value = "是否全部商品")
    private DefaultFlag isAll;

    /**
     *  商品Id集合
     */
    @ApiModelProperty(value = "商品Id集合")
    private List<String> goodsInfoId;

    /**
     *  品牌Id集合
     */
    @ApiModelProperty(value = "品牌Id集合")
    private List<Long> brandIds;

    /**
     *  品牌Id集合(过滤之后的)
     */
    @ApiModelProperty(value = "品牌Id集合(过滤之后的)")
    private List<Long> queryBrandIds;

    /**
     *  店铺分类Id集合
     */
    @ApiModelProperty(value = "店铺分类Id集合")
    private List<Long> storeCateIds;

    /**
     *  平台分类Id集合
     */
    @ApiModelProperty(value = "平台分类Id集合")
    private List<Long> cateIds;

    /**
     *  平台分类Id集合--es查询商品范围
     */
    @ApiModelProperty(value = "平台分类Id集合--es查询商品范围")
    private List<Long> cateIds4es;

    /**
     *  优惠券开始时间
     */
    @ApiModelProperty(value = "优惠券开始时间")
    private String startTime;

    /**
     *  优惠券结束时间
     */
    @ApiModelProperty(value = "优惠券结束时间")
    private String endTime;

    /**
     *  优惠券作用范围类型
     */
    @ApiModelProperty(value = "优惠券作用范围类型")
    private ScopeType scopeType;

    /**
     *  是否平台优惠券
     */
    @ApiModelProperty(value = "是否平台优惠券")
    private DefaultFlag platformFlag;

    /**
     *  店铺名称
     */
    @ApiModelProperty(value = "店铺名称")
    private String storeName;

    /**
     * 购满多少钱
     */
    @ApiModelProperty(value = "购满多少钱")
    private BigDecimal fullBuyPrice;

    /**
     * 购满类型 0：无门槛，1：满N元可使用
     */
    @ApiModelProperty(value = "购满类型")
    private FullBuyType fullBuyType;

    /**
     * 优惠券面值
     */
    @ApiModelProperty(value = "优惠券面值")
    private BigDecimal denomination;

    /**
     * 平台类目名称
     */
    @ApiModelProperty(value = "平台类目名称map<key为平台类目id，value为平台类目名称>")
    private Map<Long, String> cateMap;

    /**
     * 品牌名称
     */
    @ApiModelProperty(value = "品牌名称map<key为品牌id，value为品牌名称>")
    private Map<Long, String> brandMap;

    /**
     * 店铺分类名称
     */
    @ApiModelProperty(value = "店铺分类名称map<key为店铺分类id，value为店铺分类名称>")
    private Map<Long, String> storeCateMap;

    /**
     * 店铺ID
     */
    @ApiModelProperty(value = "店铺id")
    private Long storeId;

}
