package com.wanmi.sbc.customer.api.request.store;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * <p>B2B场景下，根据商家初始化店铺信息request</p>
 * Created by of628-wenzhi on 2018-09-12-下午2:46.
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InitStoreByCompanyRequest extends CustomerBaseRequest {

    private static final long serialVersionUID = 8882761476585829380L;

    /**
     * 商家id
     */
    @ApiModelProperty(value = "商家id")
    @NotNull
    private Long companyInfoId;
}
