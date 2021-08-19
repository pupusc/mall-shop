package com.wanmi.sbc.marketing.grouponactivity.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.marketing.bean.enums.AuditStatus;
import lombok.Data;
import org.apache.commons.lang3.math.NumberUtils;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>拼团活动信息表实体类</p>
 * @author groupon
 * @date 2019-05-15 14:02:38
 */
@Data
@Entity
@Table(name = "groupon_activity")
public class GrouponActivity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 活动ID
	 */
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "groupon_activity_id")
	private String grouponActivityId;

	/**
	 * 拼团人数
	 */
	@Column(name = "groupon_num")
	private Integer grouponNum;

	/**
	 * 开始时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	@Column(name = "start_time")
	private LocalDateTime startTime;

	/**
	 * 结束时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	@Column(name = "end_time")
	private LocalDateTime endTime;

	/**
	 * 拼团分类ID
	 */
	@Column(name = "groupon_cate_id")
	private String grouponCateId;

	/**
	 * 是否自动成团
	 */
	@Column(name = "auto_groupon")
	private boolean autoGroupon;

	/**
	 * 是否包邮
	 */
	@Column(name = "free_delivery")
	private boolean freeDelivery;

	/**
	 * spu编号
	 */
	@Column(name = "goods_id")
	private String goodsId;

	/**
	 * spu编码
	 */
	@Column(name = "goods_no")
	private String goodsNo;

	/**
	 * spu商品名称
	 */
	@Column(name = "goods_name")
	private String goodsName;

	/**
	 * 店铺ID
	 */
	@Column(name = "store_id")
	private String storeId;

	/**
	 * 是否精选
	 */
	@Column(name = "sticky")
	private boolean sticky;

	/**
	 * 活动审核状态，0：待审核，1：审核通过，2：审核不通过
	 */
	@Column(name = "audit_status")
	private AuditStatus auditStatus;

	/**
	 * 审核不通过原因
	 */
	@Column(name = "audit_fail_reason")
	private String auditFailReason;

	/**
	 * 已成团人数
	 */
	@Column(name = "already_groupon_num")
	private Integer alreadyGrouponNum = NumberUtils.INTEGER_ZERO;

	/**
	 * 待成团人数
	 */
	@Column(name = "wait_groupon_num")
	private Integer waitGrouponNum = NumberUtils.INTEGER_ZERO;

	/**
	 * 团失败人数
	 */
	@Column(name = "fail_groupon_num")
	private Integer failGrouponNum = NumberUtils.INTEGER_ZERO;

	/**
	 * 是否删除，0：否，1：是
	 */
	@Column(name = "del_flag")
	private DeleteFlag delFlag = DeleteFlag.NO;

	/**
	 * 创建时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	@Column(name = "create_time")
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	@Column(name = "update_time")
	private LocalDateTime updateTime;

}