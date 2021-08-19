package com.wanmi.sbc.customer.api.request.store;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

/**
 * <p>根据ids查询任意店铺（包含已删除）列表request</p>
 * Created by of628-wenzhi on 2018-09-12-下午5:22.
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListStoreByIdsRequest extends CustomerBaseRequest {

    private static final long serialVersionUID = 2352344145239780398L;

    /**
     * 店铺id集合
     */
    @ApiModelProperty(value = "店铺id集合")
    private List<Long> storeIds;
}
