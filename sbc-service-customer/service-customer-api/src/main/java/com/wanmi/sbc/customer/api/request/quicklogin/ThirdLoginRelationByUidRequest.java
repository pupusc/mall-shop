package com.wanmi.sbc.customer.api.request.quicklogin;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.customer.bean.enums.ThirdLoginType;
import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;


/**
 * <p>根据关联Id&第三方登录方式查询第三方登录关系request</p>
 * Created by daiyitian on 2018-08-13-下午3:47.
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ThirdLoginRelationByUidRequest extends CustomerBaseRequest {

    private static final long serialVersionUID = -1224496566453107979L;

    /**
     * 第三方登录主键
     */
    @ApiModelProperty(value = "第三方登录主键")
    @NotNull
    private String thirdLoginUid;

    /**
     * 第三方类型 0:wechat
     */
    @ApiModelProperty(value = "第三方类型")
    @NotNull
    private ThirdLoginType thirdLoginType;

    /**
     * 删除标记
     */
    @ApiModelProperty(value = "第三方类型")
    @NotNull
    private DeleteFlag delFlag;

    /**
     * 商户id-boss端取默认值
     */
    @ApiModelProperty(value = "商户id-boss端取默认值")
    @NotNull
    private Long storeId;

}
