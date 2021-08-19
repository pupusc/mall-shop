package com.wanmi.sbc.vas.api.request.iepsetting;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import java.time.LocalDateTime;
import com.wanmi.sbc.common.base.BaseRequest;
import lombok.*;
import javax.validation.constraints.*;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>企业购设置修改参数</p>
 * @author 宋汉林
 * @date 2020-03-02 20:15:04
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IepSettingModifyRequest extends BaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 *  id 
	 */
	@ApiModelProperty(value = " id ")
	@Length(max=32)
	private String id;

	/**
	 *  企业会员名称 
	 */
	@ApiModelProperty(value = " 企业会员名称 ")
	@NotBlank
	@Pattern(regexp = "^[\\u4e00-\\u9fa5_a-zA-Z0-9_]{2,8}$")
	private String enterpriseCustomerName;

	/**
	 *  企业价名称 
	 */
	@ApiModelProperty(value = " 企业价名称 ")
	@NotBlank
	@Pattern(regexp = "^[\\u4e00-\\u9fa5_a-zA-Z0-9_]{2,10}$")
	private String enterprisePriceName;

	/**
	 *  企业会员logo 
	 */
	@ApiModelProperty(value = " 企业会员logo ")
	private String enterpriseCustomerLogo;

	/**
	 *  企业会员审核 0: 不需要审核 1: 需要审核 
	 */
	@ApiModelProperty(value = " 企业会员审核 0: 不需要审核 1: 需要审核 ")
	@NotNull
	private DefaultFlag enterpriseCustomerAuditFlag;

	/**
	 *  企业商品审核 0: 不需要审核 1: 需要审核 
	 */
	@ApiModelProperty(value = " 企业商品审核 0: 不需要审核 1: 需要审核 ")
	@NotNull
	private DefaultFlag enterpriseGoodsAuditFlag;

	/**
	 *  企业会员注册协议 
	 */
	@ApiModelProperty(value = " 企业会员注册协议 ")
	private String enterpriseCustomerRegisterContent;

	/**
	 *  创建时间 
	 */
	@ApiModelProperty(value = " 创建时间 ", hidden = true)
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTime;

	/**
	 *  修改人 
	 */
	@ApiModelProperty(value = " 修改人 ", hidden = true)
	private String updatePerson;

}