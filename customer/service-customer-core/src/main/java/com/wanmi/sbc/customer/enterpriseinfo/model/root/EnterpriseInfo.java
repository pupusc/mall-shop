package com.wanmi.sbc.customer.enterpriseinfo.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DeleteFlag;

import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.Data;
import javax.persistence.*;
import com.wanmi.sbc.common.base.BaseEntity;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import java.time.LocalDateTime;

/**
 * <p>企业信息表实体类</p>
 * @author TangLian
 * @date 2020-03-03 14:11:45
 */
@Data
@Entity
@Table(name = "enterprise_info")
public class EnterpriseInfo extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * 企业Id
	 */
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "enterprise_id")
	private String enterpriseId;

	/**
	 * 企业名称
	 */
	@Column(name = "enterprise_name")
	private String enterpriseName;

	/**
	 * 统一社会信用代码
	 */
	@Column(name = "social_credit_code")
	private String socialCreditCode;

	/**
	 * 企业性质
	 */
	@Column(name = "business_nature_type")
	private Integer businessNatureType;

	/**
	 * 企业行业
	 */
	@Column(name = "business_industry_type")
	private Integer businessIndustryType;

	/**
	 * 企业人数 0：1-49，1：50-99，2：100-499，3：500-999，4：1000以上
	 */
	@Column(name = "business_employee_num")
	private Integer businessEmployeeNum;

	/**
	 * 营业执照地址
	 */
	@Column(name = "business_license_url")
	private String businessLicenseUrl;

	/**
	 * 企业会员id
	 */
	@Column(name = "customer_id")
	private String customerId;

	/**
	 * 创建时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	@Column(name = "create_time")
	private LocalDateTime createTime;

	/**
	 * 创建人
	 */
	@Column(name = "create_person")
	private String createPerson;

	/**
	 * 修改时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	@Column(name = "update_time")
	private LocalDateTime updateTime;

	/**
	 * 修改人
	 */
	@Column(name = "update_person")
	private String updatePerson;

	/**
	 * 删除标志
	 */
	@Column(name = "del_flag")
	@Enumerated
	private DeleteFlag delFlag;

}