package com.wanmi.sbc.customer.api.request.store;

import com.wanmi.sbc.common.enums.CompanySourceType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class StoreBycompanySourceType implements Serializable {
    @ApiModelProperty("商家来源类型 0:商城入驻 1:linkMall初始化")
    private CompanySourceType companySourceType;
}
