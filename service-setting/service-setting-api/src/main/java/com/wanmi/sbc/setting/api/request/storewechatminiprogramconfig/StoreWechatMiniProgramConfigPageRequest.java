package com.wanmi.sbc.setting.api.request.storewechatminiprogramconfig;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>门店微信小程序配置分页查询请求参数</p>
 * @author tangLian
 * @date 2020-01-16 11:47:15
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreWechatMiniProgramConfigPageRequest extends BaseQueryRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量查询-主键List
	 */
	@ApiModelProperty(value = "批量查询-主键List")
	private List<String> idList;

	/**
	 * 主键
	 */
	@ApiModelProperty(value = "主键")
	private String id;

	/**
	 * 微信appId
	 */
	@ApiModelProperty(value = "微信appId")
	private String appId;

	/**
	 * 微信appSecret
	 */
	@ApiModelProperty(value = "微信appSecret")
	private String appSecret;

	/**
	 * 门店id
	 */
	@ApiModelProperty(value = "门店id")
	private Long storeId;

	/**
	 * 商家id
	 */
	@ApiModelProperty(value = "商家id")
	private Long companyInfoId;

	/**
	 * 搜索条件:创建时间开始
	 */
	@ApiModelProperty(value = "搜索条件:创建时间开始")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTimeBegin;
	/**
	 * 搜索条件:创建时间截止
	 */
	@ApiModelProperty(value = "搜索条件:创建时间截止")
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
	 * 删除标志
	 */
	@ApiModelProperty(value = "删除标志")
	private DeleteFlag delFlag;

}