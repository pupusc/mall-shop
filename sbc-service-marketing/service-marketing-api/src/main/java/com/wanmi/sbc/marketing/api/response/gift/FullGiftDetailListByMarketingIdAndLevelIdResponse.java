package com.wanmi.sbc.marketing.api.response.gift;

import com.wanmi.sbc.marketing.bean.vo.MarketingFullGiftDetailVO;
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
 * @Date: 2018-11-20 16:44
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FullGiftDetailListByMarketingIdAndLevelIdResponse {

    /**
     * 营销详情列表
     */
    @ApiModelProperty(value = "营销满赠详情列表")
    private List<MarketingFullGiftDetailVO> fullGiftDetailVOList;


}
