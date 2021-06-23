package com.wanmi.sbc.account.api.response.funds;

import com.wanmi.sbc.account.bean.vo.CustomerFundsDetailVO;
import com.wanmi.sbc.common.base.MicroServicePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 会员资金明细-分页查询对象
 * @author: Geek Wang
 * @createDate: 2019/2/19 11:06
 * @version: 1.0
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerFundsDetailPageResponse implements Serializable {

    @ApiModelProperty(value = "会员资金明细列表")
    private MicroServicePage<CustomerFundsDetailVO> microServicePage;
}
