package com.wanmi.sbc.customer.api.request.store;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * <p>前端会员根据店铺id查询店铺基础信息request</p>
 * Created by of628-wenzhi on 2018-09-12-下午3:09.
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreBaseInfoByIdRequest extends CustomerBaseRequest{
    private static final long serialVersionUID = -4658929492666091501L;

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    @NotNull
    private Long storeId;
}
