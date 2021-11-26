package com.wanmi.sbc.linkedmall.api.response.order;

import com.aliyuncs.linkedmall.model.v20180116.QueryOrderListResponse;
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
public class SbcOrderListQueryResponse implements Serializable {

    @ApiModelProperty(value = "linkedmall订单详情")
    private List<QueryOrderListResponse.LmOrderListItem> lmOrderListItems;

    @ApiModelProperty(value = "当前页码")
    private Integer pageNum;

    @ApiModelProperty(value = "当前页条数")
    private Integer pageSize;
}
