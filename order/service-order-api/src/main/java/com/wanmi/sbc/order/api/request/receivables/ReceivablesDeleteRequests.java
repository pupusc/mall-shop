package com.wanmi.sbc.order.api.request.receivables;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel
public class ReceivablesDeleteRequests  implements Serializable {

    @ApiModelProperty(value = "content")
    List<String> content;
}
