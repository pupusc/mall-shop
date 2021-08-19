package com.wanmi.sbc.customer.api.response.loginregister;

import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

/**
 * 客户信息主表
 * Created by CHENLI on 2017/4/13.
 */
@ApiModel
@Data
@Builder
public class CustomerByAccountResponse extends CustomerVO {

    private static final long serialVersionUID = -5519120120296852262L;
}
