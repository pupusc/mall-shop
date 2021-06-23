package com.wanmi.sbc.customer.api.request.storelevel;

import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModelProperty;
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
public class CustomerLevelRequest extends CustomerBaseRequest {

    /**
     * 店铺id
     */
    private Long storeId;

    /**
     * 是否平台等级 （1平台（自营店铺属于平台等级） 0店铺）
     */
    @ApiModelProperty(value = "是否平台等级")
    private DefaultFlag  levelType;
}
