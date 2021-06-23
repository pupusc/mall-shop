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
 * 店铺收藏关注响应
 * Created by bail on 2017/11/29.
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoreCustomerFollowExistsResponse implements Serializable {


    private static final long serialVersionUID = 6269619748842005893L;

    /**
     * 是否已关注
     */
    @ApiModelProperty(value = "是否已关注")
    private Boolean isFollowed;
}
