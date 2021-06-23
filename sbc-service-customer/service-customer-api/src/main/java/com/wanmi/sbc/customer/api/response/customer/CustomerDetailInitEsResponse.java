package com.wanmi.sbc.customer.api.response.customer;

import com.wanmi.sbc.customer.bean.vo.CustomerDetailInitEsVO;
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
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
@Data
public class CustomerDetailInitEsResponse implements Serializable {
    private static final long serialVersionUID = 2269702455839540265L;

    private List<CustomerDetailInitEsVO> customerDetailInitEsVOList;
}
