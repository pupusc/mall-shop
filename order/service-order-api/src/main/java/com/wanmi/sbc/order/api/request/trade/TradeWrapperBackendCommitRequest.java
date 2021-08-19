package com.wanmi.sbc.order.api.request.trade;

import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.customer.bean.dto.CompanyInfoDTO;
import com.wanmi.sbc.customer.bean.dto.StoreInfoDTO;
import com.wanmi.sbc.order.bean.dto.TradeCreateDTO;
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
 * @Date: 2018-12-04 15:36
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class TradeWrapperBackendCommitRequest implements Serializable {

    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    private Operator operator;

    /**
     * 商家信息
     */
    @ApiModelProperty(value = "商家信息")
    private CompanyInfoDTO companyInfo;

    /**
     * 店铺信息
     */
    @ApiModelProperty(value = "店铺信息")
    private StoreInfoDTO storeInfo;

    /**
     * 订单信息
     */
    @ApiModelProperty(value = "订单信息")
    private TradeCreateDTO tradeCreate;

}
