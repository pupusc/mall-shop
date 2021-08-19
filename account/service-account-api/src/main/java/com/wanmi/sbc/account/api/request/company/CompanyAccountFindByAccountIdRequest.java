package com.wanmi.sbc.account.api.request.company;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 查询商家收款账户请求
 * Created by chenli on 2018/12/13.
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyAccountFindByAccountIdRequest implements Serializable {

    private static final long serialVersionUID = -2189821872078209513L;

    /**
     * 账户id
     */
    @ApiModelProperty(value = "账户id")
    @NotNull
    private Long accountId;
}
