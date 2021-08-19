package com.wanmi.sbc.linkedmall.api.response.order;

import com.aliyuncs.linkedmall.model.v20180116.QueryOrderListResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SbcOrderQueryResponse implements Serializable {

    @ApiModelProperty(value = "linkedmall订单详情")
    private QueryOrderListResponse.LmOrderListItem lmOrderListItem;
}
