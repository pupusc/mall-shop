package com.wanmi.sbc.customer.api.response.customer;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import com.wanmi.sbc.customer.bean.vo.CustomerDetailWithImgVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel
@Data
public class CustomerIdsListResponse extends CustomerBaseRequest {
    private static final long serialVersionUID = -1585004539318321019L;

    @ApiModelProperty(value = "会员信息列表")
    private List<CustomerDetailWithImgVO> customerVOList;
}
