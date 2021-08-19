package com.wanmi.sbc.goods.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>商品评价VO</p>
 * @author liutao
 * @date 2019-02-25 15:14:16
 */
@Data
public class GoodsEvaluateVO implements Serializable {
	private static final long serialVersionUID = 1L;

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
	 * 规格信息
	 */
	private String specDetails;

	/**
	 * 购买时间
	 */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime buyTime;

	/**
	 * 商品图片
	 */
	private String goodsImg;

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
	 * 会员头像路径头像路径
	 */
	@ApiModelProperty(value = "头像路径")
	private String headimgurl;

	/**
	 * 商品评分
	 */
	private Integer evaluateScore;

	/**
	 * 商品评价内容
	 */
	private String evaluateContent;

	/**
	 * 发表评价时间
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime evaluateTime;

	/**
	 * 评论回复
	 */
	private String evaluateAnswer;

	/**
	 * 回复时间
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime evaluateAnswerTime;

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
	 * 历史发表评价时间
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime historyEvaluateTime;

	/**
	 * 历史评论回复
	 */
	private String historyEvaluateAnswer;

	/**
	 * 历史回复时间
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime historyEvaluateAnswerTime;

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
	 * 是否点赞
	 */
	private Integer isPraise;

	/**
	 * 是否匿名 0：否，1：是
	 */
	private Integer isAnonymous;

	/**
	 * 是否已经修改 0：否，1：是
	 */
	private Integer isEdit;

	/**
	 * 是否已经回复 0：否 1：是
	 */
	private Integer isAnswer;

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

	/**
	 * 商品图片
	 */
	private List<GoodsImageVO> goodsImages;

	/**
	 * 评价晒图
	 */
	private List<GoodsEvaluateImageVO> evaluateImageList;

	@ApiModelProperty(value = "会员标签")
	private List<String> customerLabelList;

	/**
	 * 是否系统评价 0：否，1：是
	 */
	@ApiModelProperty(value = "是否系统评价 0：否，1：是")
	private Integer isSys;
}