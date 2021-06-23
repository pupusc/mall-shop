package com.wanmi.sbc.customer.api.request.account;

import com.wanmi.sbc.customer.bean.dto.CustomerAccountDTO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 会员银行账户-根据银行账号ID查询Request
 * @Author: wanggang
 * @CreateDate: 2018/9/11 11:05
 * @Version: 1.0
 */
@ApiModel
@Data
public class CustomerAccountOptionalRequest extends CustomerAccountDTO {

    private static final long serialVersionUID = 1074374392127287670L;
}
