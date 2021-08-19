package com.wanmi.sbc.marketing.grouponrecord.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>拼团活动参团信息表实体类</p>
 * @author groupon
 * @date 2019-05-17 16:17:44
 */
@Data
@Entity
@Table(name = "groupon_record")
public class GrouponRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * grouponRecordId
	 */
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "groupon_record_id")
	private String grouponRecordId;

	/**
	 * 拼团活动ID
	 */
	@Column(name = "groupon_activity_id")
	private String grouponActivityId;

	/**
	 * 会员ID
	 */
	@Column(name = "customer_id")
	private String customerId;

	/**
	 * SPU编号
	 */
	@Column(name = "goods_id")
	private String goodsId;

	/**
	 * sku编号
	 */
	@Column(name = "goods_info_id")
	private String goodsInfoId;

	/**
	 * 已购数量
	 */
	@Column(name = "buy_num")
	private Integer buyNum;

	/**
	 * 限购数量
	 */
	@Column(name = "limit_selling_num")
	private Integer limitSellingNum;

	/**
	 * createTime
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	@Column(name = "create_time")
	private LocalDateTime createTime;

	/**
	 * updateTime
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	@Column(name = "update_time")
	private LocalDateTime updateTime;

}