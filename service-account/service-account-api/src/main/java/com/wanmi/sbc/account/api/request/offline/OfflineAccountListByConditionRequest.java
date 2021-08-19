package com.wanmi.sbc.account.api.request.offline;

import com.wanmi.sbc.account.api.request.AccountBaseRequest;
import com.wanmi.sbc.common.enums.DefaultFlag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 条件查询线下账户列表请求
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfflineAccountListByConditionRequest extends AccountBaseRequest {
    private static final long serialVersionUID = -555606927620331370L;

    /**
     * 公司信息Id
     */
    @ApiModelProperty(value = "公司信息Id")
    @NotNull
    private Long companyInfoId;

    /**
     * 默认标识
     */
    @ApiModelProperty(value = "默认标识")
    @NotNull
    private DefaultFlag defaultFlag;
}
