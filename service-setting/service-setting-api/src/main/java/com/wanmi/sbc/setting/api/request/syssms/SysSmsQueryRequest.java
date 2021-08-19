package com.wanmi.sbc.setting.api.request.syssms;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import java.time.LocalDateTime;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import lombok.*;
import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>系统短信配置通用查询请求参数</p>
 * @author lq
 * @date 2019-11-05 16:13:47
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysSmsQueryRequest extends BaseQueryRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量查询-主键List
	 */
	@ApiModelProperty(value = "批量查询-主键List")
	private List<String> smsIdList;

	/**
	 * 主键
	 */
	@ApiModelProperty(value = "主键")
	private String smsId;

	/**
	 * 接口地址
	 */
	@ApiModelProperty(value = "接口地址")
	private String smsUrl;

	/**
	 * 名称
	 */
	@ApiModelProperty(value = "名称")
	private String smsName;

	/**
	 * SMTP密码
	 */
	@ApiModelProperty(value = "SMTP密码")
	private String smsPass;

	/**
	 * 网关
	 */
	@ApiModelProperty(value = "网关")
	private String smsGateway;

	/**
	 * 是否开启(0未开启 1已开启)
	 */
	@ApiModelProperty(value = "是否开启(0未开启 1已开启)")
	private Integer isOpen;

	/**
	 * 搜索条件:createTime开始
	 */
	@ApiModelProperty(value = "搜索条件:createTime开始")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTimeBegin;
	/**
	 * 搜索条件:createTime截止
	 */
	@ApiModelProperty(value = "搜索条件:createTime截止")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTimeEnd;

	/**
	 * 搜索条件:modifyTime开始
	 */
	@ApiModelProperty(value = "搜索条件:modifyTime开始")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime modifyTimeBegin;
	/**
	 * 搜索条件:modifyTime截止
	 */
	@ApiModelProperty(value = "搜索条件:modifyTime截止")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime modifyTimeEnd;

	/**
	 * smsAddress
	 */
	@ApiModelProperty(value = "smsAddress")
	private String smsAddress;

	/**
	 * smsProvider
	 */
	@ApiModelProperty(value = "smsProvider")
	private String smsProvider;

	/**
	 * smsContent
	 */
	@ApiModelProperty(value = "smsContent")
	private String smsContent;

}