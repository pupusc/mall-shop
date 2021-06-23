package com.wanmi.sbc.customer.api.request.storelevel;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import lombok.*;

/**
 * @author yang
 * @since 2019/3/18
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreLevelByStoreIdRequest extends CustomerBaseRequest {

    private static final long serialVersionUID = 9154054750308699784L;

    /**
     * 店铺id
     */
    private Long storeId;
}
