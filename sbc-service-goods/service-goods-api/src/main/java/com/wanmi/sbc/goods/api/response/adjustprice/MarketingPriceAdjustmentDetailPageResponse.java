package com.wanmi.sbc.goods.api.response.adjustprice;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.goods.bean.vo.MarketingPriceAdjustDetailVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>市场价改价详情列表返回结构</p>
 * Created by of628-wenzhi on 2020-12-16-11:26 上午.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class MarketingPriceAdjustmentDetailPageResponse implements Serializable {
    private static final long serialVersionUID = -971534302556252905L;

    /**
     * 市场价改价详情列表
     */
    @ApiModelProperty(value = "市场价改价详情列表")
    private MicroServicePage<MarketingPriceAdjustDetailVO> pageList;

}
