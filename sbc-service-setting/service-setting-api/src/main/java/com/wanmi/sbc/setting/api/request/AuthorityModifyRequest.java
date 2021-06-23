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

/**
 * 系统权限信息
 * Created by bail on 2017-12-28
 */
@ApiModel
@Data
public class AuthorityModifyRequest extends SettingBaseRequest {

    private static final long serialVersionUID = 7282675755148935521L;
    /**
     * 权限标识
     */
    @ApiModelProperty(value = "权限标识")
    @NotNull
    private String authorityId;

    /**
     * 系统类别(如:s2b平台,s2b商家)
     */
    @ApiModelProperty(value = "系统类别")
    @NotNull
    private Platform systemTypeCd;

    /**
     * 功能id
     */
    @ApiModelProperty(value = "功能id")
    private String functionId;

    /**
     * 权限显示名
     */
    @ApiModelProperty(value = "权限显示名")
    private String authorityTitle;

    /**
     * 权限名称
     */
    @ApiModelProperty(value = "权限名称")
    private String authorityName;

    /**
     * 权限路径
     */
    @ApiModelProperty(value = "权限路径")
    private String authorityUrl;

    /**
     * 权限请求类别(GET,POST,PUT,DELETE)
     */
    @ApiModelProperty(value = "权限请求类别")
    private String requestType;

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
    private DeleteFlag delFlag;

}
