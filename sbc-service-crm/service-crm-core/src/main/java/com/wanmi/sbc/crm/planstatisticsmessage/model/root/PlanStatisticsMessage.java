package com.wanmi.sbc.crm.planstatisticsmessage.model.root;

import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import java.time.LocalDate;

import lombok.Data;
import javax.persistence.*;
import com.wanmi.sbc.common.base.BaseEntity;

/**
 * <p>运营计划效果统计站内信收到人/次统计数据实体类</p>
 * @author lvzhenwei
 * @date 2020-02-05 15:08:00
 */
@Data
@Entity
@Table(name = "plan_statistics_message")
public class PlanStatisticsMessage extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * 运营计划id
	 */
	@Id
	@GeneratedValue
	@Column(name = "plan_id")
	private Long planId;

	/**
	 * 站内信收到人数
	 */
	@Column(name = "message_receive_num")
	private Integer messageReceiveNum;

	/**
	 * 站内信收到人次
	 */
	@Column(name = "message_receive_total")
	private Integer messageReceiveTotal;

	/**
	 * 统计日期
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateConverter.class)
	@Column(name = "statistics_date")
	private LocalDate statisticsDate;

}