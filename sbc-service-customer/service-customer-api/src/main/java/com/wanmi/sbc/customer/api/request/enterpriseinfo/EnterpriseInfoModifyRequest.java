package com.wanmi.sbc.customer.api.request.enterpriseinfo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
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
 * <p>企业信息表修改参数</p>
 * @author TangLian
 * @date 2020-03-03 14:11:45
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnterpriseInfoModifyRequest extends BaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 企业Id
	 */
	@ApiModelProperty(value = "企业Id")
	@Length(max=32)
	private String enterpriseId;

	/**
	 * 企业名称
	 */
	@ApiModelProperty(value = "企业名称")
	@NotBlank
	@Length(max=100)
	private String enterpriseName;

	/**
	 * 统一社会信用代码
	 */
	@ApiModelProperty(value = "统一社会信用代码")
	@NotBlank
	@Length(max=50)
	private String socialCreditCode;

	/**
	 * 企业性质
	 */
	@ApiModelProperty(value = "企业性质")
	@NotNull
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
	@Length(max=1024)
	private String businessLicenseUrl;

	/**
	 * 企业会员id
	 */
	@ApiModelProperty(value = "企业会员id")
	@NotBlank
	@Length(max=32)
	private String customerId;

	/**
	 * 参加时间
	 */
	@ApiModelProperty(value = "参加时间", hidden = true)
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTime;

	/**
	 * 更新人
	 */
	@ApiModelProperty(value = "更新人", hidden = true)
	private String updatePerson;

}