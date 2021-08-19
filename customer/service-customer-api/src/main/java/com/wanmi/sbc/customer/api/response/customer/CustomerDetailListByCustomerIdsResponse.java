package com.wanmi.sbc.customer.api.response.customer;

import com.wanmi.sbc.customer.bean.vo.CustomerDetailFromEsVO;
import io.swagger.annotations.ApiModel;
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
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDetailListByCustomerIdsResponse implements Serializable {
    private static final long serialVersionUID = 2269702455839540265L;

    private List<CustomerDetailFromEsVO> customerDetailFromEsVOS;
}

