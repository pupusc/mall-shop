package com.wanmi.sbc.message.api.request.pushcustomerenable;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import java.time.LocalDateTime;

/**
 * <p>会员推送通知开关修改参数</p>
 * @author Bob
 * @date 2020-01-07 15:31:47
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PushCustomerEnableModifyRequest extends BaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 会员ID
	 */
	@ApiModelProperty(value = "会员ID")
	@Length(max=32)
	private String customerId;

	/**
	 * 开启状态 0:未开启 1:启用
	 */
	@ApiModelProperty(value = "开启状态 0:未开启 1:启用")
	@Max(127)
	private Integer enableStatus;

	/**
	 * 账号安全通知 0:未启用 1:启用
	 */
	@ApiModelProperty(value = "账号安全通知 0:未启用 1:启用")
	@Max(127)
	private Integer accountSecurity;

	/**
	 * 账户资产通知 0:未启用 1:启用
	 */
	@ApiModelProperty(value = "账户资产通知 0:未启用 1:启用")
	@Max(127)
	private Integer accountAssets;

	/**
	 * 订单进度通知 0:未启用 1:启用
	 */
	@ApiModelProperty(value = "订单进度通知 0:未启用 1:启用")
	@Max(127)
	private Integer orderProgressRate;

	/**
	 * 退单进度通知 0:未启用 1:启用
	 */
	@ApiModelProperty(value = "退单进度通知 0:未启用 1:启用")
	@Max(127)
	private Integer returnOrderProgressRate;

	/**
	 * 分销业务通知 0:未启用 1:启用
	 */
	@ApiModelProperty(value = "分销业务通知 0:未启用 1:启用")
	@Max(127)
	private Integer distribution;

	/**
	 * 删除标志 0:未删除 1:删除
	 */
	@ApiModelProperty(value = "删除标志 0:未删除 1:删除", hidden = true)
	private DeleteFlag delFlag;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间", hidden = true)
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTime;

	/**
	 * 更新人ID
	 */
	@ApiModelProperty(value = "更新人ID", hidden = true)
	private String updatePerson;

}