package com.wanmi.sbc.goods.api.request.flashsaleactivity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;

/**
 * <p>抢购活动分页查询请求参数</p>
 * @author yxz
 * @date 2019-06-11 14:54:31
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlashSaleActivityPageRequest extends BaseQueryRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 搜索条件:活动时间开始
	 */
	@ApiModelProperty(value = "搜索条件:活动时间开始")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime fullTimeBegin;

	/**
	 * 搜索条件:活动时间截止
	 */
	@ApiModelProperty(value = "搜索条件:活动时间截止")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime fullTimeEnd;

	/**
	 * 搜索条件: 店铺ID
	 */
	@ApiModelProperty(value = "搜索条件:店铺ID")
	private Long storeId;

}