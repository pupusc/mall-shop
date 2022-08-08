package com.wanmi.sbc.goods.api.request.goodsevaluate;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.TerminalSource;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>商品评价新增参数</p>
 * @author liutao
 * @date 2019-02-25 15:17:42
 */
@Data
public class GoodsEvaluateAddRequest extends GoodsEvaluateBaseBO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 店铺Id
	 */
	@Max(9223372036854775807L)
	private Long storeId;

	/**
	 * 店铺名称
	 */
	@Length(max=150)
	private String storeName;

	/**
	 * 商品id(spuId)
	 */
	@Length(max=32)
	private String goodsId;

	/**
	 * 货品id(skuId)
	 */
	@Length(max=32)
	private String goodsInfoId;

	/**
	 * 商品名称
	 */
	@Length(max=255)
	private String goodsInfoName;

	/**
	 * 订单号
	 */
	@Length(max=255)
	private String orderNo;

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
	 * 商品规格信息
	 */
	@Length(max=255)
	private String specDetails;

	/**
	 * 会员Id
	 */
	@Length(max=32)
	private String customerId;

	/**
	 * 会员名称
	 */
	@Length(max=128)
	private String customerName;

	/**
	 * 会员登录账号|手机号
	 */
	@Length(max=20)
	private String customerAccount;

	/**
	 * 商品评分
	 */
	@Max(127)
	private Integer evaluateScore;

	/**
	 * 商品评价内容
	 */
	@Length(max=500)
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
	@Length(max=500)
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
	@Length(max=45)
	private String evaluateAnswerAccountName;

	/**
	 * 回复员工Id
	 */
	@Length(max=32)
	private String evaluateAnswerEmployeeId;

	/**
	 * 历史商品评分
	 */
	@Max(127)
	private Integer historyEvaluateScore;

	/**
	 * 历史商品评价内容
	 */
	@Length(max=500)
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
	@Length(max=500)
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
	@Length(max=45)
	private String historyEvaluateAnswerAccountName;

	/**
	 * 历史回复员工Id
	 */
	@Length(max=32)
	private String historyEvaluateAnswerEmployeeId;

	/**
	 * 点赞数
	 */
	@Max(9999999999L)
	private Integer goodNum;

	/**
	 * 是否匿名 0：否，1：是
	 */
	@Max(127)
	private Integer isAnonymous = 0;

	/**
	 * 是否已回复 0：否，1：是
	 */
	private Integer isAnswer;

	/**
	 * 是否已经修改 0：否，1：是
	 */
	@Max(127)
	private Integer isEdit = 0;

	/**
	 * 是否展示 0：否，1：是
	 */
	@Max(127)
	private Integer isShow = 1;

	/**
	 * 是否晒单 0：否，1：是
	 */
	@Max(127)
	private Integer isUpload = 0;

	/**
	 * 是否删除标志 0：否，1：是
	 */
	@Max(127)
	private Integer delFlag = 0;

	/**
	 * 创建时间
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTime;

	/**
	 * 创建人
	 */
	@Length(max=32)
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
	@Length(max=32)
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
	@Length(max=32)
	private String delPerson;

    /**
     * 终端来源
     */
	private TerminalSource terminalSource;

}