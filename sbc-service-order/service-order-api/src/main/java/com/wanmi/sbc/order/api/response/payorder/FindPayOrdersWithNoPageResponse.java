package com.wanmi.sbc.order.api.response.payorder;

import com.wanmi.sbc.order.bean.vo.PayOrderResponseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
@Data
@ApiModel
public class FindPayOrdersWithNoPageResponse  implements Serializable {

    @ApiModelProperty(value = "支付单列表")
    private List<PayOrderResponseVO> payOrderResponses;

    @ApiModelProperty(value = "当前页",example = "0")
    private Integer currentPage;

    @ApiModelProperty(value = "每页记录数",example = "0")
    private Integer pageSize;

    @ApiModelProperty(value = "总数",example = "0")
    private Long total;
}

