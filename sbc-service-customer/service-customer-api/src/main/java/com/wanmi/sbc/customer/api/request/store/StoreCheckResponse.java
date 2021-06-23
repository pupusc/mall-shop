package com.wanmi.sbc.customer.api.request.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>店铺校验结果</p>
 * Created by of628-wenzhi on 2018-09-18-下午9:59.
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreCheckResponse implements Serializable {
    private static final long serialVersionUID = 42242716506932777L;

    /**
     * true|false:有效|无效,只要有一个失效，则返回false
     */
    @ApiModelProperty(value = "店铺校验结果")
    private Boolean result;
}
