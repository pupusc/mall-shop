package com.wanmi.sbc.goods.api.request.liveroomlivegoodsrel;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.base.BaseRequest;
import lombok.*;
import javax.validation.constraints.*;
import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>直播房间和直播商品关联表新增参数</p>
 * @author zwb
 * @date 2020-06-08 09:12:17
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LiveRoomLiveGoodsRelAddRequest extends BaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 直播房间id
	 */
	@ApiModelProperty(value = "直播房间id")
	@NotNull
	@Max(9223372036854775807L)
	private Long roomId;

	/**
	 * 直播商品id
	 */
	@ApiModelProperty(value = "直播商品id")
	@NotNull
	@Max(9223372036854775807L)
	private Long goodsId;

	/**
	 * 删除标识,0:未删除1:已删除
	 */
	@ApiModelProperty(value = "删除标识,0:未删除1:已删除", hidden = true)
	private DeleteFlag delFlag;

}