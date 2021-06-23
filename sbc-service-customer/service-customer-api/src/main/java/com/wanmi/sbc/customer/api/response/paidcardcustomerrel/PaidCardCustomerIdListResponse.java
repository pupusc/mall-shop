package com.wanmi.sbc.customer.api.response.paidcardcustomerrel;

import com.wanmi.sbc.customer.bean.vo.PaidCardCustomerRelVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @program: sbc-background
 * @description:
 * @author: Mr.Tian
 * @create: 2021-05-11 09:56
 **/
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaidCardCustomerIdListResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 付费会员列表结果
     */
    @ApiModelProperty(value = "付费会员Id列表结果")
    private List<String> customersIdList;
}
