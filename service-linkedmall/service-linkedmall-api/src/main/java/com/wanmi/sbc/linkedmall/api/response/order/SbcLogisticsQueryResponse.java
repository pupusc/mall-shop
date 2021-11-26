package com.wanmi.sbc.linkedmall.api.response.order;

import com.aliyuncs.linkedmall.model.v20180116.QueryLogisticsResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SbcLogisticsQueryResponse implements Serializable {

    @ApiModelProperty(value = "linkedmall订单物流详情")
    private List<QueryLogisticsResponse.DataItem> dataItems;
}
