package com.wanmi.sbc.customer.api.response.follow;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 店铺收藏批量关注响应
 * Created by bail on 2017/11/29.
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoreCustomerFollowExistsBatchResponse implements Serializable {


    private static final long serialVersionUID = 6269619748842005893L;

    /**
     * 已关注的店铺ID
     */
    @ApiModelProperty(value = "已关注的店铺ID")
    private List<Long> storeIds;
}
