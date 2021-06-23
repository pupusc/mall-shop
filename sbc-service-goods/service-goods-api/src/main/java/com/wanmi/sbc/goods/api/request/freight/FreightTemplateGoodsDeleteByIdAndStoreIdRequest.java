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
 * 根据单品运费模板id和店铺id删除单品运费模板请求
 * Created by daiyitian on 2018/10/31.
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FreightTemplateGoodsDeleteByIdAndStoreIdRequest implements Serializable {

    private static final long serialVersionUID = -7157678083564667273L;

    /**
     * 单品运费模板id
     */
    @ApiModelProperty(value = "单品运费模板id")
    @NotNull
    private Long freightTempId;

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    @NotNull
    private Long storeId;

}
