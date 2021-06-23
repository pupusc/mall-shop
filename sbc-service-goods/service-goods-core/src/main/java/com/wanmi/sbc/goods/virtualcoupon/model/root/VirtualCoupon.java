package com.wanmi.sbc.goods.virtualcoupon.model.root;

import com.wanmi.sbc.common.enums.DeleteFlag;

import lombok.Data;
import javax.persistence.*;
import com.wanmi.sbc.common.base.BaseEntity;

/**
 * <p>卡券实体类</p>
 * @author 梁善
 * @date 2021-01-25 10:19:19
 */
@Data
@Entity
@Table(name = "virtual_coupon")
public class VirtualCoupon extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * 电子卡券ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	/**
	 * 店铺标识
	 */
	@Column(name = "store_id")
	private Long storeId;

	/**
	 * 名称
	 */
	@Column(name = "name")
	private String name;

	/**
	 * 总数量
	 */
	@Column(name = "sum_number")
	private Integer sumNumber;

	/**
	 * 已售总数量
	 */
	@Column(name = "saled_number")
	private Integer saledNumber;

	/**
	 * 0:兑换码 1:券码+密钥 2:链接
	 */
	@Column(name = "provide_type")
	private Integer provideType;

	/**
	 * 0:未发布 1:已发布
	 */
	@Column(name = "publish_status")
	private Integer publishStatus;

	/**
	 * 关联的skuId
	 */
	@Column(name = "sku_id")
	private String skuId;

	/**
	 * 备注
	 */
	@Column(name = "description")
	private String description;

	/**
	 * 删除标识;0:未删除1:已删除
	 */
	@Column(name = "del_flag")
	@Enumerated
	private DeleteFlag delFlag;

}