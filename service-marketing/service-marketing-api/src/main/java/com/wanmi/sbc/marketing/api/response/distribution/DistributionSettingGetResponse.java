package com.wanmi.sbc.marketing.api.response.distribution;

import com.wanmi.sbc.customer.bean.vo.DistributorLevelVO;
import com.wanmi.sbc.goods.bean.vo.GoodsBrandVO;
import com.wanmi.sbc.goods.bean.vo.GoodsCateVO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.marketing.bean.vo.CouponInfoVO;
import com.wanmi.sbc.marketing.bean.vo.DistributionRewardCouponVO;
import com.wanmi.sbc.marketing.bean.vo.DistributionSettingVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>查询分销设置响应</p>
 *
 * @author gaomuwei
 * @date 2019-02-19 10:08:02
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DistributionSettingGetResponse {

    /**
     * 分销配置
     */
    @ApiModelProperty(value = "分销配置")
    private DistributionSettingVO distributionSetting;

    /**
     * 奖励的优惠券
     */
    @ApiModelProperty(value = "奖励的优惠券")
    private List<CouponInfoVO> couponInfos = new ArrayList<>();

    /**
     * 优惠券奖励的组数
     */
    @ApiModelProperty(value = "优惠券奖励的组数")
    private List<DistributionRewardCouponVO> couponInfoCounts = new ArrayList<>();

    /**
     * 礼包商品列表
     */
    @ApiModelProperty(value = "礼包商品列表")
    private List<GoodsInfoVO> goodsInfos = new ArrayList<>();

    /**
     * 品牌列表
     */
    @ApiModelProperty(value = "品牌列表")
    private List<GoodsBrandVO> brands = new ArrayList();


    /**
     * 分类列表
     */
    @ApiModelProperty(value = "分类列表")
    private List<GoodsCateVO> cates = new ArrayList();

    /**
     * 分销员等级列表
     */
    @ApiModelProperty(value = "分类员等级列表")
    private List<DistributorLevelVO> distributorLevels = new ArrayList<>();

}
