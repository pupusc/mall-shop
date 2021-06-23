package com.wanmi.sbc.account.api.response.funds;

import com.wanmi.sbc.account.bean.vo.CustomerFundsForEsVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 会员资金列表
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerFundsListResponse implements Serializable {

    private static final long serialVersionUID = -4920262534476730225L;

    /**
     * 会员资金列表
     */
    @ApiModelProperty(value = "会员资金列表")
    private List<CustomerFundsForEsVO> lists;
}
