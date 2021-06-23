package com.wanmi.sbc.goods.api.request.liveroomlivegoodsrel;

import com.wanmi.sbc.common.base.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;


/**
 * <p>单个查询直播房间和直播商品关联表请求参数</p>
 * @author zwb
 * @date 2020-06-08 09:12:17
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LiveRoomLiveGoodsRelByRoomIdRequest extends BaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@ApiModelProperty(value = "主键id")
	@NotNull
	private Long roomId;

}