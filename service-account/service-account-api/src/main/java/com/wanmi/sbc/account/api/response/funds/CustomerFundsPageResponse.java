package com.wanmi.sbc.account.api.response.funds;

import com.wanmi.sbc.account.bean.vo.CustomerFundsVO;
import com.wanmi.sbc.common.base.MicroServicePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 会员资金-分页对象
 * @author: Geek Wang
 * @createDate: 2019/2/19 11:06
 * @version: 1.0
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerFundsPageResponse implements Serializable {

    @ApiModelProperty(value = "会员资金列表")
    private MicroServicePage<CustomerFundsVO> microServicePage;
}
