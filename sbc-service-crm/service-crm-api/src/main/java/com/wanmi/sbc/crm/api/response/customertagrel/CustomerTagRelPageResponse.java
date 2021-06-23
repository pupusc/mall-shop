package com.wanmi.sbc.crm.api.response.customertagrel;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.crm.bean.vo.CustomerTagRelVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>会员标签关联分页结果</p>
 * @author dyt
 * @date 2019-11-12 14:49:08
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerTagRelPageResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 会员标签关联分页结果
     */
    @ApiModelProperty(value = "会员标签关联分页结果")
    private MicroServicePage<CustomerTagRelVO> customerTagRelVOPage;
}
