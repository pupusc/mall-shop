package com.wanmi.sbc.marketing.api.response.markup;

import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.marketing.bean.enums.MarketingSubType;
import com.wanmi.sbc.marketing.bean.vo.MarkupLevelVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * @author he
 * @date 2021-02-04 16:09:09
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarkupLevelBySkuResponse implements Serializable {
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

}
