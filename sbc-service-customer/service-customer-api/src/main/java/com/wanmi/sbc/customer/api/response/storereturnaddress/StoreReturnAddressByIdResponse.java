package com.wanmi.sbc.customer.api.response.storereturnaddress;

import com.wanmi.sbc.customer.bean.vo.StoreReturnAddressVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>根据id查询任意（包含已删除）店铺退货地址表信息response</p>
 * @author dyt
 * @date 2020-11-02 11:38:39
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreReturnAddressByIdResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 店铺退货地址表信息
     */
    @ApiModelProperty(value = "店铺退货地址表信息")
    private StoreReturnAddressVO storeReturnAddressVO;
}
