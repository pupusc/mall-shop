package com.wanmi.sbc.customer.api.response.address;

import com.wanmi.sbc.customer.bean.vo.CustomerDeliveryAddressVO;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 会员收货地址-添加Response
 */
@ApiModel
@Data
@Builder
public class CustomerDeliveryAddressAddResponse extends CustomerDeliveryAddressVO implements Serializable{

    private static final long serialVersionUID = 7346333480324482834L;
}
