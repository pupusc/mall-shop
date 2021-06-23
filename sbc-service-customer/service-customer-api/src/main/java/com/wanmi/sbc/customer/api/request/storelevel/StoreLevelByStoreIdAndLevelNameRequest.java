package com.wanmi.sbc.customer.api.request.storelevel;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * @author yang
 * @since 2019/3/18
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreLevelByStoreIdAndLevelNameRequest extends CustomerBaseRequest {

    private static final long serialVersionUID = -3406770272879115001L;

    /**
     * 等级名称
     */
    @NotNull
    private String levelName;

    /**
     * 店铺编号
     */
    @NotNull
    private Long storeId;
}
