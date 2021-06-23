package com.wanmi.sbc.goods.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.goods.bean.enums.PriceAdjustmentType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Enumerated;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>调价记录表VO</p>
 * @author chenli
 * @date 2020-12-09 19:57:21
 */
@ApiModel
@Data
public class PriceAdjustmentRecordVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 调价单号
	 */
	@ApiModelProperty(value = "调价单号")
	private String id;

	/**
	 * 调价类型：0 市场价、 1 等级价、2 阶梯价、3 供货价
	 */
	@ApiModelProperty(value = "调价类型：0 市场价、 1 等级价、 2 阶梯价、 3 供货价")
	private PriceAdjustmentType priceAdjustmentType;

	/**
	 * 店铺id
	 */
	@ApiModelProperty(value = "店铺id")
	private Long storeId;

	/**
	 * 调价商品数
	 */
	@ApiModelProperty(value = "调价商品数")
	private Long goodsNum;

	/**
	 * 生效时间
	 */
	@ApiModelProperty(value = "生效时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime effectiveTime;

	/**
	 * 制单人名称
	 */
	@ApiModelProperty(value = "制单人名称")
	private String creatorName;

	/**
	 * 制单人账户
	 */
	@ApiModelProperty(value = "制单人账户")
	private String creatorAccount;

	/**
	 * 是否确认：0 未确认、1 已确认
	 */
	@ApiModelProperty(value = "是否确认：0 未确认、1 已确认")
	@Enumerated
	private DefaultFlag confirmFlag;

	@ApiModelProperty(value = "创建人")
	private String createPerson;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTime;

}