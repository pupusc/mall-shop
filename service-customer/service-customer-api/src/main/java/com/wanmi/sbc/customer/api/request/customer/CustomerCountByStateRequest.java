package com.wanmi.sbc.customer.api.request.customer;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import com.wanmi.sbc.customer.bean.enums.CheckState;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerCountByStateRequest extends CustomerBaseRequest {
    private static final long serialVersionUID = -6700338370684244828L;

    @ApiModelProperty(value = "审核状态审核状态(0:待审核,1:已审核,2:审核未通过)")
    private CheckState checkState;

    @ApiModelProperty(value = "删除标记")
    private DeleteFlag deleteFlag = DeleteFlag.NO;
}
