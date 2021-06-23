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
 * <p>添加店铺后返回的店铺信息</p>
 * Created by of628-wenzhi on 2018-09-18-下午8:32.
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreAddResponse implements Serializable{
    private static final long serialVersionUID = 6038328664691487166L;

    /**
     * 已添加的店铺信息
     */
    @ApiModelProperty(value = "已添加的店铺信息")
    private StoreVO storeVO;
}
