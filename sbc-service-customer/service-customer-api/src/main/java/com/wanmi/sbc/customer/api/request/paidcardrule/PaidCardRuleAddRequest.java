package com.wanmi.sbc.customer.api.request.paidcardrule;

import java.math.BigDecimal;
import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import com.wanmi.sbc.customer.bean.enums.PaidCardRuleTypeEnum;
import com.wanmi.sbc.customer.bean.enums.StatusEnum;
import com.wanmi.sbc.customer.bean.enums.TimeUnitEnum;
import lombok.*;
import javax.validation.constraints.*;
import org.hibernate.validator.constraints.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;

/**
 * <p>付费会员新增参数</p>
 * @author xuhai
 * @date 2021-01-29 14:04:01
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaidCardRuleAddRequest extends CustomerBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 配置类型 0：付费配置；1：续费配置
	 */
	@ApiModelProperty(value = "配置类型 0：付费配置；1：续费配置")
	@NotNull
	private PaidCardRuleTypeEnum type;

	/**
	 * 名称
	 */
	@ApiModelProperty(value = "名称")
	@NotBlank
	@Length(max=20)
	private String name;

	/**
	 * 价格
	 */
	@ApiModelProperty(value = "价格")
	@NotNull
	private BigDecimal price;

	/**
	 * 0:禁用；1：启用
	 */
	@ApiModelProperty(value = "0:禁用；1：启用")
	private StatusEnum status;

	/**
	 * 付费会员类型id
	 */
	@ApiModelProperty(value = "付费会员类型id")
	@Length(max=32)
	private String paidCardId;

	/**
	 * 时间单位：0天，1月，2年
	 */
	@ApiModelProperty(value = "时间单位：0天，1月，2年")
	@NotNull
	private TimeUnitEnum timeUnit;

	/**
	 * 时间（数值）
	 */
	@ApiModelProperty(value = "时间（数值）")
	@NotNull
	@Max(127)
	private Integer timeVal;

	/**
	 * erp sku 编码
	 */
	@ApiModelProperty(value = "erp_sku_code")
	@NotBlank
	@Length(max=25)
	private String erpSkuCode;

}