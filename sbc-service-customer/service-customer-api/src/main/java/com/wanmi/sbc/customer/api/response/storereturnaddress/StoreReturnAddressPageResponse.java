package com.wanmi.sbc.customer.api.response.storereturnaddress;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.customer.bean.vo.StoreReturnAddressVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>店铺退货地址表分页结果</p>
 * @author dyt
 * @date 2020-11-02 11:38:39
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreReturnAddressPageResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 店铺退货地址表分页结果
     */
    @ApiModelProperty(value = "店铺退货地址表分页结果")
    private MicroServicePage<StoreReturnAddressVO> storeReturnAddressVOPage;
}
