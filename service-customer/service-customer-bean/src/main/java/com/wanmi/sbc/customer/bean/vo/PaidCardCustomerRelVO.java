package com.wanmi.sbc.customer.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.wanmi.sbc.common.enums.DeleteFlag;
import lombok.Data;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>付费会员VO</p>
 * @author xuhai
 * @date 2021-01-29 14:03:59
 */
@ApiModel
@Data
public class PaidCardCustomerRelVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	private String id;

	/**
	 * 会员id
	 */
	private String customerId;

	/**
	 * 付费会员类型ID
	 */
	private String paidCardId;

	/**
	 * 开始时间
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime beginTime;

	/**
	 * 结束时间
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime endTime;

	/**
	 * 状态： 0：未删除 1：删除
	 */
	private DeleteFlag delFlag;

	/**
	 * 卡号
	 */
	private String cardNo;


	/**
	 * 付费会员卡信息
	 */
	private PaidCardVO paidCardVO;

	/**
	 * 是否发送了即将过期提醒短信
	 */
	private Boolean sendMsgFlag;


	/**
	 * 是否发送了已经过期提醒短信
	 */
	private Boolean sendExpireMsgFlag;


	private String phone;

	private String paidCardName;

	/**
	 * 状态： 0：樊登同步的会员 1：商城的会员
	 */
	private Integer paidSource;

	/**
	 * 最大的临时id
	 */
	private Integer maxTmpId;
}