package com.wanmi.sbc.marketing.api.response.gift;

import com.wanmi.sbc.marketing.bean.vo.MarketingVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class FullGiftModifyResponse {

    /**
     * 营销视图对象
     */
    @ApiModelProperty(value = "营销视图对象")
    private MarketingVO marketingVO;

}
