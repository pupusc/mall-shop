package com.wanmi.sbc.customer.api.request.store;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * <p>根据店铺名称查询未删除店铺request</p>
 * Created by of628-wenzhi on 2018-09-12-下午7:27.
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListStoreByNameRequest extends CustomerBaseRequest {

    private static final long serialVersionUID = 7168151477298853933L;
    /**
     * 店铺名称关键字
     */
    @ApiModelProperty(value = "店铺名称关键字")
    private String storeName;
}
