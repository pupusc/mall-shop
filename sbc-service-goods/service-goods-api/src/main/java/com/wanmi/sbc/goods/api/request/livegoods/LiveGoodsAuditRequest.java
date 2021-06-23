package com.wanmi.sbc.goods.api.request.livegoods;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.LiveGoodsVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>直播商品提审参数</p>
 * @author zwb
 * @date 2020-06-06 18:49:08
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LiveGoodsAuditRequest extends BaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 商品列表
	 */
	@ApiModelProperty(value = "商品列表")
	private List<LiveGoodsVO> goodsInfoVOList;

	/**
	 * accessToken
	 */
	@ApiModelProperty(value = "accessToken")
	@Length(max=255)
	private String accessToken;
}