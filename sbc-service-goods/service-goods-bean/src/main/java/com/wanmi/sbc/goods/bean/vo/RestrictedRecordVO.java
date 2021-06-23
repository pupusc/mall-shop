package com.wanmi.sbc.goods.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.util.CustomLocalDateSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateDeserializer;
import java.time.LocalDate;
import lombok.Data;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>限售VO</p>
 * @author 限售记录
 * @date 2020-04-11 15:59:01
 */
@ApiModel
@Data
public class RestrictedRecordVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 记录主键
	 */
	@ApiModelProperty(value = "记录主键")
	private Long recordId;

	/**
	 * 会员的主键
	 */
	@ApiModelProperty(value = "会员的主键")
	private String customerId;

	/**
	 * 货品主键
	 */
	@ApiModelProperty(value = "货品主键")
	private String goodsInfoId;

	/**
	 * 购买的数量
	 */
	@ApiModelProperty(value = "购买的数量")
	private Long purchaseNum;

	/**
	 * 周期类型（0: 终生，1:周  2:月  3:年）
	 */
	@ApiModelProperty(value = "周期类型（0: 终生，1:周  2:月  3:年）")
	private Integer restrictedCycleType;

	/**
	 * 开始时间
	 */
	@ApiModelProperty(value = "开始时间")
	@JsonSerialize(using = CustomLocalDateSerializer.class)
	@JsonDeserialize(using = CustomLocalDateDeserializer.class)
	private LocalDate startDate;

	/**
	 * 结束时间
	 */
	@ApiModelProperty(value = "结束时间")
	@JsonSerialize(using = CustomLocalDateSerializer.class)
	@JsonDeserialize(using = CustomLocalDateDeserializer.class)
	private LocalDate endDate;

}