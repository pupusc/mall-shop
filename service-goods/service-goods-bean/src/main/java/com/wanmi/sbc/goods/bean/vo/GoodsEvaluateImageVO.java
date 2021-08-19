package com.wanmi.sbc.goods.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>商品评价图片VO</p>
 * @author liutao
 * @date 2019-02-26 09:56:17
 */
@Data
public class GoodsEvaluateImageVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 图片Id
	 */
	private String imageId;

	/**
	 * 评价id
	 */
	private String evaluateId;

	/**
	 * 商品id
	 */
	private String goodsId;

	/**
	 * 图片KEY
	 */
	private String imageKey;

	/**
	 * 图片名称
	 */
	private String imageName;

	/**
	 * 原图地址
	 */
	private String artworkUrl;

	/**
	 * 是否展示 0：否，1：是
	 */
	private Integer isShow;

	/**
	 * 创建时间
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime updateTime;

	/**
	 * 删除标识,0:未删除1:已删除
	 */
	private Integer delFlag;

	/**
	 * 商品评价
	 */
	private GoodsEvaluateVO goodsEvaluate;

}