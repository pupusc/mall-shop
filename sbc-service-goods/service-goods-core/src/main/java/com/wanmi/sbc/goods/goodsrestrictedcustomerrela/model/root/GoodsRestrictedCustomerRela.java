package com.wanmi.sbc.goods.goodsrestrictedcustomerrela.model.root;


import com.wanmi.sbc.goods.bean.enums.AssignPersonRestrictedType;
import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;

/**
 * <p>限售配置会员关系表实体类</p>
 * @author baijz
 * @date 2020-04-08 11:32:28
 */
@Data
@Entity
@Table(name = "goods_restricted_customer_rela")
public class GoodsRestrictedCustomerRela implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 限售会员的关系主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "rela_id")
	private Long relaId;

	/**
	 * 限售主键
	 */
	@Column(name = "restricted_id")
	private Long restrictedSaleId;

	/**
	 * 特定会员的限售类型 0: 会员等级  1：指定会员
	 */
	@Column(name = "assign_person_restricted_type")
	private AssignPersonRestrictedType assignPersonRestrictedType;

	/**
	 * 会员ID
	 */
	@Column(name = "customer_id")
	private String customerId;

	/**
	 * 会员等级ID
	 */
	@Column(name = "customer_level_id")
	private Long customerLevelId;

}