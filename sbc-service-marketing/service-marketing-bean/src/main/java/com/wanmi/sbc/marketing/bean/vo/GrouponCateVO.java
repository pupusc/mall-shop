package com.wanmi.sbc.marketing.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Enumerated;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>拼团活动信息表VO</p>
 * @author groupon
 * @date 2019-05-15 14:13:58
 */
@ApiModel
@Data
public class GrouponCateVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 拼团分类Id
	 */
    @ApiModelProperty(value = "拼团分类Id")
	private String grouponCateId;

	/**
	 * 拼团分类名称
	 */
    @ApiModelProperty(value = "拼团分类名称")
	private String grouponCateName;

	/**
	 * 是否是默认精选分类 0：否，1：是
	 */
    @ApiModelProperty(value = "是否是默认精选分类 0：否，1：是")
	@Enumerated
	private DefaultFlag defaultCate;
}