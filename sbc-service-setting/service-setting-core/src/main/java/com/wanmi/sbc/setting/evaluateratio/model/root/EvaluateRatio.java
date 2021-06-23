package com.wanmi.sbc.setting.evaluateratio.model.root;

import java.math.BigDecimal;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import java.time.LocalDateTime;

import lombok.Data;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import java.io.Serializable;

/**
 * <p>商品评价系数设置实体类</p>
 * @author liutao
 * @date 2019-02-27 15:53:40
 */
@Data
@Entity
@Table(name = "evaluate_ratio")
public class EvaluateRatio implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 系数id
	 */
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ratio_id")
	private String ratioId;

	/**
	 * 商品评论系数
	 */
	@Column(name = "goods_ratio")
	private BigDecimal goodsRatio;

	/**
	 * 服务评论系数
	 */
	@Column(name = "server_ratio")
	private BigDecimal serverRatio;

	/**
	 * 物流评分系数
	 */
	@Column(name = "logistics_ratio")
	private BigDecimal logisticsRatio;

	/**
	 * 是否删除标志 0：否，1：是
	 */
	@Column(name = "del_flag")
	private Integer delFlag;

	/**
	 * 创建时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
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
	@Column(name = "update_time")
	private LocalDateTime updateTime;

	/**
	 * 修改人
	 */
	@Column(name = "update_person")
	private String updatePerson;

	/**
	 * 删除时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "del_time")
	private LocalDateTime delTime;

	/**
	 * 删除人
	 */
	@Column(name = "del_person")
	private String delPerson;

}