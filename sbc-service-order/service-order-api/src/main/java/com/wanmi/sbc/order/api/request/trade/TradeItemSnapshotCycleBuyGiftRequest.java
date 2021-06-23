package com.wanmi.sbc.order.api.request.trade;

import com.wanmi.sbc.order.bean.dto.CycleBuyInfoDTO;
import com.wanmi.sbc.order.bean.dto.TradeItemDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class TradeItemSnapshotCycleBuyGiftRequest implements Serializable {

    private static final long serialVersionUID = -6630016776841880973L;

    /**
     * 终端token
     */
    @ApiModelProperty(value = "终端token", hidden = true)
    private String terminalToken;

    /**
     * 商品spuId
     */
    @ApiModelProperty(value = "商品spuId")
    @NotBlank
    private String goodsId;

    /**
     * 周期购信息
     */
    @ApiModelProperty(value = "周期购信息")
    @NotNull
    private CycleBuyInfoDTO cycleBuyInfo;
}
