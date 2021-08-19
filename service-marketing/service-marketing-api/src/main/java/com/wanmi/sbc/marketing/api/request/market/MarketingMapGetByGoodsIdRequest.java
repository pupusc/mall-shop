package com.wanmi.sbc.marketing.api.request.market;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.marketing.bean.enums.MarketingStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2018-11-27 10:16
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarketingMapGetByGoodsIdRequest extends BaseQueryRequest {

    /**
     * 商品Id集合，查询出对应的营销信息
     */
    @ApiModelProperty(value = "商品Id列表")
    private List<String> goodsInfoIdList;

    /**
     * 查询某个状态下的营销活动
     */
    @ApiModelProperty(value = "查询某个状态下的营销活动")
    private MarketingStatus marketingStatus;

    /**
     * 排除某个状态下的营销活动
     */
    @ApiModelProperty(value = "排除某个状态下的营销活动")
    private MarketingStatus excludeStatus;

    /**
     * 是否是删除状态
     */
    @ApiModelProperty(value = "是否是删除状态")
    private DeleteFlag deleteFlag;

    /**
     * 是否关联活动级别
     */
    @ApiModelProperty(value = "是否关联活动级别")
    private Boolean cascadeLevel;


}
