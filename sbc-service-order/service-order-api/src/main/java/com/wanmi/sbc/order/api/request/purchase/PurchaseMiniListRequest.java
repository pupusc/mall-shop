package com.wanmi.sbc.order.api.request.purchase;

import com.wanmi.sbc.customer.bean.dto.CustomerDTO;
import com.wanmi.sbc.order.bean.dto.PurchaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p></p>
 * author: sunkun
 * Date: 2018-12-03
 */
@Data
@ApiModel
public class PurchaseMiniListRequest extends PurchaseQueryDTO {

    private static final long serialVersionUID = 6755875113792084803L;

    @ApiModelProperty(value = "客户信息")
    private CustomerDTO customer;

    /**
     * 是否是pc端访问或者社交分销关闭
     */
    @ApiModelProperty(value = "是否是pc端访问或者社交分销关闭")
    private Boolean pcAndNoOpenFlag;

}
