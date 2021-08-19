package com.wanmi.sbc.message.api.request.appmessage;

import java.util.List;

import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.message.bean.vo.AppMessageVO;
import lombok.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>App站内信消息发送表新增参数</p>
 * @author xuyunpeng
 * @date 2020-01-06 10:53:00
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppMessageAddRequest extends BaseRequest {
	/**
	 * app消息
	 */
	@ApiModelProperty(value = "app消息")
	private AppMessageVO appMessageVO;

	/**
	 * 会员列表
	 */
	@ApiModelProperty(value = "会员列表")
	private List<String> customerIds;

}