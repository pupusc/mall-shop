package com.wanmi.sbc.marketing.api.request.distribution;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @Author: gaomuwei
 * @Date: Created In 下午4:18 2019/2/19
 * @Description:
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DistributionStoreSettingGet4StoreBagsRequest {

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    @NotNull
    private String storeId;

}
