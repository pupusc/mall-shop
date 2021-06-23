package com.wanmi.sbc.customer.api.request.loginregister;

import com.wanmi.sbc.customer.bean.dto.ThirdLoginRelationDTO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * 会员登录注册-绑定第三方账号Request
 */
@ApiModel
@Data
public class CustomerBindThirdAccountRequest extends ThirdLoginRelationDTO implements Serializable {
    private static final long serialVersionUID = 2490956768076028574L;
}
