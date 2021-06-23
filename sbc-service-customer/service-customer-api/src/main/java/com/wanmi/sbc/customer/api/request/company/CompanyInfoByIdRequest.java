package com.wanmi.sbc.customer.api.request.company;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * <p>根据id查询单个公司信息request</p>
 * Created by daiyitian on 2018-08-13-下午3:47.
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyInfoByIdRequest extends CustomerBaseRequest {

    private static final long serialVersionUID = 2304827735774417750L;
    /**
     * 公司信息id
     */
    @ApiModelProperty(value = "供应商id列表")
    @NotNull
    private Long companyInfoId;

}
