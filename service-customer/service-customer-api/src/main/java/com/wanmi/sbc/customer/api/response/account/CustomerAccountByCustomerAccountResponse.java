package com.wanmi.sbc.customer.api.response.account;

import com.wanmi.sbc.customer.bean.vo.CustomerAccountVO;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: wanggang
 * @CreateDate: 2018/9/11 11:07
 * @Version: 1.0
 */
@ApiModel
@Data
@Builder
public class CustomerAccountByCustomerAccountResponse extends CustomerAccountVO implements Serializable{

    private static final long serialVersionUID = 8243501884058863765L;

}
