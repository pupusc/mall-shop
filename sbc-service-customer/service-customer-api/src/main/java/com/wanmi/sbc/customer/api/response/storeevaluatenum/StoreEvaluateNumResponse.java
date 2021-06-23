package com.wanmi.sbc.customer.api.response.storeevaluatenum;

import com.wanmi.sbc.customer.bean.vo.StoreEvaluateNumVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Auther: jiaojiao
 * @Date: 2019/3/19 14:56
 * @Description:
 */
@ApiModel
@Data
public class StoreEvaluateNumResponse
{
    @ApiModelProperty(value = "各项评分列表")
    private List<StoreEvaluateNumVO> storeEvaluateNumVOList;

    @ApiModelProperty(value = "评价人数")
    private Long evaluateCount;

    @ApiModelProperty(value = "综合评分")
    private BigDecimal compositeScore;


}
