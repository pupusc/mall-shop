package com.wanmi.sbc.order.api.request.trade;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Author yangzhen
 * @Description //TODO 批量查找
 * @Date 18:15 2020/11/28
 * @Param
 * @return
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class TradeGetByIdsRequest implements Serializable {

    private static final long serialVersionUID = -4153498256754887224L;

    /**
     * 交易id
     */
    @ApiModelProperty(value = "交易id")
    private List<String> tid;
}
