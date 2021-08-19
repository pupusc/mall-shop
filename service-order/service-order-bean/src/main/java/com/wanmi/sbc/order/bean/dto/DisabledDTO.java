package com.wanmi.sbc.order.bean.dto;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 判断导出订单是否导出子订单
 * @Autho caiping
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class DisabledDTO extends BaseQueryRequest {

    /**
     * 是否导出子单
     */
    @ApiModelProperty(value = "导出子单")
    private String disabled;
}
