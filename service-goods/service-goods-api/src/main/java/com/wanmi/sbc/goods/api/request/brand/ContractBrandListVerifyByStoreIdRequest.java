package com.wanmi.sbc.goods.api.request.brand;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 根据店铺id校验自定义品牌是否与平台重复请求结构
 * Created by daiyitian on 2018/11/02.
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContractBrandListVerifyByStoreIdRequest implements Serializable {

    private static final long serialVersionUID = 4515178783252329105L;

    /**
     * 店铺主键
     */
    @ApiModelProperty(value = "店铺主键")
    @NotNull
    private Long storeId;

}
