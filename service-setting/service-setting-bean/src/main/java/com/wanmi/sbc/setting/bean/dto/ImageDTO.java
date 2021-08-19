package com.wanmi.sbc.setting.bean.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.enums.DeleteFlag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 图片实体类
 * Created by dyt on 2017/4/11.
 */

@ApiModel
@Data
public class ImageDTO implements Serializable {

    private static final long serialVersionUID = 7387692215630856237L;
    /**
     * 图片编号
     */
    @ApiModelProperty(value = "图片编号")
    private Long imageId;

    /**
     * 商品编号
     */
    @ApiModelProperty(value = "商品编号")
    private Long cateId;

    /**
     * 图片名称
     */
    @ApiModelProperty(value = "图片名称")
    private String imageName;

    /**
     * 图片KEY
     */
    @ApiModelProperty(value = "图片KEY")
    private String imageKey;

    /**
     * 原图路径
     */
    @ApiModelProperty(value = "原图路径")
    private String artworkUrl;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;

    /**
     * 删除标记
     */
    @ApiModelProperty(value = "删除标记", dataType = "com.wanmi.sbc.common.enums.DeleteFlag")
    @NotNull
    private DeleteFlag delFlag;

    /**
     * 图片服务器类型
     * 对应system_config的config_type
     * 参照ConfigType枚举
     */
    @ApiModelProperty(value = "图片服务器类型-对应system_config的config_type")
    private String serverType;

}
