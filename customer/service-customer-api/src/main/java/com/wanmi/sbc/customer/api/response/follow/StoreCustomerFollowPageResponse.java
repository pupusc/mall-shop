package com.wanmi.sbc.customer.api.response.follow;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.customer.bean.vo.StoreCustomerFollowVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 店铺收藏响应
 * Created by bail on 2017/11/29.
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoreCustomerFollowPageResponse implements Serializable {


    private static final long serialVersionUID = 6269619748842005893L;

    /**
     * 店铺收藏分页数据
     */
    @ApiModelProperty(value = "店铺收藏分页数据")
    private MicroServicePage<StoreCustomerFollowVO> customerFollowVOPage;
}
