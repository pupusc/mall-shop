package com.wanmi.sbc.account.api.request.customerdrawcash;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.account.bean.enums.*;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>会员提现管理状态查询请求参数</p>
 * @author chenyufei
 * @date 2019-02-25 17:22:24
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDrawCashStatusQueryRequest extends BaseQueryRequest {

	private static final long serialVersionUID = 1L;

	/**
	 * 运营端审核状态(0:待审核,1:审核不通过,2:审核通过)
	 */
	@ApiModelProperty(value = "运营端审核状态(0:待审核,1:审核不通过,2:审核通过)")
	private AuditStatus auditStatus;

	/**
	 * 提现状态(0:未提现,1:提现失败,2:提现成功)
	 */
	@ApiModelProperty(value = "提现状态(0:未提现,1:提现失败,2:提现成功)")
	private DrawCashStatus drawCashStatus;

	/**
	 * 用户操作状态(0:已申请,1:已取消)
	 */
	@ApiModelProperty(value = "用户操作状态(0:已申请,1:已取消)")
	private CustomerOperateStatus customerOperateStatus;

	/**
	 * 提现单完成状态(0:未完成,1:已完成)
	 */
	@ApiModelProperty(value = "提现单完成状态(0:未完成,1:已完成)")
	private FinishStatus finishStatus;

	/**
	 * 删除标志(0:未删除,1:已删除)
	 */
	@ApiModelProperty(value = "删除标志(0:未删除,1:已删除)")
	private DeleteFlag delFlag;

}