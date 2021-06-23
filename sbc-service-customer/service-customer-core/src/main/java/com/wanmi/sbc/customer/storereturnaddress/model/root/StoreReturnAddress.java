package com.wanmi.sbc.customer.storereturnaddress.model.root;

import com.wanmi.sbc.common.enums.DeleteFlag;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import java.time.LocalDateTime;

import lombok.Data;
import javax.persistence.*;
import com.wanmi.sbc.common.base.BaseEntity;
import org.hibernate.annotations.GenericGenerator;

/**
 * <p>店铺退货地址表实体类</p>
 * @author dyt
 * @date 2020-11-02 11:38:39
 */
@Data
@Entity
@Table(name = "store_return_address")
public class StoreReturnAddress extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * 收货地址ID
	 */
	@Id
	@Column(name = "address_id")
	private String addressId;

	/**
	 * 公司信息ID
	 */
	@Column(name = "company_info_id")
	private Long companyInfoId;

	/**
	 * 店铺信息ID
	 */
	@Column(name = "store_id")
	private Long storeId;

	/**
	 * 收货人
	 */
	@Column(name = "consignee_name")
	private String consigneeName;

	/**
	 * 收货人手机号码
	 */
	@Column(name = "consignee_number")
	private String consigneeNumber;

	/**
	 * 省份
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
	 * 街道id
	 */
	@Column(name = "street_id")
	private Long streetId;

	/**
	 * 详细街道地址
	 */
	@Column(name = "return_address")
	private String returnAddress;

	/**
	 * 是否是默认地址
	 */
	@Column(name = "is_default_address")
	private Boolean isDefaultAddress;

	/**
	 * 是否删除标志 0：否，1：是
	 */
	@Column(name = "del_flag")
	@Enumerated
	private DeleteFlag delFlag;

	/**
	 * 删除时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "delete_time")
	private LocalDateTime deleteTime;

	/**
	 * 删除人
	 */
	@Column(name = "delete_person")
	private String deletePerson;

}