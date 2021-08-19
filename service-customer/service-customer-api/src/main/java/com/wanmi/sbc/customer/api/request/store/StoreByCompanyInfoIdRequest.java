package com.wanmi.sbc.customer.api.request.store;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * <p>根据商家id查询店铺基本信息request</p>
 * Created by of628-wenzhi on 2018-09-12-下午2:46.
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreByCompanyInfoIdRequest extends CustomerBaseRequest{

    private static final long serialVersionUID = -5191413261379833651L;

    /**
     * 商家id
     */
    @ApiModelProperty(value = "商家id")
    @NotNull
    private Long companyInfoId;
}
