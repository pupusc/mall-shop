package com.wanmi.sbc.order.api.request.purchase;

import com.wanmi.sbc.order.bean.dto.PurchaseSaveDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p></p>
 * author: sunkun
 * Date: 2018-11-30
 */
@Data
@ApiModel
public class PurchaseSaveRequest extends PurchaseSaveDTO {

    private static final long serialVersionUID = -7860637469700165619L;

    /**
     * 门店id
     */
    @ApiModelProperty(value = "门店id")
    private Long storeId;



}
