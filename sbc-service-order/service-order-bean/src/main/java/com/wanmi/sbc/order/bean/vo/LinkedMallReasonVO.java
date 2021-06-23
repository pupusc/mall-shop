package com.wanmi.sbc.order.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * linkedMall原因内容
 * Created by jinwei on 14/3/2017.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class LinkedMallReasonVO implements Serializable {

    private static final long serialVersionUID = -6414564526969459575L;

    /**
     * 原因id
     */
    @ApiModelProperty(value = "原因id")
    private Long reasonTextId;

    /**
     * 原因内容
     */
    @ApiModelProperty(value = "原因内容")
    private String reasonTips;

    /**
     * 是否要求上传凭证
     */
    @ApiModelProperty(value = "是否要求上传凭证")
    private Boolean proofRequired;

    /**
     * 是否要求留言
     */
    @ApiModelProperty(value = "是否要求留言")
    private Boolean refundDescRequired;
}
