package com.wanmi.sbc.customer.api.request.store;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * <p>根据店铺id查询任意（包含已删除）店铺基本信息request</p>
 * Created by of628-wenzhi on 2018-09-12-下午2:46.
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreByIdRequest extends CustomerBaseRequest{

    private static final long serialVersionUID = -125741706415554266L;
    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    @NotNull
    private Long storeId;
}
