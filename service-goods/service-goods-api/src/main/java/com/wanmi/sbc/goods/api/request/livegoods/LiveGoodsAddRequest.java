package com.wanmi.sbc.goods.api.request.livegoods;

import java.math.BigDecimal;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import java.time.LocalDateTime;
import java.util.List;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.base.BaseRequest;
import lombok.*;
import javax.validation.constraints.*;
import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>直播商品新增参数</p>
 * @author zwb
 * @date 2020-06-10 11:05:45
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LiveGoodsAddRequest extends BaseRequest {
	private static final long serialVersionUID = 1L;


	/**
	 * accessToken
	 */
	@ApiModelProperty(value = "accessToken")
	@Length(max=255)
	private String accessToken;

	/**
	 * 直播间id
	 */
	@ApiModelProperty(value = "直播间id")
	@Max(9223372036854775807L)
	private Long roomId;


	/**
	 * 直播商品idList
	 */
	@ApiModelProperty(value = "直播商品idList")
	private List<Long> goodsIdList;




}