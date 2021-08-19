package com.wanmi.sbc.customer.api.response.customer;

import com.wanmi.sbc.customer.bean.vo.CustomerDetailVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 会员信息响应
 * Created by CHENLI on 2017/4/19.
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDetailListByConditionResponse implements Serializable {
    private static final long serialVersionUID = -4561855333716527650L;
    /**
     * 会员信息列表
     */
    @ApiModelProperty(value = "会员信息列表")
    private List<CustomerDetailVO> detailResponseList;
}
