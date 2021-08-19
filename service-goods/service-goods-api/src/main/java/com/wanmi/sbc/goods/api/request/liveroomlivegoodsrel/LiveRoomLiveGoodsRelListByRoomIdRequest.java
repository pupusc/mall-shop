package com.wanmi.sbc.goods.api.request.liveroomlivegoodsrel;

import com.wanmi.sbc.common.base.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Map;


/**
 * <p>单个查询直播房间和直播商品关联表请求参数</p>
 * @author zwb
 * @date 2020-06-08 09:12:17
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LiveRoomLiveGoodsRelListByRoomIdRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * Map
	 */
	@ApiModelProperty(value = "Map")
	@NotNull
	private Map<Long, Long> roomIdAndStoreIdMap;

}