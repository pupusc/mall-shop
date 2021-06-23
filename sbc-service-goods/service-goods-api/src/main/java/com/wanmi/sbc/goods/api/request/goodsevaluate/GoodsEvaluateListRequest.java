package com.wanmi.sbc.goods.api.request.goodsevaluate;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>商品评价列表查询请求参数</p>
 * @author liutao
 * @date 2019-02-25 15:17:42
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsEvaluateListRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量查询-评价idList
	 */
	private List<String> evaluateIdList;

	private List<String> orderNos;

	/**
	 * 评价id
	 */
	private String evaluateId;

	/**
	 * 店铺Id
	 */
	private Long storeId;

	/**
	 * 店铺名称
	 */
	private String storeName;

	/**
	 * 商品id(spuId)
	 */
	private String goodsId;

	/**
	 * 货品id(skuId)
	 */
	private String goodsInfoId;

	/**
	 * 商品名称
	 */
	private String goodsInfoName;

	/**
	 * 订单号
	 */
	private String orderNo;

	/**
	 * 商品图片
	 */
	private String goodsImg;

	/**
	 * 商品规格信息
	 */
	private String specDetails;

	/**
	 * 会员Id
	 */
	private String customerId;

	/**
	 * 会员名称
	 */
	private String customerName;

	/**
	 * 会员登录账号|手机号
	 */
	private String customerAccount;

	/**
	 * 商品评分
	 */
	private Integer evaluateScore;

	/**
	 * 商品评价内容
	 */
	private String evaluateContent;

	/**
	 * 搜索条件:发表评价时间开始
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime evaluateTimeBegin;
	/**
	 * 搜索条件:发表评价时间截止
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime evaluateTimeEnd;

	/**
	 * 评论回复
	 */
	private String evaluateAnswer;

	/**
	 * 搜索条件:回复时间开始
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime evaluateAnswerTimeBegin;
	/**
	 * 搜索条件:回复时间截止
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime evaluateAnswerTimeEnd;

	/**
	 * 回复人账号
	 */
	private String evaluateAnswerAccountName;

	/**
	 * 回复员工Id
	 */
	private String evaluateAnswerEmployeeId;

	/**
	 * 历史商品评分
	 */
	private Integer historyEvaluateScore;

	/**
	 * 历史商品评价内容
	 */
	private String historyEvaluateContent;

	/**
	 * 搜索条件:历史发表评价时间开始
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime historyEvaluateTimeBegin;
	/**
	 * 搜索条件:历史发表评价时间截止
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime historyEvaluateTimeEnd;

	/**
	 * 历史评论回复
	 */
	private String historyEvaluateAnswer;

	/**
	 * 搜索条件:历史回复时间开始
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime historyEvaluateAnswerTimeBegin;
	/**
	 * 搜索条件:历史回复时间截止
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime historyEvaluateAnswerTimeEnd;

	/**
	 * 历史回复人账号
	 */
	private String historyEvaluateAnswerAccountName;

	/**
	 * 历史回复员工Id
	 */
	private String historyEvaluateAnswerEmployeeId;

	/**
	 * 点赞数
	 */
	private Integer goodNum;

	/**
	 * 是否匿名 0：否，1：是
	 */
	private Integer isAnonymous;

	/**
	 * 是否已回复 0：否，1：是
	 */
	private Integer isAnswer;

	/**
	 * 是否已经修改 0：否，1：是
	 */
	private Integer isEdit;

	/**
	 * 是否展示 0：否，1：是
	 */
	private Integer isShow;

	/**
	 * 是否晒单 0：否，1：是
	 */
	private Integer isUpload;

	/**
	 * 是否删除标志 0：否，1：是
	 */
	private Integer delFlag;

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
	 * 创建人
	 */
	private String createPerson;

	/**
	 * 搜索条件:修改时间开始
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime updateTimeBegin;
	/**
	 * 搜索条件:修改时间截止
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime updateTimeEnd;

	/**
	 * 修改人
	 */
	private String updatePerson;

	/**
	 * 搜索条件:删除时间开始
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime delTimeBegin;
	/**
	 * 搜索条件:删除时间截止
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime delTimeEnd;

	/**
	 * 删除人
	 */
	private String delPerson;

}