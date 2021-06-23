package com.wanmi.sbc.goods.api.request.freight;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 根据店铺id查询默认单品运费模板请求
 * Created by daiyitian on 2018/10/31.
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FreightTemplateGoodsDefaultByStoreIdRequest implements Serializable {


    private static final long serialVersionUID = -3381987177603611250L;

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    @NotNull
    private Long storeId;

}
