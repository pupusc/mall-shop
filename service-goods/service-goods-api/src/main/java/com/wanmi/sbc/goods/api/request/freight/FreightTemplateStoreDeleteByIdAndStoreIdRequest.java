package com.wanmi.sbc.goods.api.request.freight;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 根据运费模板id删除店铺运费模板请求数据结构
 * Created by daiyitian on 2018/5/3.
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FreightTemplateStoreDeleteByIdAndStoreIdRequest implements Serializable {

    private static final long serialVersionUID = -2287001752202532435L;

    /**
     * 运费模板id
     */
    @ApiModelProperty(value = "运费模板id")
    @NotNull
    private Long freightTempId;

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    @NotNull
    private Long storeId;

}
