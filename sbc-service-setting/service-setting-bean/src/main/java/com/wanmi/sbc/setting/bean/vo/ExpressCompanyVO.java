package com.wanmi.sbc.setting.bean.vo;

import com.wanmi.sbc.common.enums.DeleteFlag;
import lombok.Data;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>物流公司VO</p>
 * @author lq
 * @date 2019-11-05 16:10:00
 */
@ApiModel
@Data
public class ExpressCompanyVO implements Serializable {
	private static final long serialVersionUID = 1L;

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