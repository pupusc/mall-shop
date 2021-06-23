package com.wanmi.sbc.customer.api.response.email;

import com.wanmi.sbc.customer.bean.vo.CustomerEmailVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 邮箱服务器设置
 */
@ApiModel
@Data
public class CustomerEmailAddResponse extends CustomerEmailVO {

    private static final long serialVersionUID = -4587973124340179476L;

}
