package com.wanmi.sbc.setting.companyinfo.model.root;

import com.wanmi.sbc.common.enums.DeleteFlag;
import lombok.Data;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>公司信息实体类</p>
 * @author lq
 * @date 2019-11-05 16:09:36
 */
@Data
@Entity
@Table(name = "company_info")
public class CompanyInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 公司信息ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "company_info_id")
	private Long companyInfoId;

	/**
	 * 公司名称
	 */
	@Column(name = "company_name")
	private String companyName;

	/**
	 * 法人身份证反面
	 */
	@Column(name = "back_ID_card")
	private String backIdcard;

	/**
	 * 省
	 */
	@Column(name = "province_id")
	private Long provinceId;

	/**
	 * 市
	 */
	@Column(name = "city_id")
	private Long cityId;

	/**
	 * 区
	 */
	@Column(name = "area_id")
	private Long areaId;

	/**
	 * 详细地址
	 */
	@Column(name = "detail_address")
	private String detailAddress;

	/**
	 * 联系人
	 */
	@Column(name = "contact_name")
	private String contactName;

	/**
	 * 联系方式
	 */
	@Column(name = "contact_phone")
	private String contactPhone;

	/**
	 * 版权信息
	 */
	@Column(name = "copyright")
	private String copyright;

	/**
	 * 公司简介
	 */
	@Column(name = "company_descript")
	private String companyDescript;

	/**
	 * 操作人
	 */
	@Column(name = "operator")
	private String operator;

	/**
	 * 创建时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "create_time")
	private LocalDateTime createTime;

	/**
	 * 修改时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "update_time")
	private LocalDateTime updateTime;

	/**
	 * 商家类型 0、平台自营 1、第三方商家
	 */
	@Column(name = "company_type")
	private Integer companyType;

	/**
	 * 账户是否全部收到打款 0、否 1、是
	 */
	@Column(name = "is_account_checked")
	private Integer isAccountChecked;

	/**
	 * 社会信用代码
	 */
	@Column(name = "social_credit_code")
	private String socialCreditCode;

	/**
	 * 住所
	 */
	@Column(name = "address")
	private String address;

	/**
	 * 法定代表人
	 */
	@Column(name = "legal_representative")
	private String legalRepresentative;

	/**
	 * 注册资本
	 */
	@Column(name = "registered_capital")
	private BigDecimal registeredCapital;

	/**
	 *  成立日期
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "found_date")
	private LocalDateTime foundDate;

	/**
	 * 营业期限自
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "business_term_start")
	private LocalDateTime businessTermStart;

	/**
	 * 营业期限至
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "business_term_end")
	private LocalDateTime businessTermEnd;

	/**
	 * 经营范围
	 */
	@Column(name = "business_scope")
	private String businessScope;

	/**
	 * 营业执照副本电子版
	 */
	@Column(name = "business_licence")
	private String businessLicence;

	/**
	 * 法人身份证正面
	 */
	@Column(name = "front_ID_card")
	private String frontIdcard;

	/**
	 * 商家编号
	 */
	@Column(name = "company_code")
	private String companyCode;

	/**
	 * 商家名称
	 */
	@Column(name = "supplier_name")
	private String supplierName;

	/**
	 * 是否删除 0 否  1 是
	 */
	@Column(name = "del_flag")
	@Enumerated
	private DeleteFlag delFlag;


}