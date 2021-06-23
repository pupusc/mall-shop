package com.wanmi.sbc.customer.api.request.enterpriseinfo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import java.time.LocalDateTime;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.base.BaseRequest;
import lombok.*;
import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>企业信息表列表查询请求参数</p>
 * @author TangLian
 * @date 2020-03-03 14:11:45
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnterpriseInfoListRequest extends BaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量查询-企业IdList
	 */
	@ApiModelProperty(value = "批量查询-企业IdList")
	private List<String> enterpriseIdList;

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

	/**
	 * 创建人
	 */
	@ApiModelProperty(value = "创建人")
	private String createPerson;

	/**
	 * 搜索条件:参加时间开始
	 */
	@ApiModelProperty(value = "搜索条件:参加时间开始")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTimeBegin;
	/**
	 * 搜索条件:参加时间截止
	 */
	@ApiModelProperty(value = "搜索条件:参加时间截止")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTimeEnd;

	/**
	 * 更新人
	 */
	@ApiModelProperty(value = "更新人")
	private String updatePerson;

	/**
	 * 搜索条件:更新时间开始
	 */
	@ApiModelProperty(value = "搜索条件:更新时间开始")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime updateTimeBegin;
	/**
	 * 搜索条件:更新时间截止
	 */
	@ApiModelProperty(value = "搜索条件:更新时间截止")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime updateTimeEnd;

	/**
	 * 删除标志
	 */
	@ApiModelProperty(value = "删除标志")
	private DeleteFlag delFlag;

}