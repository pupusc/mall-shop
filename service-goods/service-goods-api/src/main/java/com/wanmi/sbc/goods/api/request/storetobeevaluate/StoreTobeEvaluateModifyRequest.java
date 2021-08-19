package com.wanmi.sbc.goods.api.request.storetobeevaluate;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.Max;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>店铺服务待评价修改参数</p>
 * @author lzw
 * @date 2019-03-20 17:01:46
 */
@ApiModel
@Data
public class StoreTobeEvaluateModifyRequest implements Serializable {

	private static final long serialVersionUID = -6272356006184934122L;

	/**
	 * id
	 */
	@ApiModelProperty(value = "id")
	@Length(max=32)
	private String id;

	/**
	 * 店铺Id
	 */
	@ApiModelProperty(value = "店铺Id")
	@Max(9223372036854775807L)
	private Long storeId;

	/**
	 * 店铺logo
	 */
	@ApiModelProperty(value = "店铺logo")
	private String storeLogo;

	/**
	 * 店铺名称
	 */
	@ApiModelProperty(value = "店铺名称")
	@Length(max=150)
	private String storeName;

	/**
	 * 订单号
	 */
	@ApiModelProperty(value = "订单号")
	@NotBlank
	@Length(max=255)
	private String orderNo;

	/**
	 * 购买商品数量
	 */
	@NotBlank
	@ApiModelProperty(value = "店铺名称")
	private Integer goodsNum;

	/**
	 * 会员Id
	 */
	@ApiModelProperty(value = "会员Id")
	@NotBlank
	@Length(max=32)
	private String customerId;

	/**
	 * 会员名称
	 */
	@ApiModelProperty(value = "会员名称")
	@Length(max=128)
	private String customerName;

	/**
	 * 会员登录账号|手机号
	 */
	@ApiModelProperty(value = "会员登录账号|手机号")
	@NotBlank
	@Length(max=20)
	private String customerAccount;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTime;

	/**
	 * 创建人
	 */
	@ApiModelProperty(value = "创建人")
	@Length(max=32)
	private String createPerson;

	/**
	 * 修改时间
	 */
	@ApiModelProperty(value = "修改时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime updateTime;

	/**
	 * 修改人
	 */
	@ApiModelProperty(value = "修改人")
	@Length(max=32)
	private String updatePerson;

}