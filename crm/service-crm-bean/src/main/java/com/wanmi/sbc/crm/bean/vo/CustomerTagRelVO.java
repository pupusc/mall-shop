package com.wanmi.sbc.crm.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>会员标签关联VO</p>
 * @author dyt
 * @date 2019-11-12 14:49:08
 */
@ApiModel
@Data
public class CustomerTagRelVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@ApiModelProperty(value = "主键id")
	private Long id;

	/**
	 * 会员id
	 */
	@ApiModelProperty(value = "会员id")
	private String customerId;

	/**
	 * 标签id
	 */
	@ApiModelProperty(value = "标签id")
	private Long tagId;

    /**
     * 标签id
     */
    @ApiModelProperty(value = "标签名称")
    private String tagName;
}