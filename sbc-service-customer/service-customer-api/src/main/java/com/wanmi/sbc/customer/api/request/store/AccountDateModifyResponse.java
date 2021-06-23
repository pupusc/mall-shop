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
 * <p>店铺对账日期设置response</p>
 * Created by of628-wenzhi on 2018-09-18-下午8:44.
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountDateModifyResponse implements Serializable {
    private static final long serialVersionUID = 8554618276367322284L;

    /**
     * 包含结算日期在内的店铺信息
     */
    @ApiModelProperty(value = "包含结算日期在内的店铺信息")
    private StoreVO storeVO;
}
