package com.wanmi.sbc.customer.api.request.store;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * <p>店铺校验请求参数</p>
 * Created by of628-wenzhi on 2018-09-18-下午9:58.
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class StoreCheckRequest extends CustomerBaseRequest {
    private static final long serialVersionUID = -9035872691748396631L;

    /**
     * 需要校验的店铺id集合
     */
    @ApiModelProperty(value = "需要校验的店铺id集合")
    private List<Long> ids;
}
