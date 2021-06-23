package com.wanmi.sbc.setting.systempointsconfig.model.root;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.EnableStatus;
import com.wanmi.sbc.setting.bean.enums.PointsUsageFlag;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>积分设置实体类</p>
 * @author yxz
 * @date 2019-03-28 16:24:21
 */
@Data
@Entity
@Table(name = "system_points_config")
public class SystemPointsConfig implements Serializable {
	private static final long serialVersionUID = 145987333623095405L;

	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "points_config_id")
	private String pointsConfigId;

	/**
	 * 满x积分可用
	 */
	@Column(name = "over_points_available")
	private Long overPointsAvailable;

	/**
	 * 积分抵扣限额
	 */
	@Column(name = "max_deduction_rate")
	private BigDecimal maxDeductionRate;

	/**
	 * 积分过期月份
	 */
	@Column(name = "points_expire_month")
	private Integer pointsExpireMonth;

	/**
	 * 积分过期日期
	 */
	@Column(name = "points_expire_day")
	private Integer pointsExpireDay;

	/**
	 * 积分说明
	 */
	@Column(name = "remark")
	private String remark;

	/**
	 * 使用方式 0:订单抵扣,1:商品抵扣
	 */
	@Enumerated
	@Column(name = "points_usage_flag")
	private PointsUsageFlag pointsUsageFlag;

    /**
     * 积分价值
     */
    @Column(name = "points_worth")
    private Long pointsWorth;

	/**
	 * 是否启用标志 0：停用，1：启用
	 */
	@Enumerated
	@Column(name = "status")
	private EnableStatus status;

	/**
	 * 是否删除标志 0：否，1：是
	 */
	@Enumerated
	@Column(name = "del_flag")
	private DeleteFlag delFlag;

	/**
	 * 创建时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "create_time")
	private LocalDateTime createTime;

	/**
	 * 修改时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "update_time")
	private LocalDateTime updateTime;

	/**
	 * 最近使用方式的切换时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "switch_usage_time")
	private LocalDateTime switchUsageTime;

}