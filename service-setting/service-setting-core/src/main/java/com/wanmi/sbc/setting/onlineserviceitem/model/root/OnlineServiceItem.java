package com.wanmi.sbc.setting.onlineserviceitem.model.root;

import com.wanmi.sbc.common.enums.DeleteFlag;
import lombok.Data;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>onlineerviceItem实体类</p>
 * @author lq
 * @date 2019-11-05 16:10:54
 */
@Data
@Entity
@Table(name = "online_service_item")
public class OnlineServiceItem implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 在线客服座席id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "service_item_id")
	private Integer serviceItemId;

	/**
	 * 店铺ID
	 */
	@Column(name = "store_id")
	private Long storeId;

	/**
	 * 在线客服主键
	 */
	@Column(name = "online_service_id")
	private Integer onlineServiceId;

	/**
	 * 客服昵称
	 */
	@Column(name = "customer_service_name")
	private String customerServiceName;

	/**
	 * 客服账号
	 */
	@Column(name = "customer_service_account")
	private String customerServiceAccount;

	/**
	 * 删除标志 默认0：未删除 1：删除
	 */
	@Column(name = "del_flag")
	@Enumerated
	private DeleteFlag delFlag;

	/**
	 * 创建时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "create_time")
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "update_time")
	private LocalDateTime updateTime;

	/**
	 * 操作人
	 */
	@Column(name = "operate_person")
	private String operatePerson;

}