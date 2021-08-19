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
 * <p>店铺签约和商家信息修改response</p>
 * Created by of628-wenzhi on 2018-09-18-下午10:04.
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreContractModifyResponse implements Serializable{
    private static final long serialVersionUID = 707232612689625808L;

    /**
     * 修改后的店铺信息
     */
    @ApiModelProperty(value = "修改后的店铺信息")
    private StoreVO storeVO;
}
