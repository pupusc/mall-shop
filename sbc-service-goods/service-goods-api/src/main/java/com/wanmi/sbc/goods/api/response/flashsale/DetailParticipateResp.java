package com.wanmi.sbc.goods.api.response.flashsale;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @program: sbc-micro-service
 * @description: boss活动详情参与总数信息返回值
 * @create: 2019-06-12 10:54
 **/
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetailParticipateResp implements Serializable {

    private static final long serialVersionUID = 4316863103772971556L;

    @ApiModelProperty(value = "活动日期，如：2019-06-12")
    private String activityDate;

    @ApiModelProperty(value = "活动时间，如：13:00")
    private String activityTime;

    @ApiModelProperty(value = "参与商家总数")
    private Integer storeSum;

    @ApiModelProperty(value = "参与商品总数")
    private Integer goodsSum;
}