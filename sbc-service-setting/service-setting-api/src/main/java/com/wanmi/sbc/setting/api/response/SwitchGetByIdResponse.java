package com.wanmi.sbc.setting.api.response;

import com.wanmi.sbc.common.enums.DeleteFlag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 系统开关
 * Created by yuanlinling on 2017/4/26.
 */
@ApiModel
@Data
public class SwitchGetByIdResponse implements Serializable {

    private static final long serialVersionUID = 45616212105080259L;
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private String id;

    /**
     * 开关名称
     */
    @ApiModelProperty(value = "开关名称")
    private String switchName;

    /**
     *开关状态 0：关闭 1：开启
     */
    @ApiModelProperty(value = "开关状态-0：关闭 1：开启", dataType = "com.wanmi.sbc.common.enums.DefaultFlag")
    private Integer status;

    /**
     * 删除标志
     */
    @ApiModelProperty(value = "删除标志", dataType = "com.wanmi.sbc.common.enums.DeleteFlag")
    @NotNull
    private DeleteFlag delFlag;

}
