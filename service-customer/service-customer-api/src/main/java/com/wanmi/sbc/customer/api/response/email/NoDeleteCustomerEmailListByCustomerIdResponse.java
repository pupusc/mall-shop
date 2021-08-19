package com.wanmi.sbc.customer.api.response.email;

import com.wanmi.sbc.customer.bean.vo.CustomerEmailVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 获取邮箱列表请求类
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoDeleteCustomerEmailListByCustomerIdResponse implements Serializable {

    private static final long serialVersionUID = 4528720296354441073L;

    /**
     * 邮箱列表
     */
    @ApiModelProperty(value = "邮箱列表")
    private List<CustomerEmailVO> customerEmails;
}
