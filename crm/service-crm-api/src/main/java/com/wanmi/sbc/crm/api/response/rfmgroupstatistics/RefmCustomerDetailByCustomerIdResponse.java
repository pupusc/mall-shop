package com.wanmi.sbc.crm.api.response.rfmgroupstatistics;

import com.wanmi.sbc.crm.bean.vo.RfmCustomerDetailVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>根据customerId查询最近一天的rfm会员统计明细response</p>
 * @author dyt
 * @date 2019-11-12 14:49:08
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel
@Data
public class RefmCustomerDetailByCustomerIdResponse extends RfmCustomerDetailVO {
    private static final long serialVersionUID = 1L;
}
