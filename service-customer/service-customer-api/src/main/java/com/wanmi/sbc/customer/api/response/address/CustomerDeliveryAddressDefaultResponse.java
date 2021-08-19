package com.wanmi.sbc.customer.api.response.address;

import com.wanmi.sbc.customer.bean.vo.CustomerDeliveryAddressVO;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;


@ApiModel
@Data
@Builder
public class CustomerDeliveryAddressDefaultResponse extends CustomerDeliveryAddressVO implements Serializable{

    private static final long serialVersionUID = 9014012869896411103L;
}
