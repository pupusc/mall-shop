package com.wanmi.sbc.customer.api.response.address;

import com.wanmi.sbc.customer.bean.vo.CustomerDeliveryAddressVO;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 会员收货地址-修改Response
 */
@ApiModel
@Data
@Builder
public class CustomerDeliveryAddressModifyResponse extends CustomerDeliveryAddressVO implements Serializable{

    private static final long serialVersionUID = 3804652878213538177L;
}
