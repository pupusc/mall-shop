package com.wanmi.sbc.message.api.request.pushsendnode;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @program: sbc-micro-service
 * @description: 节点推送参数
 * @create: 2020-01-14 10:47
 **/
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PushSendNodeRequest implements Serializable {
    private static final long serialVersionUID = -1913675866616752115L;

    @ApiModelProperty(value = "会员ID")
    @NotBlank
    private String customerId;

    @ApiModelProperty(value = "节点ID")
    @NotNull
    private Long nodeId;

    @ApiModelProperty(value = "推送标题")
    @NotBlank
    private String title;

    @ApiModelProperty(value = "推送内容")
    @NotBlank
    private String content;

    @ApiModelProperty(value = "推送路由地址")
    private String router;


}