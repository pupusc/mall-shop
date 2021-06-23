package com.wanmi.sbc.order.api.request.trade;

import com.wanmi.sbc.marketing.bean.dto.TradeMarketingDTO;
import com.wanmi.sbc.order.bean.dto.TradeItemDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 保存订单赠品商品快照请求结构
 * @Author: daiyitian
 * @Description:
 * @Date: 2018-11-16 16:39
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class TradeItemSnapshotGiftRequest implements Serializable {

    private static final long serialVersionUID = -1076979847505660373L;

    /**
     * 终端token
     */
    @ApiModelProperty(value = "终端token", hidden = true)
    private String terminalToken;

    /**
     * 商品信息，必传
     */
    @ApiModelProperty(value = "商品信息")
    @NotEmpty
    @Valid
    private List<TradeItemDTO> tradeItems;

    /**
     * 营销快照
     */
    @ApiModelProperty(value = "营销快照")
    @NotNull
    private TradeMarketingDTO tradeMarketingDTO;
}
