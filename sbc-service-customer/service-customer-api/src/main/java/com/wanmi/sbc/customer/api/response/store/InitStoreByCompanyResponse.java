package com.wanmi.sbc.customer.api.response.store;

import com.wanmi.sbc.customer.bean.vo.StoreVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>B2B模式下初始化店铺信息response</p>
 * Created by of628-wenzhi on 2018-09-12-下午2:56.
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InitStoreByCompanyResponse implements Serializable {

    private static final long serialVersionUID = -7266719356338509942L;

    /**
     * 店铺信息
     */
    @ApiModelProperty(value = "店铺信息")
    private StoreVO storeVO;
}
