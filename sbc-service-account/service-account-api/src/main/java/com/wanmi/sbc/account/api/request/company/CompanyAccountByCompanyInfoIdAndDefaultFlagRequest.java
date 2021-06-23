package com.wanmi.sbc.account.api.request.company;

import com.wanmi.sbc.common.enums.DefaultFlag;
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
 * Created by daiyitian on 2018/10/15.
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyAccountByCompanyInfoIdAndDefaultFlagRequest implements Serializable {

    private static final long serialVersionUID = 1462266013674905380L;

    /**
     * 公司信息id
     */
    @ApiModelProperty(value = "公司信息id")
    @NotNull
    private Long companyInfoId;

    /**
     * 默认标识
     */
    @ApiModelProperty(value = "默认标识")
    @NotNull
    private DefaultFlag defaultFlag;
}
