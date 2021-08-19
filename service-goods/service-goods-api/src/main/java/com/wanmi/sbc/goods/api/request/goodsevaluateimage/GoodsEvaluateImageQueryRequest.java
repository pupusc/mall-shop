package com.wanmi.sbc.goods.api.request.goodsevaluateimage;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>商品评价图片通用查询请求参数</p>
 * @author liutao
 * @date 2019-02-26 09:56:17
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsEvaluateImageQueryRequest extends BaseQueryRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量查询-图片IdList
	 */
	private List<String> imageIdList;

	/**
	 * 图片Id
	 */
	private String imageId;

	/**
	 * 评价id
	 */
	private String evaluateId;

	/**
	 * spuid
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
	 * 搜索条件:创建时间开始
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTimeBegin;
	/**
	 * 搜索条件:创建时间截止
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTimeEnd;

	/**
	 * 搜索条件:更新时间开始
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime updateTimeBegin;
	/**
	 * 搜索条件:更新时间截止
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime updateTimeEnd;

	/**
	 * 删除标识,0:未删除1:已删除
	 */
	private Integer delFlag;

}