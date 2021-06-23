package com.wanmi.sbc.customer.api.response.address;

import com.wanmi.sbc.customer.bean.vo.CustomerDeliveryAddressVO;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

/**
 * 会员收货地址-根据收货地址ID查询Response
 */
@ApiModel
@Data
@Builder
public class CustomerDeliveryAddressByIdResponse extends CustomerDeliveryAddressVO {

    private static final long serialVersionUID = 476693859521872573L;
}
