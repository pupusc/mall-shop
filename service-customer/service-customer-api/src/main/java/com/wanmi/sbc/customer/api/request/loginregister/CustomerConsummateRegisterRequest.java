package com.wanmi.sbc.customer.api.request.loginregister;

import com.wanmi.sbc.customer.bean.dto.CustomerDetailDTO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * 会员登录注册-完善注册信息Request
 */
@ApiModel
@Data
public class CustomerConsummateRegisterRequest extends CustomerDetailDTO implements Serializable {

    private static final long serialVersionUID = -4275240405991430899L;
}
