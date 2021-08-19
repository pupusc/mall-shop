package com.wanmi.sbc.order.api.request.purchase;

import com.wanmi.sbc.common.enums.BoolFlag;
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
public class PurchaseUpdateNumRequest extends PurchaseSaveDTO {

    private static final long serialVersionUID = 3517002784178075668L;

    // 是否更新购物车时间
    private BoolFlag updateTimeFlag = BoolFlag.YES;

}
