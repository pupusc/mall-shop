package com.wanmi.sbc.order.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author yangzhen
 * @Description //TODO
 * @Date 10:10 2020/11/30
 * @Param
 * @return
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class ProviderTradeListQueryVO implements Serializable {

    private static final long serialVersionUID = -4153498256754887224L;

    /**
     * 交易id
     */
    @ApiModelProperty(value = "交易id")
    private String tid;

    /**
     * 是否需要查询linkedmall子订单
     */
    @ApiModelProperty(value = "是否需要查询linkedmall子订单")
    private Boolean needLmOrder;




}
