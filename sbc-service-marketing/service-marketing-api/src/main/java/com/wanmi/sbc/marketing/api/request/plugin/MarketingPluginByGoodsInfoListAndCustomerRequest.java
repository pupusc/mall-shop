package com.wanmi.sbc.marketing.api.request.plugin;

import com.wanmi.sbc.customer.bean.dto.CustomerDTO;
import com.wanmi.sbc.goods.bean.dto.GoodsInfoDTO;
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
 * <p>获取营销入参结构</p>
 * @author: sunkun
 * Date: 2018-11-19
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarketingPluginByGoodsInfoListAndCustomerRequest implements Serializable {

    private static final long serialVersionUID = -4434832821177284914L;

    @ApiModelProperty(value = "商品信息列表")
    @NotEmpty
    private List<GoodsInfoDTO> goodsInfoList;

    /**
     * 当前客户
     */
    @ApiModelProperty(value = "客户信息")
    private CustomerDTO customerDTO;

}
