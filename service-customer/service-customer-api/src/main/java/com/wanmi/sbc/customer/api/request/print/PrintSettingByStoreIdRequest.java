package com.wanmi.sbc.customer.api.request.print;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * <p>根据id查询单个打印设置request</p>
 * Created by daiyitian on 2018-08-13-下午3:47.
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrintSettingByStoreIdRequest extends CustomerBaseRequest {

    private static final long serialVersionUID = 532880731185256387L;

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    @NotNull
    private Long storeId;

}