package com.wanmi.sbc.setting.api.request.platformaddress;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.setting.bean.enums.AddrLevel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>平台地址信息列表查询请求参数</p>
 * @author dyt
 * @date 2020-03-30 14:39:57
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlatformAddressListRequest extends BaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量查询-主键idList
	 */
	@ApiModelProperty(value = "批量查询-主键idList")
	private List<String> idList;

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
     * 批量查询-地址id
     */
    @ApiModelProperty(value = "批量查询-地址id")
    private List<String> addrIdList;

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
     * 批量查询-父地址ID
     */
    @ApiModelProperty(value = "批量查询-父地址ID")
    private List<String> addrParentIdList;

	/**
	 * 地址层级(0-省级;1-市级;2-区县级;3-乡镇或街道级)
	 */
	@ApiModelProperty(value = "地址层级(0-省级;1-市级;2-区县级;3-乡镇或街道级)")
	private AddrLevel addrLevel;

    /**
     * 批量查询-地址层级(0-省级;1-市级;2-区县级;3-乡镇或街道级)
     */
    @ApiModelProperty(value = "批量查询-地址层级(0-省级;1-市级;2-区县级;3-乡镇或街道级)")
    private List<AddrLevel> addrLevels;

	/**
	 * 搜索条件:入库时间开始
	 */
	@ApiModelProperty(value = "搜索条件:入库时间开始")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTimeBegin;
	/**
	 * 搜索条件:入库时间截止
	 */
	@ApiModelProperty(value = "搜索条件:入库时间截止")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTimeEnd;

	/**
	 * 搜索条件:更新时间开始
	 */
	@ApiModelProperty(value = "搜索条件:更新时间开始")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime updateTimeBegin;
	/**
	 * 搜索条件:更新时间截止
	 */
	@ApiModelProperty(value = "搜索条件:更新时间截止")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime updateTimeEnd;

	/**
	 * 是否删除标志 0：否，1：是；默认0
	 */
	@ApiModelProperty(value = "是否删除标志 0：否，1：是；默认0")
	private DeleteFlag delFlag;

	/**
	 * 搜索条件:删除时间开始
	 */
	@ApiModelProperty(value = "搜索条件:删除时间开始")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime deleteTimeBegin;
	/**
	 * 搜索条件:删除时间截止
	 */
	@ApiModelProperty(value = "搜索条件:删除时间截止")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime deleteTimeEnd;

    /**
     * 填加是否叶子节点验证
     */
    @ApiModelProperty(value = "是否叶子节点验证")
	private Boolean leafFlag;

}