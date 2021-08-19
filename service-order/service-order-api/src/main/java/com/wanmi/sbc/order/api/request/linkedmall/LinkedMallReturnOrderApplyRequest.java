package com.wanmi.sbc.order.api.request.linkedmall;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * linkedMall申请请求结构
 * Created by dyt on 6/5/2017.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class LinkedMallReturnOrderApplyRequest implements Serializable {

    private static final long serialVersionUID = 4079600894275807085L;

    /**
     * 退单id
     */
    @ApiModelProperty(value = "退单id")
    @NotBlank
    private String rid;

    /**
     * 原因id
     */
    @ApiModelProperty(value = "原因id")
    @NotNull
    private Long reasonTextId;

    /**
     * 原因内容
     */
    @ApiModelProperty(value = "原因内容")
    @NotBlank
    private String reasonTips;

    /**
     * 买家留言
     */
    @ApiModelProperty(value = "留言")
    private String leaveMessage;

    /**
     * 凭证
     */
    @ApiModelProperty(value = "凭证")
    private List<String> images;
}
