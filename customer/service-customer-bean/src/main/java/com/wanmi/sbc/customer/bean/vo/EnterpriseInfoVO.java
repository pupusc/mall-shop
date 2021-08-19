package com.wanmi.sbc.customer.bean.vo;

import lombok.Data;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>企业信息表VO</p>
 * @author TangLian
 * @date 2020-03-03 14:11:45
 */
@ApiModel
@Data
public class EnterpriseInfoVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 企业Id
	 */
	@ApiModelProperty(value = "企业Id")
	private String enterpriseId;

	/**
	 * 企业名称
	 */
	@ApiModelProperty(value = "企业名称")
	private String enterpriseName;

	/**
	 * 统一社会信用代码
	 */
	@ApiModelProperty(value = "统一社会信用代码")
	private String socialCreditCode;

	/**
	 * 企业性质
	 */
	@ApiModelProperty(value = "企业性质")
	private Integer businessNatureType;

	/**
	 * 企业行业
	 */
	@ApiModelProperty(value = "企业行业")
	private Integer businessIndustryType;

	/**
	 * 企业人数 0：1-49，1：50-99，2：100-499，3：500-999，4：1000以上
	 */
	@ApiModelProperty(value = "企业人数 0：1-49，1：50-99，2：100-499，3：500-999，4：1000以上")
	private Integer businessEmployeeNum;

	/**
	 * 营业执照地址
	 */
	@ApiModelProperty(value = "营业执照地址")
	private String businessLicenseUrl;

	/**
	 * 企业会员id
	 */
	@ApiModelProperty(value = "企业会员id")
	private String customerId;

}