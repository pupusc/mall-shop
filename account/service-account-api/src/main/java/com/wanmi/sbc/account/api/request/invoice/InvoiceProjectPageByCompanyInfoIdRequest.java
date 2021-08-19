package com.wanmi.sbc.account.api.request.invoice;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 开票项目分页请求
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceProjectPageByCompanyInfoIdRequest implements Serializable {


    private static final long serialVersionUID = -8294529579630208706L;

    /**
     * 分页请求
     */
    @ApiModelProperty(value = "分页请求")
    @NotNull
    private BaseQueryRequest baseQueryRequest;

    /**
     * 公司信息Id
     */
    @ApiModelProperty(value = "公司信息Id")
    @NotNull
    private Long companyInfoId;
}
