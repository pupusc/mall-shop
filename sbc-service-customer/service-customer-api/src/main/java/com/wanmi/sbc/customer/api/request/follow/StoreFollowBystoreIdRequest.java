package com.wanmi.sbc.customer.api.request.follow;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/* *
 * @Description:  店铺
 * @Author: Bob
 * @Date: 2019-04-02 18:30
*/
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreFollowBystoreIdRequest implements Serializable {


    private static final long serialVersionUID = -5404566370728068985L;

    /**
     * 店铺ID
     */
    @ApiModelProperty(value = "店铺ID")
    @NotNull
    private Long storeId;

}
