package com.wanmi.sbc.goods.api.request.goodsevaluateimage;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>商品评价图片新增参数</p>
 * @author liutao
 * @date 2019-02-26 09:56:17
 */
@Data
public class GoodsEvaluateImageAddRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 评价id
	 */
	@Length(max=32)
	private String evaluateId;

	/**
	 * spuid
	 */
	@Length(max=32)
	private String goodsId;

	/**
	 * 图片KEY
	 */
	@Length(max=255)
	private String imageKey;

	/**
	 * 图片名称
	 */
	@Length(max=45)
	private String imageName;

	/**
	 * 原图地址
	 */
	@Length(max=255)
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
	@Max(127)
	private Integer delFlag = 0;

}