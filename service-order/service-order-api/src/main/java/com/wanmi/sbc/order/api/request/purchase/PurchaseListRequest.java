package com.wanmi.sbc.order.api.request.purchase;

import com.wanmi.sbc.order.bean.dto.PurchaseQueryDTO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * <p></p>
 * author: sunkun
 * Date: 2018-12-03
 */
@Data
@ApiModel
public class PurchaseListRequest extends PurchaseQueryDTO {

    private static final long serialVersionUID = -454248433046596178L;

}
