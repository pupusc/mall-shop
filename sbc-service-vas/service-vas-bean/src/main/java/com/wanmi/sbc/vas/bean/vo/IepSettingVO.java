package com.wanmi.sbc.vas.bean.vo;

import com.wanmi.sbc.common.enums.DefaultFlag;
import lombok.Data;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>企业购设置VO</p>
 * @author 宋汉林
 * @date 2020-03-02 20:15:04
 */
@ApiModel
@Data
public class IepSettingVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *  id 
	 */
	@ApiModelProperty(value = " id ")
	private String id;

	/**
	 *  企业会员名称 
	 */
	@ApiModelProperty(value = " 企业会员名称 ")
	private String enterpriseCustomerName;

	/**
	 *  企业价名称 
	 */
	@ApiModelProperty(value = " 企业价名称 ")
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
	private DefaultFlag enterpriseCustomerAuditFlag;

	/**
	 *  企业商品审核 0: 不需要审核 1: 需要审核 
	 */
	@ApiModelProperty(value = " 企业商品审核 0: 不需要审核 1: 需要审核 ")
	private DefaultFlag enterpriseGoodsAuditFlag;

	/**
	 *  企业会员注册协议 
	 */
	@ApiModelProperty(value = " 企业会员注册协议 ")
	private String enterpriseCustomerRegisterContent;

}