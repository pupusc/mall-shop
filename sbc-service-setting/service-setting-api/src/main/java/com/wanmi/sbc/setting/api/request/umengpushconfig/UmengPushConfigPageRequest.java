package com.wanmi.sbc.setting.api.request.umengpushconfig;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

/**
 * <p>友盟push接口配置分页查询请求参数</p>
 * @author bob
 * @date 2020-01-07 10:34:04
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UmengPushConfigPageRequest extends BaseQueryRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量查询-idList
	 */
	@ApiModelProperty(value = "批量查询-idList")
	private List<Integer> idList;

	/**
	 * id
	 */
	@ApiModelProperty(value = "id")
	private Integer id;

	/**
	 * androidKeyId
	 */
	@ApiModelProperty(value = "androidKeyId")
	private String androidKeyId;

	/**
	 * androidMsgSecret
	 */
	@ApiModelProperty(value = "androidMsgSecret")
	private String androidMsgSecret;

	/**
	 * androidKeySecret
	 */
	@ApiModelProperty(value = "androidKeySecret")
	private String androidKeySecret;

	/**
	 * iosKeyId
	 */
	@ApiModelProperty(value = "iosKeyId")
	private String iosKeyId;

	/**
	 * iosKeySecret
	 */
	@ApiModelProperty(value = "iosKeySecret")
	private String iosKeySecret;

}