package com.wanmi.sbc.marketing.halfpricesecondpiece.model.request;


import com.wanmi.sbc.marketing.common.request.MarketingSaveRequest;
import com.wanmi.sbc.marketing.halfpricesecondpiece.model.entry.MarketingHalfPriceSecondPieceLevel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 第二件半价活动规则
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HalfPriceSecondPieceSaveRequest extends MarketingSaveRequest {
    private static final long serialVersionUID = 4894133284221615424L;

    /**
     * 第二件半价实体对象
     */
    @ApiModelProperty(value = "第二件半价活动规则列表")
    private MarketingHalfPriceSecondPieceLevel halfPriceSecondPieceLevel;

    public MarketingHalfPriceSecondPieceLevel generateHalfPriceSecondPieceList(Long marketingId) {
        halfPriceSecondPieceLevel.setMarketingId(marketingId);
        return halfPriceSecondPieceLevel;
    }
}
