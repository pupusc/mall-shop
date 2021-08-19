package com.wanmi.sbc.order.bean.vo;

import lombok.Data;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>有赞销售员客户关系VO</p>
 * @author he
 * @date 2021-03-02 10:10:55
 */
@ApiModel
@Data
public class YzSalesmanCustomerVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@ApiModelProperty(value = "id")
	private Long id;

	/**
	 * nickname
	 */
	@ApiModelProperty(value = "nickname")
	private String nickname;

	/**
	 * ctUid
	 */
	@ApiModelProperty(value = "ctUid")
	private Long ctUid;

	/**
	 * isValid
	 */
	@ApiModelProperty(value = "isValid")
	private Integer isValid;

	/**
	 * avatar
	 */
	@ApiModelProperty(value = "avatar")
	private String avatar;

	/**
	 * ctYzOpenId
	 */
	@ApiModelProperty(value = "ctYzOpenId")
	private String ctYzOpenId;

	/**
	 * relationId
	 */
	@ApiModelProperty(value = "relationId")
	private Long relationId;

	/**
	 * allChannelName
	 */
	@ApiModelProperty(value = "allChannelName")
	private String allChannelName;

}