package com.wanmi.sbc.marketing.api.response.markup;

import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.marketing.bean.enums.MarketingSubType;
import com.wanmi.sbc.marketing.bean.vo.MarketingFullGiftLevelVO;
import com.wanmi.sbc.marketing.bean.vo.MarkupLevelVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>根据id查询任意（包含已删除）加价购活动信息response</p>
 * @author he
 * @date 2021-02-04 16:09:09
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarkupLevelByIdResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 活动规则列表
     */
    @ApiModelProperty(value = "活动规则列表")
    private List<MarkupLevelVO> levelList;

    /**
     * 加购商品列表
     */
    @ApiModelProperty(value = "加购商品列表")
    private List<GoodsInfoVO> goodsInfoVOList;

    /**
     * 活动子类型
     */
    @ApiModelProperty(value = "活动子类型")
    private MarketingSubType marketingSubType;
}
