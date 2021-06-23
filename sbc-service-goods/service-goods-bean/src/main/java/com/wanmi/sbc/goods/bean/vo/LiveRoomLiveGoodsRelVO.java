package com.wanmi.sbc.goods.bean.vo;

import lombok.Data;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>直播房间和直播商品关联表VO</p>
 * @author zwb
 * @date 2020-06-08 09:12:17
 */
@ApiModel
@Data
public class LiveRoomLiveGoodsRelVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@ApiModelProperty(value = "主键id")
	private Long id;

	/**
	 * 直播房间id
	 */
	@ApiModelProperty(value = "直播房间id")
	private Long roomId;

	/**
	 * 直播商品id
	 */
	@ApiModelProperty(value = "直播商品id")
	private Long goodsId;

}