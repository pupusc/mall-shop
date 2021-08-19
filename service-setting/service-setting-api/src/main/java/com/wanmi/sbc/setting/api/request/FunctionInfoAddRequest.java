package com.wanmi.sbc.setting.api.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.Platform;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
@ApiModel
@Data
public class FunctionInfoAddRequest extends SettingBaseRequest {
    private static final long serialVersionUID = 6051735921149389720L;
    /**
     * 系统类别(如:s2b平台,s2b商家)
     */
    @ApiModelProperty(value = "系统类别")
    @NotNull
    private Platform systemTypeCd;

    /**
     * 菜单id
     */
    @ApiModelProperty(value = "菜单id")
    private String menuId;

    /**
     * 功能显示名
     */
    @ApiModelProperty(value = "功能显示名")
    private String functionTitle;

    /**
     * 功能名称
     */
    @ApiModelProperty(value = "功能名称")
    private String functionName;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 排序号
     */
    @ApiModelProperty(value = "排序号")
    private Integer sort;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    /**
     * 删除标识,0:未删除1:已删除
     */
    @ApiModelProperty(value = "删除标识", dataType = "com.wanmi.sbc.common.enums.DeleteFlag")
    @NotNull
    private DeleteFlag delFlag;
}
