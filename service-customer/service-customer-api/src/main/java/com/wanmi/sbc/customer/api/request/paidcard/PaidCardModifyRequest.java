package com.wanmi.sbc.customer.api.request.paidcard;

import java.math.BigDecimal;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import java.time.LocalDateTime;
import java.util.List;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import com.wanmi.sbc.customer.api.request.paidcardrightsrel.PaidCardRightsRelAddRequest;
import com.wanmi.sbc.customer.api.request.paidcardrightsrel.PaidCardRightsRelModifyRequest;
import com.wanmi.sbc.customer.api.request.paidcardrule.PaidCardRuleAddRequest;
import com.wanmi.sbc.customer.api.request.paidcardrule.PaidCardRuleModifyRequest;
import com.wanmi.sbc.customer.bean.enums.BgTypeEnum;
import com.wanmi.sbc.customer.bean.enums.EnableEnum;
import lombok.*;
import javax.validation.constraints.*;
import org.hibernate.validator.constraints.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;

/**
 * <p>付费会员修改参数</p>
 * @author xuhai
 * @date 2021-01-29 14:03:56
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaidCardModifyRequest extends CustomerBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@ApiModelProperty(value = "主键")
	@Length(max=32)
	@NotBlank
	private String id;

	/**
	 * 背景信息。背景颜色传十六进制类似 #ccc；背景图片传图片地址
	 */
	@ApiModelProperty(value = "背景信息。背景颜色传十六进制类似 #ccc；背景图片传图片地址")
	@NotBlank
	@Length(max=255)
	private String background;

	/**
	 * 付费会员图标
	 */
	@ApiModelProperty(value = "付费会员图标")
	@NotBlank
	@Length(max=255)
	private String icon;

	/**
	 * 折扣率
	 */
	@ApiModelProperty(value = "折扣率")
	@NotNull
	private BigDecimal discountRate;

	/**
	 * 规则说明
	 */
	@ApiModelProperty(value = "规则说明")
	private String rule;

	/**
	 * 付费会员用户协议
	 */
	@ApiModelProperty(value = "付费会员用户协议")
	private String agreement;

	/**
	 * 删除标识 1：删除；0：未删除
	 */
	@ApiModelProperty(value = "删除标识 1：删除；0：未删除")
	private DeleteFlag delFlag;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	@ApiModelProperty(value = "更新时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime updateTime;

	/**
	 * 启动禁用标识 1：启用；2：禁用
	 */
	@ApiModelProperty(value = "启动禁用标识 1：启用；2：禁用")
	@NotNull
	private EnableEnum enable;

	/**
	 * 创建人ID
	 */
	@ApiModelProperty(value = "创建人ID")
	@Length(max=32)
	private String createPerson;

	/**
	 * 修改人ID
	 */
	@ApiModelProperty(value = "修改人ID")
	@Length(max=32)
	private String updatePerson;

	/**
	 * 背景类型0背景色；1背景图片
	 */
	@ApiModelProperty(value = "背景类型0背景色；1背景图片")
	@NotNull
	private BgTypeEnum bgType;

	/**
	 * 前景色
	 */
	@ApiModelProperty(value = "前景色")
	@NotBlank
	@Length(max=10)
	private String textColor;

	/**
	 * erp spu 编码
	 */
	@ApiModelProperty(value = "erp_spu_code")
	@Length(max=25)
	private String erpSpuCode;

	/**
	 * 付费规则列表
	 */
	@ApiModelProperty(value = "付费规则")
	private List<PaidCardRuleModifyRequest> ruleList;

	/**
	 * 会员权益列表
	 */
	@ApiModelProperty(value = "前景色")
	private List<PaidCardRightsRelModifyRequest> rightsList;

}