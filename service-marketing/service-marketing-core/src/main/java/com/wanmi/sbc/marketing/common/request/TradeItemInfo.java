package com.wanmi.sbc.marketing.common.request;

import com.wanmi.sbc.goods.bean.enums.DistributionGoodsAudit;
import com.wanmi.sbc.goods.bean.enums.GoodsType;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>获取营销优惠时传入的订单商品信息参数封装</p>
 * Created by of628-wenzhi on 2018-03-14-下午6:02.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TradeItemInfo {
    /**
     * 单品id
     */
    private String skuId;

    /**
     * 商品id
     */
    private String spuId;

    /**
     * 商品价格（计算区间价或会员价后的应付金额）
     */
    private BigDecimal price;

    /**
     * 商品购买数量
     */
    private Long num;

    /**
     * 店铺id
     */
    private Long storeId;

    /**
     * 商品品牌id
     */
    private Long brandId;

    /**
     * 分类id
     */
    private Long cateId;
    /**
     * 商品类型
     */
    private GoodsType goodsType;
    /**
     * 店铺分类
     */
    private List<Long> storeCateIds;

    /**
     * 分销商品审核状态
     */
    private DistributionGoodsAudit distributionGoodsAudit;
}
