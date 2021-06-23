package com.wanmi.sbc.goods.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.enums.ShareChannel;
import com.wanmi.sbc.common.enums.TerminalSource;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import java.time.LocalDateTime;
import lombok.Data;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>商品分享VO</p>
 * @author zhangwenchang
 * @date 2020-03-06 13:46:24
 */
@ApiModel
@Data
public class GoodsShareRecordVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * shareId
	 */
	@ApiModelProperty(value = "shareId")
	private Long shareId;

	/**
	 * 会员Id
	 */
	@ApiModelProperty(value = "会员Id")
	private String customerId;

	/**
	 * SPU id
	 */
	@ApiModelProperty(value = "SPU id")
	private String goodsId;

	/**
	 * SKU id
	 */
	@ApiModelProperty(value = "SKU id")
	private String goodsInfoId;

	/**
	 * 店铺ID
	 */
	@ApiModelProperty(value = "店铺ID")
	private Long storeId;

	/**
	 * 公司信息ID
	 */
	@ApiModelProperty(value = "公司信息ID")
	private Long companyInfoId;

	/**
	 * 终端：1 H5，2pc，3APP，4小程序
	 */
	@ApiModelProperty(value = "终端：1 H5，2pc，3APP，4小程序")
	private TerminalSource terminalSource;

	/**
	 * 分享渠道：0微信，1朋友圈，2QQ，3QQ空间，4微博，5复制链接，6保存图片
	 */
	@ApiModelProperty(value = "分享渠道：0微信，1朋友圈，2QQ，3QQ空间，4微博，5复制链接，6保存图片")
	private ShareChannel shareChannel;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTime;

}