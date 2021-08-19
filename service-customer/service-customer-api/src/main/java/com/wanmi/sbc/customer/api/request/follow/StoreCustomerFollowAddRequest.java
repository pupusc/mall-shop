package com.wanmi.sbc.customer.api.request.follow;

import com.wanmi.sbc.common.enums.TerminalSource;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 店铺收藏的新增条件
 * Created by daiyitian on 2017/11/6.
 */
@ApiModel
@Data
public class StoreCustomerFollowAddRequest implements Serializable {

    private static final long serialVersionUID = -4710836069085092691L;
    /**
     * 客户ID
     */
    @ApiModelProperty(value = "客户ID")
    @NotBlank
    private String customerId;

    /**
     * 店铺ID
     */
    @ApiModelProperty(value = "店铺ID")
    @NotNull
    private Long storeId;


    /**
     * 终端
     */
    @ApiModelProperty(value = "终端")
    private TerminalSource terminalSource;

}
