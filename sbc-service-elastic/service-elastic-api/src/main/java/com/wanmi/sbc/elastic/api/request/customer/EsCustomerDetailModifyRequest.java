package com.wanmi.sbc.elastic.api.request.customer;

import com.wanmi.sbc.elastic.bean.dto.customer.EsCustomerDetailDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EsCustomerDetailModifyRequest implements Serializable {

    /**
     * 会员详情信息
     */
    @ApiModelProperty(value = "会员详情信息")
    private EsCustomerDetailDTO esCustomerDetailDTO;
}
