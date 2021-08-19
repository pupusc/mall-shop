package com.wanmi.sbc.customer.provider.impl.email;

import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.response.email.CustomerEmailAddResponse;
import com.wanmi.sbc.customer.api.response.email.CustomerEmailModifyResponse;
import com.wanmi.sbc.customer.bean.vo.CustomerEmailVO;
import com.wanmi.sbc.customer.email.model.root.CustomerEmail;

import java.util.List;
import java.util.stream.Collectors;

/**
 * bean转换类
 */
public class CustomerEmailConvert {
    public  static List<CustomerEmailVO> toVO(List<CustomerEmail> emails) {
        return emails.stream().map(customerEmail -> {
            CustomerEmailVO vo = new CustomerEmailVO();
            KsBeanUtil.copyPropertiesThird(customerEmail, vo);

            return vo;
        }).collect(Collectors.toList());
    }

    public static CustomerEmailAddResponse toAddResponse(CustomerEmail customerEmail) {
        CustomerEmailAddResponse response = new CustomerEmailAddResponse();

        KsBeanUtil.copyPropertiesThird(customerEmail, response);

        return response;
    }

    public static CustomerEmailModifyResponse toModifyResponse(CustomerEmail customerEmail) {
        CustomerEmailModifyResponse response = new CustomerEmailModifyResponse();

        KsBeanUtil.copyPropertiesThird(customerEmail, response);

        return response;
    }
}
