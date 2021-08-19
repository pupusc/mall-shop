package com.wanmi.sbc.order.api.request.trade;

import com.wanmi.sbc.order.bean.dto.ProviderTradeQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2018-12-05 15:05
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class ProviderTradeListExportRequest implements Serializable {

    /**
     * 分页参数
     */
    @ApiModelProperty(value = "分页参数")
    private ProviderTradeQueryDTO providerTradeQueryDTO;

}
