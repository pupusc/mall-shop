package com.wanmi.sbc.customer.api.response.level;

import com.wanmi.sbc.customer.bean.vo.CustomerLevelVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author yang
 * @since 2019/3/4
 */

@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class CustomerLevelByCustomerIdResponse extends CustomerLevelVO implements Serializable {

    private static final long serialVersionUID = -847125563881124800L;

}
