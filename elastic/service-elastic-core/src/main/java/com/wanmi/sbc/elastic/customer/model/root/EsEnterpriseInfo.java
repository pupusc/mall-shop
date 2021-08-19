package com.wanmi.sbc.elastic.customer.model.root;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

/**
 * <p>企业信息表VO</p>
 * @author TangLian
 * @date 2020-03-03 14:11:45
 */
@ApiModel
@Data
public class EsEnterpriseInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 企业名称
	 */
	@Field(type = FieldType.Keyword)
	private String enterpriseName;

	/**
	 * 统一社会信用代码
	 */
	@Field(type = FieldType.Keyword)
	private String socialCreditCode;

	/**
	 * 企业性质
	 */
	@Field(type = FieldType.Integer)
	private Integer businessNatureType;

	/**
	 * 企业行业
	 */
	@Field(type = FieldType.Integer)
	private Integer businessIndustryType;

	/**
	 * 企业人数 0：1-49，1：50-99，2：100-499，3：500-999，4：1000以上
	 */
	@Field(type = FieldType.Integer)
	private Integer businessEmployeeNum;

	/**
	 * 企业会员id
	 */
	@ApiModelProperty(value = "企业会员id")
	private String customerId;

}