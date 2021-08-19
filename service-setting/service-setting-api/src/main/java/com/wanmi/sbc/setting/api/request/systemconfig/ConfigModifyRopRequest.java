package com.wanmi.sbc.setting.api.request.systemconfig;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * Created by feitingting on 2019/11/6.
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfigModifyRopRequest {
    /**
     * 编号
     */
    @ApiModelProperty(value="编号")
    @NotNull
    private Long id;

    /**
     * 类型
     */
    @ApiModelProperty(value="类型")
    private String configType;

    /**
     * 状态
     */
    @ApiModelProperty(value="状态")
    private Integer status;

}
