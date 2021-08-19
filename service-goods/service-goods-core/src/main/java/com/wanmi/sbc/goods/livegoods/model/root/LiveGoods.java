package com.wanmi.sbc.goods.livegoods.model.root;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import java.time.LocalDateTime;
import com.wanmi.sbc.common.enums.DeleteFlag;

import lombok.Data;
import javax.persistence.*;
import com.wanmi.sbc.common.base.BaseEntity;

/**
 * <p>直播商品实体类</p>
 * @author zwb
 * @date 2020-06-10 11:05:45
 */
@Data
@Entity
@Table(name = "live_goods")
public class LiveGoods extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	/**
	 * 微信断商品id
	 */
	@Column(name = "goods_id")
	private Long goodsId;

	/**
	 * 商品标题
	 */
	@Column(name = "name")
	private String name;

	/**
	 * 填入mediaID
	 */
	@Column(name = "cover_img_url")
	private String coverImgUrl;

	/**
	 * 价格类型，1：一口价，2：价格区间，3：显示折扣价
	 */
	@Column(name = "price_type")
	private Integer priceType;

	/**
	 * 直播商品价格左边界
	 */
	@Column(name = "price")
	private BigDecimal price;

	/**
	 * 直播商品价格右边界
	 */
	@Column(name = "price2")
	private BigDecimal price2;

	/**
	 * 商品详情页的小程序路径
	 */
	@Column(name = "url")
	private String url;

	/**
	 * 库存
	 */
	@Column(name = "stock")
	private Long stock;

	/**
	 * 商品详情id
	 */
	@Column(name = "goods_info_id")
	private String goodsInfoId;

	/**
	 * 店铺标识
	 */
	@Column(name = "store_id")
	private Long storeId;

	/**
	 * 提交审核时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "submit_time")
	private LocalDateTime submitTime;

	/**
	 * 审核单ID
	 */
	@Column(name = "audit_id")
	private Long auditId;

	/**
	 * 审核状态,0:未审核1 审核通过2审核失败3禁用中
	 */
	@Column(name = "audit_status")
	private Integer auditStatus;

	/**
	 * 审核原因
	 */
	@Column(name = "audit_reason")
	private String auditReason;

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

	/**
	 * 删除标记 0:未删除1:已删除
	 */
	@Column(name = "del_flag")
	@Enumerated
	private DeleteFlag delFlag;

	/**
	 * 1, 2：表示是为api添加商品，否则是在MP添加商品
	 */
	@Column(name = "third_party_tag")
	private Integer thirdPartyTag;

	/**
	 * 创建人
	 */
	@Column(name = "create_person")
	private String createPerson;

	/**
	 * 修改人
	 */
	@Column(name = "update_person")
	private String updatePerson;



	/**
	 * goodsId
	 */
	@Transient
	private String goodsIdForDetails;
}