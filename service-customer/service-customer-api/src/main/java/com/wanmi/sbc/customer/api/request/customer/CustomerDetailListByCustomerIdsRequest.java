package com.wanmi.sbc.customer.api.request.customer;

import com.wanmi.sbc.customer.bean.dto.CustomerDetailFromEsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 会员详情查询参数
 * Created by CHENLI on 2017/4/19.
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDetailListByCustomerIdsRequest implements Serializable {

    private static final long serialVersionUID = -1281379836937760934L;
    /**
     * 会员详细信息标识UUID
     */
    @ApiModelProperty(value = "会员详细信息标识UUID")
    private List<CustomerDetailFromEsDTO> dtoList;

    private Long companyInfoId;
}
