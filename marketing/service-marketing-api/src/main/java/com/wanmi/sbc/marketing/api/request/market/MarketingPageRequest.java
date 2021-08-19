package com.wanmi.sbc.marketing.api.request.market;

import com.wanmi.sbc.marketing.bean.dto.MarketingPageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2018-11-16 16:39
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MarketingPageRequest implements Serializable {

    private static final long serialVersionUID = -5040158058205825043L;

    @ApiModelProperty(value = "店铺id")
    private Long storeId;

    @ApiModelProperty(value = "营销信息")
    private MarketingPageDTO marketingPageDTO;

    @ApiModelProperty(value = "判断是否展示营销规则")
    private Boolean rules;


}
