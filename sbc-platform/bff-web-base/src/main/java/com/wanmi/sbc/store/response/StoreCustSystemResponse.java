package com.wanmi.sbc.store.response;

import com.wanmi.sbc.customer.api.response.store.StoreBaseInfoResponse;
import com.wanmi.sbc.customer.bean.vo.CustomerLevelVO;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel
@Data
public class StoreCustSystemResponse {
    /**
     * 会员在该店铺的会员等级信息
     */
    @ApiModelProperty(value = "会员在该店铺的会员等级信息")
    private CustomerLevelVO level;

    /**
     * 店铺的会员体系
     */
    @ApiModelProperty(value = "店铺的会员体系")
    private List<CustomerLevelVO> levelList;

    /**
     * 店铺信息
     */
    @ApiModelProperty(value = "店铺信息")
    private StoreBaseInfoResponse store;

    public StoreCustSystemResponse(StoreVO store, CustomerLevelVO level, List<CustomerLevelVO> levelList) {
        this.setLevel(level);
        this.setLevelList(levelList);
        this.setStore(new StoreBaseInfoResponse().convertFromEntity(store));
    }

}
