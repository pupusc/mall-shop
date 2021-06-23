package com.wanmi.sbc.marketing.api.response.gift;

import com.wanmi.sbc.marketing.bean.vo.MarketingFullGiftLevelVO;
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
 * @Date: 2018-11-20 14:29
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FullGiftLevelListByMarketingIdResponse {

    /**
     * 活动规则列表
     */
    @ApiModelProperty(value = "活动规则列表")
    private List<MarketingFullGiftLevelVO> fullGiftLevelVOList;

}
