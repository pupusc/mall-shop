package com.wanmi.sbc.customer.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import java.time.LocalDateTime;
import lombok.Data;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>樊登付费类型 映射商城付费类型VO</p>
 * @author tzx
 * @date 2021-01-29 14:13:37
 */
@ApiModel
@Data
public class FdPaidCastVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 樊登付费类型 映射商城付费类型主键
	 */
	@ApiModelProperty(value = "樊登付费类型 映射商城付费类型主键")
	private Long id;

	/**
	 * 樊登付费会员类型
	 */
	@ApiModelProperty(value = "樊登付费会员类型")
	private Integer fdPayType;

	/**
	 * 商城付费会员类型id
	 */
	@ApiModelProperty(value = "商城付费会员类型id")
	private String paidCardId;

	/**
	 * 删除时间
	 */
	@ApiModelProperty(value = "删除时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime deleteTime;

}