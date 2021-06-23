package com.wanmi.sbc.customer.api.response.customer;

import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerBaseListByIdsResponse {


    @ApiModelProperty(value = "会员信息列表")
    private List<CustomerVO> customerVOList;
}
