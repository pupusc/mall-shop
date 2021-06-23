package com.wanmi.sbc.customer.api.request.store;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * <p>根据companyIds查询未删除店铺列表request</p>
 * Created by of628-wenzhi on 2018-09-12-下午5:22.
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListCompanyStoreByCompanyIdsRequest extends CustomerBaseRequest {

    private static final long serialVersionUID = -2003428237705584988L;

    /**
     * 公司id集合
     */
    @ApiModelProperty(value = "公司id集合")
    @NotEmpty
    private List<Long> companyIds;

    /**
     * 店铺id集合
     */
    @ApiModelProperty(value = "店铺id集合")
    @NotEmpty
    private List<Long> storeIds;
}
