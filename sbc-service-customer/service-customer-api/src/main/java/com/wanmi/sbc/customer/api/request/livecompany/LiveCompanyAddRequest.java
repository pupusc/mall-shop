package com.wanmi.sbc.customer.api.request.livecompany;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import java.time.LocalDateTime;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.base.BaseRequest;
import lombok.*;
import javax.validation.constraints.*;
import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>直播商家新增参数</p>
 * @author zwb
 * @date 2020-06-06 18:06:59
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LiveCompanyAddRequest extends BaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 提交审核时间
	 */
	@ApiModelProperty(value = "提交审核时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime submitTime;

	/**
	 * 直播状态 0未开通，1待审核，2已开通，3审核未通过，4禁用中
	 */
	@ApiModelProperty(value = "直播状态 0未开通，1待审核，2已开通，3审核未通过，4禁用中")
	@Max(127)
	private Integer liveBroadcastStatus;

	/**
	 * 直播审核原因
	 */
	@ApiModelProperty(value = "直播审核原因")
	@Length(max=255)
	private String auditReason;

	/**
	 * 创建人
	 */
	@ApiModelProperty(value = "创建人")
	@Length(max=45)
	private String createPerson;

	/**
	 * 更新人
	 */
	@ApiModelProperty(value = "更新人", hidden = true)
	private String updatePerson;

	/**
	 * 删除人
	 */
	@ApiModelProperty(value = "删除人")
	@Length(max=255)
	private String deletePerson;

	/**
	 * 删除时间
	 */
	@ApiModelProperty(value = "删除时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime deleteTime;

	/**
	 * 删除标识,0:未删除1:已删除
	 */
	@ApiModelProperty(value = "删除标识,0:未删除1:已删除", hidden = true)
	private DeleteFlag delFlag;

	/**
	 * 公司信息ID
	 */
	@ApiModelProperty(value = "公司信息ID")
	@Max(9223372036854775807L)
	private Long companyInfoId;

	/**
	 * 店铺id
	 */
	@ApiModelProperty(value = "店铺id")
	@Max(9223372036854775807L)
	private Long storeId;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTime;

}