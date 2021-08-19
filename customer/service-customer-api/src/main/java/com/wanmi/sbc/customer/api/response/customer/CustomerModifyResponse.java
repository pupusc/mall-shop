package com.wanmi.sbc.customer.api.response.customer;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import com.wanmi.sbc.customer.bean.vo.CustomerDetailToEsVO;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerModifyResponse implements Serializable {
    private static final long serialVersionUID = -3854269857592932191L;

    private CustomerDetailToEsVO customerDetailToEsVO;

}
