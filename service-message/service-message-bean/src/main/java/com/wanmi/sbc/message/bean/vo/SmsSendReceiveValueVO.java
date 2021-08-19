package com.wanmi.sbc.message.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>接受明细</p>
 * @author zgl
 * @date 2019-12-03 15:36:05
 */
@ApiModel
@Data
public class SmsSendReceiveValueVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@ApiModelProperty(value = "id")
	private Long id;

	/**
	 * 名称
	 */
	@ApiModelProperty(value = "接受名称")
	private String name;


    /**
     * 群体类型 0:系统1:自定义
     */
    @ApiModelProperty(value = "群体类型 0:系统1:自定义")
	private String type;

	@ApiModelProperty(value = "手机号")
    private String account;
}