package com.wanmi.sbc.message.bean.vo;

import com.wanmi.sbc.common.enums.DeleteFlag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>会员推送通知开关VO</p>
 * @author Bob
 * @date 2020-01-07 15:31:47
 */
@ApiModel
@Data
public class PushCustomerEnableVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 会员ID
	 */
	@ApiModelProperty(value = "会员ID")
	private String customerId;

	/**
	 * 开启状态 0:未开启 1:启用
	 */
	@ApiModelProperty(value = "开启状态 0:未开启 1:启用")
	private Integer enableStatus;

	/**
	 * 账号安全通知 0:未启用 1:启用
	 */
	@ApiModelProperty(value = "账号安全通知 0:未启用 1:启用")
	private Integer accountSecurity;

	/**
	 * 账户资产通知 0:未启用 1:启用
	 */
	@ApiModelProperty(value = "账户资产通知 0:未启用 1:启用")
	private Integer accountAssets;

	/**
	 * 订单进度通知 0:未启用 1:启用
	 */
	@ApiModelProperty(value = "订单进度通知 0:未启用 1:启用")
	private Integer orderProgressRate;

	/**
	 * 退单进度通知 0:未启用 1:启用
	 */
	@ApiModelProperty(value = "退单进度通知 0:未启用 1:启用")
	private Integer returnOrderProgressRate;

	/**
	 * 分销业务通知 0:未启用 1:启用
	 */
	@ApiModelProperty(value = "分销业务通知 0:未启用 1:启用")
	private Integer distribution;

	/**
	 * 删除标志 0:未删除 1:删除
	 */
	@ApiModelProperty(value = "删除标志 0:未删除 1:删除", hidden = true)
	private DeleteFlag delFlag;

}