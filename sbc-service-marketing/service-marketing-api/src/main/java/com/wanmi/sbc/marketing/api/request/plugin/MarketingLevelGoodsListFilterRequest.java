package com.wanmi.sbc.marketing.api.request.plugin;

import com.wanmi.sbc.customer.bean.dto.CustomerDTO;
import com.wanmi.sbc.goods.bean.dto.GoodsInfoDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * <p>会员等级插件公共Request</p>
 * @author: sunkun
 * Date: 2018-11-19
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarketingLevelGoodsListFilterRequest implements Serializable {

    private static final long serialVersionUID = -1088504297015472177L;

    /**
     * 商品列表
     */
    @ApiModelProperty(value = "商品信息列表")
    @NotNull
    private List<GoodsInfoDTO> goodsInfos;

    /**
     * 当前客户
     */
    @ApiModelProperty(value = "客户信息")
    private CustomerDTO customerDTO;
}
