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
 * 删除商家收款账户请求
 * Created by daiyitian on 2018/10/15.
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyAccountDeleteByIdRequest implements Serializable {

    private static final long serialVersionUID = 1462266013674905380L;

    /**
     * 线下账户id
     */
    @ApiModelProperty(value = "线下账户id")
    @NotNull
    private Long offlineAccountId;
}
