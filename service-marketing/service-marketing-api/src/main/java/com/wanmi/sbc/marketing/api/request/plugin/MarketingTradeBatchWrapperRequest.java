package com.wanmi.sbc.marketing.api.request.plugin;

import com.wanmi.sbc.marketing.bean.dto.TradeMarketingWrapperDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;
import java.util.List;

/**
 * 批量订单提交营销处理请求结构
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarketingTradeBatchWrapperRequest implements Serializable {


    private static final long serialVersionUID = 4520038630632475443L;

    /**
     * 批量订单营销封装参数 {@link TradeMarketingWrapperDTO}
     */
    @ApiModelProperty(value = "订单营销信息列表")
    @NotEmpty
    private List<TradeMarketingWrapperDTO> wraperDTOList;
}
