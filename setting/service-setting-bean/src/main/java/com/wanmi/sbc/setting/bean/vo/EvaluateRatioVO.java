package com.wanmi.sbc.setting.bean.vo;

import java.math.BigDecimal;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import java.time.LocalDateTime;
import lombok.Data;
import java.io.Serializable;

/**
 * <p>商品评价系数设置VO</p>
 * @author liutao
 * @date 2019-02-27 15:53:40
 */
@Data
public class EvaluateRatioVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 系数id
	 */
	private String ratioId;

	/**
	 * 商品评论系数
	 */
	private BigDecimal goodsRatio;

	/**
	 * 服务评论系数
	 */
	private BigDecimal serverRatio;

	/**
	 * 物流评分系数
	 */
	private BigDecimal logisticsRatio;

	/**
	 * 是否删除标志 0：否，1：是
	 */
	private Integer delFlag;

	/**
	 * 创建时间
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTime;

	/**
	 * 创建人
	 */
	private String createPerson;

	/**
	 * 修改时间
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime updateTime;

	/**
	 * 修改人
	 */
	private String updatePerson;

	/**
	 * 删除时间
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime delTime;

	/**
	 * 删除人
	 */
	private String delPerson;

}