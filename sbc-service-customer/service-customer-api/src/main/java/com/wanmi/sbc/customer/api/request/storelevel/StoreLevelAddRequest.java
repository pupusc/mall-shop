package com.wanmi.sbc.customer.api.request.storelevel;

import java.math.BigDecimal;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import java.time.LocalDateTime;
import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.*;

/**
 * <p>商户客户等级表新增参数</p>
 * @author yang
 * @date 2019-02-27 19:51:30
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StoreLevelAddRequest extends CustomerBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 店铺编号
	 */
	private Long storeId;

	/**
	 * 等级名称
	 */
	@NotBlank
	@Length(max=32)
	private String levelName;

	/**
	 * 折扣率
	 */
	@NotNull
	private BigDecimal discountRate;

	/**
	 * 客户升级所需累积支付金额
	 */
	private BigDecimal amountConditions;

	/**
	 * 客户升级所需累积支付订单笔数
	 */
	private Integer orderConditions;

	/**
	 * 创建时间
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTime;

	/**
	 * 创建人
	 */
	private String createPerson;

}