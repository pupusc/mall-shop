package com.wanmi.sbc.order.api.request.trade;

import com.wanmi.sbc.goods.bean.dto.GoodsInfoDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 保存订单商品快照请求结构，修改商品数量
 * @Author: daiyitian
 * @Description:
 * @Date: 2018-11-16 16:39
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class TradeItemModifyGoodsNumRequest implements Serializable {

    private static final long serialVersionUID = -1076979847505660373L;

    /**
     * 客户id
     */
    @ApiModelProperty(value = "客户id")
    @NotBlank
    private String customerId;

    /**
     * skuId
     */
    @ApiModelProperty(value = "skuId")
    @NotBlank
    private String skuId;

    /**
     * 购买数量
     */
    @ApiModelProperty(value = "购买数量")
    @Min(1L)
    @NotNull
    private Long num;

    /**
     * 快照商品详细信息，包含所属商家，店铺等信息
     */
    @ApiModelProperty(value = "快照商品详细信息，包含所属商家，店铺等信息")
    @NotEmpty
    private List<GoodsInfoDTO> skuList;

    private String terminalToken;
}
