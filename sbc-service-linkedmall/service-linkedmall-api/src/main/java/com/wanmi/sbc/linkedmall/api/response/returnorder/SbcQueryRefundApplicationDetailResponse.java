package com.wanmi.sbc.linkedmall.api.response.returnorder;

import com.aliyuncs.linkedmall.model.v20180116.QueryRefundApplicationDetailResponse;
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
public class SbcQueryRefundApplicationDetailResponse implements Serializable {

    @ApiModelProperty(value = "linkedmall退单详情")
    private QueryRefundApplicationDetailResponse.RefundApplicationDetail detail;
}
