package com.wanmi.sbc.customer.api.request.quicklogin;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import com.wanmi.sbc.customer.bean.enums.ThirdLoginType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;


/**
 * <p>根据用户Id&第三方登录方式查询第三方登录关系request</p>
 * Created by daiyitian on 2018-08-13-下午3:47.
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ThirdLoginRelationByCustomerRequest extends CustomerBaseRequest {

    private static final long serialVersionUID = -1224496566453107979L;

    /**
     * 用户Id
     */
    @ApiModelProperty(value = "用户Id")
    @NotNull
    private String customerId;

    /**
     * 第三方类型 0:wechat
     */
    @ApiModelProperty(value = "第三方类型")
    @NotNull
    private ThirdLoginType thirdLoginType;

    @ApiModelProperty(value = "是否被删除")
    private DeleteFlag delFlag;

    @ApiModelProperty(value = "店铺Id")
    private Long storeId;
}
