package com.wanmi.sbc.customer.api.response.account;

import com.wanmi.sbc.customer.bean.vo.CustomerAccountVO;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

/**
 * @Author: wanggang
 * @CreateDate: 2018/9/11 11:07
 * @Version: 1.0
 */
@ApiModel
@Data
@Builder
public class CustomerAccountModifyResponse extends CustomerAccountVO{

    private static final long serialVersionUID = 986634516988917371L;
}
