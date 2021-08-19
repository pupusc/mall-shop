package com.wanmi.sbc.order.api.request.settlement;

import com.wanmi.sbc.common.enums.StoreType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2018-12-10 14:21
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class SettlementAnalyseRequest {

    /**
     * 结算定时任务参数
     */
    @ApiModelProperty(value = "结算定时任务参数")
    private String param;

    /**
     * 结算类型
     */
    @ApiModelProperty(value = "结算类型")
    private StoreType storeType;

}
