package com.wanmi.sbc.order.api.request.purchase;

import com.wanmi.sbc.order.bean.dto.PurchaseSaveDTO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * <p></p>
 * author: sunkun
 * Date: 2018-11-30
 */
@Data
@ApiModel
public class PurchaseBatchSaveRequest extends PurchaseSaveDTO {

    private static final long serialVersionUID = -3559582099527002037L;

}
