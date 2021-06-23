package com.wanmi.sbc.customer.api.request.storelevel;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * @author yang
 * @since 2019/3/6
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreLevelByCustomerIdAndStoreIdRequest extends BaseQueryRequest {

    private static final long serialVersionUID = -7922894070104607201L;

    /**
     * 客户编号
     */
    @NotNull
    private String customerId;

    /**
     * 店铺编号
     */
    @NotNull
    private Long storeId;
}
