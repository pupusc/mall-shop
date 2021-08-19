package com.wanmi.sbc.goods.api.request.presellsale;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel
public class PresellSaleByIdDeleteRequest implements Serializable {

    @ApiModelProperty(value = "预售活动id")
    private String presellSaleId;

    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人")
    private String update_person;
}
