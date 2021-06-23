package com.wanmi.sbc.customer.api.response.account;

import com.wanmi.sbc.customer.bean.vo.CustomerAccountVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: wanggang
 * @CreateDate: 2018/9/11 11:07
 * @Version: 1.0
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerAccountListResponse implements Serializable {

    private static final long serialVersionUID = 7563058556548541276L;

    @ApiModelProperty(value = "会员银行账号")
    private List<CustomerAccountVO> customerAccountVOList;
}
