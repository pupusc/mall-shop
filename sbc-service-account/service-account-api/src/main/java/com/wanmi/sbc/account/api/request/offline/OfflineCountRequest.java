package com.wanmi.sbc.account.api.request.offline;

import com.wanmi.sbc.account.api.request.AccountBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 线下账户统计请求
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfflineCountRequest extends AccountBaseRequest {
    private static final long serialVersionUID = -846618877540864857L;

    /**
     * 公司信息Id
     */
    @ApiModelProperty(value = "公司信息Id")
    @NotNull
    private Long companyInfoId;
}
