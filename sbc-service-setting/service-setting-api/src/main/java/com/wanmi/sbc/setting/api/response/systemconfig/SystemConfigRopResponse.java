package com.wanmi.sbc.setting.api.response.systemconfig;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by feitingting on 2019/11/6.
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemConfigRopResponse {

    /**
     * 编号
     */
    @ApiModelProperty(value = "编号")
    private Long id;

    /**
     * 配置键
     */
    @ApiModelProperty(value = "配置键")
    private String configKey;

    /**
     * 类型
     */
    @ApiModelProperty(value = "类型")
    private String configType;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String configName;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 状态
     */
    @ApiModelProperty(value = "状态")
    private Integer status;

    /**
     * 内容
     */
    @ApiModelProperty(value = "内容")
    private String context;

}
