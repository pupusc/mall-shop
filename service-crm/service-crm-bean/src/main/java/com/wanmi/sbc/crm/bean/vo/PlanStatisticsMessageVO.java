package com.wanmi.sbc.crm.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.util.CustomLocalDateSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateDeserializer;
import java.time.LocalDate;
import lombok.Data;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>运营计划效果统计站内信收到人/次统计数据VO</p>
 * @author lvzhenwei
 * @date 2020-02-05 15:08:00
 */
@ApiModel
@Data
public class PlanStatisticsMessageVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 运营计划id
	 */
	@ApiModelProperty(value = "运营计划id")
	private Long planId;

	/**
	 * 站内信收到人数
	 */
	@ApiModelProperty(value = "站内信收到人数")
	private Integer messageReceiveNum;

	/**
	 * 站内信收到人次
	 */
	@ApiModelProperty(value = "站内信收到人次")
	private Integer messageReceiveTotal;

	/**
	 * 统计日期
	 */
	@ApiModelProperty(value = "统计日期")
	@JsonSerialize(using = CustomLocalDateSerializer.class)
	@JsonDeserialize(using = CustomLocalDateDeserializer.class)
	private LocalDate statisticsDate;

}