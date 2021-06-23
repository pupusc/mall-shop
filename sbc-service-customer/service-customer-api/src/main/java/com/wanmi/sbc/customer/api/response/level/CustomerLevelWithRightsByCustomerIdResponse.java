package com.wanmi.sbc.customer.api.response.level;

import com.wanmi.sbc.customer.bean.vo.CustomerLevelVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author yang
 * @since 2019/3/5
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class CustomerLevelWithRightsByCustomerIdResponse extends CustomerLevelVO implements Serializable {

    private static final long serialVersionUID = 8152360345775624432L;

    /**
     * 客户成长值
     */
    private Long customerGrowthValue;

    /**
     * 客户姓名
     */
    private String customerName;

    /**
     * 客户头像
     */
    private String headImg;


}
