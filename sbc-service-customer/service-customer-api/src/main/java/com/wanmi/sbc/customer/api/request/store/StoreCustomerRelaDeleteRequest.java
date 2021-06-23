package com.wanmi.sbc.customer.api.request.store;

import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.customer.bean.dto.StoreCustomerRelaDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <Description> <br>
 *
 * @author hejiawen<br>
 * @version 1.0<br>
 * @taskId <br>
 * @createTime 2018-09-12 14:55 <br>
 * @see com.wanmi.sbc.customer.api.request.store <br>
 * @since V1.0<br>
 */

@ApiModel
@Data
public class StoreCustomerRelaDeleteRequest  extends BaseRequest {

    private static final long serialVersionUID = 7161467477562613625L;

    /**
     * 店铺-用户关系 {@link StoreCustomerRelaDTO}
     */
    @ApiModelProperty(value = "店铺-用户关系")
    private StoreCustomerRelaDTO storeCustomerRelaDTO;
}
