package com.wanmi.sbc.setting.api.request.expresscompany;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import lombok.*;
import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>物流公司分页查询请求参数</p>
 * @author lq
 * @date 2019-11-05 16:10:00
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpressCompanyPageRequest extends BaseQueryRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量查询-主键ID,自增List
	 */
	@ApiModelProperty(value = "批量查询-主键ID,自增List")
	private List<Long> expressCompanyIdList;

	/**
	 * 主键ID,自增
	 */
	@ApiModelProperty(value = "主键ID,自增")
	private Long expressCompanyId;

	/**
	 * 物流公司名称
	 */
	@ApiModelProperty(value = "物流公司名称")
	private String expressName;

	/**
	 * 物流公司代码
	 */
	@ApiModelProperty(value = "物流公司代码")
	private String expressCode;

	/**
	 * 是否是常用物流公司 0：否 1：是
	 */
	@ApiModelProperty(value = "是否是常用物流公司 0：否 1：是")
	private Integer isChecked;

	/**
	 * 是否是用户新增 0：否 1：是
	 */
	@ApiModelProperty(value = "是否是用户新增 0：否 1：是")
	private Integer isAdd;

	/**
	 * 删除标志 默认0：未删除 1：删除
	 */
	@ApiModelProperty(value = "删除标志 默认0：未删除 1：删除")
	private DeleteFlag delFlag;

}