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
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * <p></p>
 * author: sunkun
 * Date: 2018-11-27
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarketingPluginGetCustomerLevelsRequest implements Serializable {

    private static final long serialVersionUID = -3412322936799503755L;

    @ApiModelProperty(value = "商品信息列表")
    @NotNull
    @Size(min = 1)
    private List<GoodsInfoDTO> goodsInfoList;

    @ApiModelProperty(value = "客户信息")
    @NotNull
    private CustomerDTO customer;

}
