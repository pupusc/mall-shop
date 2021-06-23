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
 * 线下账户获取请求
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfflineAccountGetByIdRequest extends AccountBaseRequest {
    private static final long serialVersionUID = -2796086396878270685L;

    /**
     * 线下账户Id
     */
    @ApiModelProperty(value = "线下账户Id")
    @NotNull
    private Long offlineAccountId;
}
