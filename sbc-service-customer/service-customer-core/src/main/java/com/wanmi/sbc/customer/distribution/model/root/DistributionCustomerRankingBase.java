package com.wanmi.sbc.customer.distribution.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>用户分销排行榜统计数据实体类</p>
 * @author lq
 * @date 2019-04-19 10:05:05
 */
@Data
@NoArgsConstructor
public class DistributionCustomerRankingBase implements Serializable {


	/**
	 * 会员ID
	 */
	private String customerId;

	/**
	 * 邀新人数\有效邀新人数\销售额(元)\预估收益
	 */
	private Long num;
	/**
	 * 销售额(元)\预估收益
	 */
	private BigDecimal totalAmount;

	public DistributionCustomerRankingBase(String customerId, Long num) {
		this.customerId = customerId;
		this.num = num;
	}

	public DistributionCustomerRankingBase(String customerId, BigDecimal totalAmount) {
		this.customerId = customerId;
		this.totalAmount = totalAmount;
	}
}