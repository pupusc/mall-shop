package com.wanmi.sbc.erp.api.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * @program: sbc-background
 * @description: 退货单查询Response
 * @author: 0F3685-wugongjiang
 * @create: 2021-02-18 09:35
 **/
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReturnTradeResponse implements Serializable {

    /**
     * 退货入库状态(0:未入库
     * 1:已入库)
     */
    @ApiModelProperty(value = "退货入库状态集合")
    private Map<String,Integer> receiveStatusMap;
}
