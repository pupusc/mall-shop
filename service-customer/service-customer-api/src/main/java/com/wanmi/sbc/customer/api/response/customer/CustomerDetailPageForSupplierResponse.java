package com.wanmi.sbc.customer.api.response.customer;

import com.wanmi.sbc.customer.bean.vo.CustomerDetailPageVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 会员信息响应
 * Created by CHENLI on 2017/4/19.
 */
@ApiModel
@Data
public class CustomerDetailPageForSupplierResponse extends CustomerDetailPageVO {
    private static final long serialVersionUID = 2269702455839540265L;
}
