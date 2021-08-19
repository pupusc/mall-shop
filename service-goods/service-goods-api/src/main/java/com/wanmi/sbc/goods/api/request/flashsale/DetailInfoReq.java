package com.wanmi.sbc.goods.api.request.flashsale;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @program: sbc-micro-service
 * @description: boss查询商品详情参数
 * @create: 2019-06-12 10:46
 **/
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetailInfoReq extends BaseQueryRequest {

    private static final long serialVersionUID = -821131266906561957L;

    @ApiModelProperty(value = "活动日期，如：2019-06-12")
    private String activityDate;

    @ApiModelProperty(value = "活动时间，如：12:00")
    private String activityTime;

}