package com.wanmi.sbc.goods.api.request.freight;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FreightTemplateGoodsExpressByIdRequest implements Serializable {

    private static final long serialVersionUID = 8060701404541704160L;

    /**
     * 运费模板id
     */
    @ApiModelProperty(value = "运费模板id")
    @NotNull
    private Long freightTempId;

}
