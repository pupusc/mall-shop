package com.wanmi.sbc.order.exceptionoftradepoints.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DeleteFlag;

import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.order.bean.enums.HandleStatus;
import lombok.Data;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>积分订单抵扣异常表实体类</p>
 * @author caofang
 * @date 2021-03-10 18:54:25
 */
@Data
@Entity
@Table(name = "exception_of_trade_points")
public class ExceptionOfTradePoints implements Serializable {

	private static final long serialVersionUID = 1179022514371085048L;

	/**
	 * 异常标识ID
	 */
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "id")
	private String id;

	/**
	 * 订单id
	 */
	@Column(name = "trade_id")
	private String tradeId;

	/**
	 * 使用积分
	 */
	@Column(name = "points")
	private Long points;

	/**
	 * 异常码
	 */
	@Column(name = "error_code")
	private String errorCode;

	/**
	 * 异常描述
	 */
	@Column(name = "error_desc")
	private String errorDesc;

	/**
	 * 樊登积分抵扣码
	 */
	@Column(name = "deduct_code")
	private String deductCode;

	/**
	 * 处理状态,0：待处理，1：处理失败，2：处理成功
	 */
	@Column(name = "handle_status")
	@Enumerated
	private HandleStatus handleStatus;

	/**
	 * 错误次数
	 */
	@Column(name = "error_time")
	private Integer errorTime;

	/**
	 * 是否删除标志 0：否，1：是
	 */
	@Column(name = "del_flag")
	@Enumerated
	private DeleteFlag delFlag;

	/**
	 * 创建时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	@Column(name = "create_time")
	private LocalDateTime createTime;
}