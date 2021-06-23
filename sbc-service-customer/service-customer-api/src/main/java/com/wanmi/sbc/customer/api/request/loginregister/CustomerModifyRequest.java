package com.wanmi.sbc.customer.api.request.loginregister;

import com.wanmi.sbc.customer.bean.dto.CustomerDTO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * 会员登录注册-修改Request
 */
@ApiModel
@Data
public class CustomerModifyRequest extends CustomerDTO implements Serializable {

    private static final long serialVersionUID = -6924180773097979990L;
}
