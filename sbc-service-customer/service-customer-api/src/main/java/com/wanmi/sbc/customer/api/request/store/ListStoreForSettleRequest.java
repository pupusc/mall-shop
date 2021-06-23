package com.wanmi.sbc.customer.api.request.store;

import com.wanmi.sbc.common.enums.StoreType;
import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

/**
 * <p>查询账期内有效店铺request</p>
 * Created by of628-wenzhi on 2018-09-12-下午7:05.
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListStoreForSettleRequest extends CustomerBaseRequest {
    private static final long serialVersionUID = 8906623733675967093L;

    @ApiModelProperty(value = "账期")
    @NotNull
    @Range(max = 31, min = 1)
    private Integer targetDay;

    @ApiModelProperty(value = "商家类型")
    private StoreType storeType;
}
