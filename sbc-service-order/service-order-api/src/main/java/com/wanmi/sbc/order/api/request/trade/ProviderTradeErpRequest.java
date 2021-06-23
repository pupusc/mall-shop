package com.wanmi.sbc.order.api.request.trade;

import com.wanmi.sbc.order.bean.dto.ProviderTradeQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.query.Criteria;

import java.io.Serializable;

/**
 * @Author: weiwenhao
 * @Description:
 * @Date: 2021-02-04 19:00
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class ProviderTradeErpRequest implements Serializable {

    /**
     * 分页数量
     */
    @ApiModelProperty(value = "分页数量")
    private int pageSize;

    /**
     * 子订单号
     */
    @ApiModelProperty(value = "子订单号")
    private String ptid;

    /**
     * 子订单号
     */
    @ApiModelProperty(value = "发货开始时间")
    private String startTime;

    /**
     * 子订单号
     */
    @ApiModelProperty(value = "发货结束时间")
    private String endTime;

    /**
     * 分页数量
     */
    @ApiModelProperty(value = "页码")
    private int pageNum;
}
