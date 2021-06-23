package com.wanmi.sbc.vas.api.request.iepsetting;

import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.base.BaseRequest;
import lombok.*;
import javax.validation.constraints.*;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>企业购设置新增参数</p>
 * @author 宋汉林
 * @date 2020-03-02 20:15:04
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IepSettingAddRequest extends BaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 *  企业会员名称 
	 */
	@ApiModelProperty(value = " 企业会员名称 ")
	@NotBlank
	@Length(max=8)
	private String enterpriseCustomerName;

	/**
	 *  企业价名称 
	 */
	@ApiModelProperty(value = " 企业价名称 ")
	@NotBlank
	@Length(max=10)
	private String enterprisePriceName;

	/**
	 *  企业会员logo 
	 */
	@ApiModelProperty(value = " 企业会员logo ")
	@Length(max=100)
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
	 *  创建人 
	 */
	@ApiModelProperty(value = " 创建人 ", hidden = true)
	private String createPerson;

	/**
	 *  修改人 
	 */
	@ApiModelProperty(value = " 修改人 ", hidden = true)
	private String updatePerson;

	/**
	 *  是否删除标志 0：否，1：是 
	 */
	@ApiModelProperty(value = " 是否删除标志 0：否，1：是 ", hidden = true)
	private DeleteFlag delFlag;

}