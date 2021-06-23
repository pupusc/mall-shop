package com.wanmi.sbc.setting.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.setting.bean.enums.AddrLevel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>平台地址信息VO</p>
 * @author dyt
 * @date 2020-03-30 14:39:57
 */
@ApiModel
@Data
public class PlatformAddressVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@ApiModelProperty(value = "主键id")
	private String id;

	/**
	 * 地址id
	 */
	@ApiModelProperty(value = "地址id")
	private String addrId;

	/**
	 * 地址名称
	 */
	@ApiModelProperty(value = "地址名称")
	private String addrName;

	/**
	 * 父地址ID
	 */
	@ApiModelProperty(value = "父地址ID")
	private String addrParentId;

	/**
	 * 地址层级(0-省级;1-市级;2-区县级;3-乡镇或街道级)
	 */
	@ApiModelProperty(value = "地址层级(0-省级;1-市级;2-区县级;3-乡镇或街道级)")
	private AddrLevel addrLevel;

    /**
     * 排序号
     */
    @ApiModelProperty(value = "排序号")
    private Integer sortNo;

	/**
	 * 数据类型 0:初始化 1:人工
	 */
	@ApiModelProperty(value = "数据类型 0:初始化 1:人工")
	private Integer dataType;

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
	 * 删除时间
	 */
	@ApiModelProperty(value = "删除时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime deleteTime;

    /**
     * 是否叶子节点 true:是 false:否
     */
    @ApiModelProperty(value = "是否叶子节点 true:是 false:否")
	private Boolean leafFlag;

}