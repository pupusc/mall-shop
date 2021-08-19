package com.wanmi.sbc.customer.api.request.quicklogin;

import com.wanmi.sbc.customer.bean.enums.ThirdLoginType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 第三方关系表删除
 *
 * @Author: songhanlin
 * @Date: Created In 10:01 AM 2018/8/8
 * @Description: 第三方关系表
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ThirdLoginRelationDeleteByCustomerRequest implements Serializable {

    private static final long serialVersionUID = 93541450644417749L;

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

    /**
     * 商户id-boss端取默认值
     */
    @ApiModelProperty(value = "商户id-boss端取默认值")
    @NotNull
    private Long storeId;

}
