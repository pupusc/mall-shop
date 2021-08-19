package com.wanmi.sbc.customer.api.response.address;

import com.wanmi.sbc.customer.bean.vo.CustomerDeliveryAddressVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDeliveryAddressListResponse implements Serializable {

    private static final long serialVersionUID = -209744202815448213L;

    @ApiModelProperty(value = "会员收货地址")
    private List<CustomerDeliveryAddressVO> customerDeliveryAddressVOList = new ArrayList<>();
}
