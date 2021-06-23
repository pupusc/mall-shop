package com.wanmi.sbc.customer.api.response.growthvalue;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerGrowthValueTodayResponse {

    /**
     * 今日到账成长值总数
     */
    @ApiModelProperty(value = "今日到账成长值总数")
    private Integer growthValueSum;
}
