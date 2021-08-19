package com.wanmi.sbc.marketing.api.request.market;

import com.wanmi.sbc.marketing.bean.dto.SkuExistsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2018-11-19 9:37
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExistsSkuByMarketingTypeRequest implements Serializable {

    private static final long serialVersionUID = 5875413246876814990L;

    @ApiModelProperty(value = "营销类型对应的SKU")
    private SkuExistsDTO skuExistsDTO;

    @ApiModelProperty(value = "店铺id")
    @NotNull
    private Long storeId;

}
