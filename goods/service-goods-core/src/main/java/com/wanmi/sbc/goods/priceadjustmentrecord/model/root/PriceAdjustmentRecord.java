package com.wanmi.sbc.goods.priceadjustmentrecord.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.goods.bean.enums.PriceAdjustmentType;
import lombok.Data;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * <p>调价记录表实体类</p>
 * @author chenli
 * @date 2020-12-09 19:57:21
 */
@Data
@Entity
@Table(name = "price_adjustment_record")
public class PriceAdjustmentRecord {
	private static final long serialVersionUID = 1L;

	/**
	 * 调价单号
	 */
	@Id
	@Column(name = "id")
	private String id;

	/**
	 * 调价类型：0 市场价、 1 等级价、2 阶梯价、3 供货价
	 */
	@Column(name = "price_adjustment_type")
	@Enumerated
	private PriceAdjustmentType priceAdjustmentType;

	/**
	 * 店铺id
	 */
	@Column(name = "store_id")
	private Long storeId;

	/**
	 * 调价商品数
	 */
	@Column(name = "goods_num")
	private Long goodsNum;

	/**
	 * 生效时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	@Column(name = "effective_time")
	private LocalDateTime effectiveTime;

	/**
	 * 制单人名称
	 */
	@Column(name = "creator_name")
	private String creatorName;

	/**
	 * 制单人账号
	 */
	@Column(name = "creator_account")
	private String creatorAccount;

	/**
	 * 是否确认：0 未确认、1 已确认
	 */
	@Column(name = "confirm_flag")
	@Enumerated
	private DefaultFlag confirmFlag;

	/**
	 * 创建人
	 */
	@Column(name = "create_person")
	private String createPerson;

	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTime;

}