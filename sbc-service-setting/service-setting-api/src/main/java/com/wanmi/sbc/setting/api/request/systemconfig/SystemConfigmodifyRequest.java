package com.wanmi.sbc.setting.api.request.systemconfig;

import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.common.enums.DeleteFlag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>系统配置表通用查询请求参数</p>
 * @author yang
 * @date 2019-11-05 18:33:04
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemConfigmodifyRequest extends BaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 *  编号
	 */
	@ApiModelProperty(value = " 编号")
	private Long id;

	/**
	 * 键
	 */
	@ApiModelProperty(value = "键")
	private String configKey;

	/**
	 * 类型
	 */
	@ApiModelProperty(value = "类型")
	private String configType;

	/**
	 * 名称
	 */
	@ApiModelProperty(value = "名称")
	private String configName;

	/**
	 * 备注
	 */
	@ApiModelProperty(value = "备注")
	private String remark;

	/**
	 * 状态,0:未启用1:已启用
	 */
	@ApiModelProperty(value = "状态,0:未启用1:已启用")
	private Integer status;

	/**
	 * 配置内容，如JSON内容
	 */
	@ApiModelProperty(value = "配置内容，如JSON内容")
	private String context;

	/**
	 * 删除标识,0:未删除1:已删除
	 */
	@ApiModelProperty(value = "删除标识,0:未删除1:已删除")
	private DeleteFlag delFlag;

}