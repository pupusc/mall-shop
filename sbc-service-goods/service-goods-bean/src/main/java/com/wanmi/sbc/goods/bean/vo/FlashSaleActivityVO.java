package com.wanmi.sbc.goods.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>秒杀活动VO</p>
 * @author yxz
 * @date 2019-06-11 13:48:53
 */
@ApiModel
@Data
public class FlashSaleActivityVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 活动日期
	 */
	@ApiModelProperty(value = "活动日期")
	private String activityDate;

	/**
	 * 活动时间
	 */
	@ApiModelProperty(value = "活动时间")
	private String activityTime;

	/**
	 * 活动状态 即将开场，进行中，已结束
	 */
	@ApiModelProperty(value = "即将开场，进行中，已结束")
	private String status;

	/**
	 * 活动时间
	 */
	@ApiModelProperty(value = "活动时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime activityFullTime;

	/**
	 * 结束时间
	 */
	@ApiModelProperty(value = "结束时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime activityEndTime;

	/**
	 * 参与商家数
	 */
	private Integer storeNum;

	/**
	 * 抢购商品数量
	 */
	private Integer goodsNum;
}