package com.wanmi.sbc.order.api.request.trade;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author: wanggang
 * @createDate: 2018/12/3 10:06
 * @version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class VerifyStoreRequest implements Serializable {

    private static final long serialVersionUID = 1990419472888509559L;

    /**
     * 店铺ID集合
     */
    @ApiModelProperty(value = "店铺ID集合")
    private List<Long> storeIds;
}
