package com.wanmi.sbc.customer.api.request.liveroomreplay;

import com.wanmi.sbc.common.base.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;


/**
 * <p>单个查询直播回放请求参数</p>
 * @author zwb
 * @date 2020-06-17 09:24:26
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LiveRoomReplayByRoomIdRequest extends BaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 直播房间id
	 */
	@ApiModelProperty(value = "直播房间id")
	@NotNull
	private Long roomId;

}