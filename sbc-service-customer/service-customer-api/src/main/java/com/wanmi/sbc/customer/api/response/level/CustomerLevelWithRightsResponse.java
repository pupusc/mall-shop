package com.wanmi.sbc.customer.api.response.level;

import com.wanmi.sbc.customer.bean.vo.CustomerLevelVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author yang
 * @since 2019/3/5
 */
@ApiModel
@Data
public class CustomerLevelWithRightsResponse implements Serializable {

    private static final long serialVersionUID = -4200267457574154477L;

    /**
     * 等级信息
     */
    private List<CustomerLevelVO> customerLevelVOList;
}
