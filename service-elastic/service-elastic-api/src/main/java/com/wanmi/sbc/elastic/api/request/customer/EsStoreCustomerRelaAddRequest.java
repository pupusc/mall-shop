package com.wanmi.sbc.elastic.api.request.customer;

import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.customer.bean.dto.StoreCustomerRelaDTO;
import com.wanmi.sbc.elastic.bean.dto.customer.EsStoreCustomerRelaDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <Description> <br>
 *
 * @author hejiawen<br>
 * @version 1.0<br>
 * @taskId <br>
 * @createTime 2018-09-12 14:47 <br>
 * @see com.wanmi.sbc.customer.api.request.store <br>
 * @since V1.0<br>
 */
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
@Data
public class EsStoreCustomerRelaAddRequest extends BaseRequest {

    private static final long serialVersionUID = 5341571993960683235L;

    /**
     * 店铺-用户关系 {@link StoreCustomerRelaDTO}
     */
    @ApiModelProperty(value = "店铺-用户关系")
    private EsStoreCustomerRelaDTO storeCustomerRelaDTO;
}

