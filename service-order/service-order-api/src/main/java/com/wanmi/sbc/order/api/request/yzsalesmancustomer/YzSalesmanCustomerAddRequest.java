package com.wanmi.sbc.order.api.request.yzsalesmancustomer;

import lombok.*;
import javax.validation.constraints.*;
import org.hibernate.validator.constraints.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>有赞销售员客户关系新增参数</p>
 * @author he
 * @date 2021-03-02 10:10:55
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class YzSalesmanCustomerAddRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * nickname
	 */
	@ApiModelProperty(value = "nickname")
	@Length(max=50)
	private String nickname;

	/**
	 * ctUid
	 */
	@ApiModelProperty(value = "ctUid")
	@Max(9223372036854775807L)
	private Long ctUid;

	/**
	 * isValid
	 */
	@ApiModelProperty(value = "isValid")
	@Max(127)
	private Integer isValid;

	/**
	 * avatar
	 */
	@ApiModelProperty(value = "avatar")
	@Length(max=255)
	private String avatar;

	/**
	 * ctYzOpenId
	 */
	@ApiModelProperty(value = "ctYzOpenId")
	@Length(max=32)
	private String ctYzOpenId;
	/**
	 * ctYzOpenId
	 */
	@ApiModelProperty(value = "ctYzOpenId")
	private String mobile;

	/**
	 * relationId
	 */
	@ApiModelProperty(value = "relationId")
	@Max(9223372036854775807L)
	private Long relationId;

	/**
	 * allChannelName
	 */
	@ApiModelProperty(value = "allChannelName")
	@Length(max=255)
	private String allChannelName;

}