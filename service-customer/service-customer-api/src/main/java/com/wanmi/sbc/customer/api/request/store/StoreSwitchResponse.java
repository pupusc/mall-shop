package com.wanmi.sbc.customer.api.request.store;

import com.wanmi.sbc.customer.bean.vo.StoreVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>开关店返回店铺信息</p>
 * Created by of628-wenzhi on 2018-09-18-下午9:47.
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreSwitchResponse implements Serializable{
    private static final long serialVersionUID = -9046698503354973510L;

    /**
     * 店铺信息
     */
    @ApiModelProperty(value = "店铺信息")
    private StoreVO storeVO;
}
