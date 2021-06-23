package com.wanmi.sbc.setting.api.response.yunservice;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>根据id查询云配置信息response</p>
 * @author yang
 * @date 2019-11-05 18:33:04
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class YunConfigResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *  编号
     */
    @ApiModelProperty(value = " 编号")
    private Long id;

    /**
     * 键
     */
    @ApiModelProperty(value = "键")
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
     * 状态,0:未启用1:已启用
     */
    @ApiModelProperty(value = "状态,0:未启用1:已启用")
    private Integer status;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;

    /**
     * 删除标识,0:未删除1:已删除
     */
    @ApiModelProperty(value = "删除标识,0:未删除1:已删除")
    private DeleteFlag delFlag;

    /**
     * 请求路径
     */
    @ApiModelProperty(value = "请求路径")
    private String endPoint;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String accessKeyId;

    /**
     * 密钥
     */
    @ApiModelProperty(value = "密钥")
    private String accessKeySecret;

    /**
     * 存储空间名
     */
    @ApiModelProperty(value = "存储空间名")
    private String bucketName;

    /**
     * 地区
     */
    @ApiModelProperty(value = "地区")
    private String region;
}
