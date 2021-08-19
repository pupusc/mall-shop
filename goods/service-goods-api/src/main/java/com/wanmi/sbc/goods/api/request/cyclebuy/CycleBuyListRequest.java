package com.wanmi.sbc.goods.api.request.cyclebuy;


import com.wanmi.sbc.goods.bean.enums.AddedFlag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <p>搜索查询周期购活动请求参数</p>
 * @author weiwenhao
 * @date 2021-01-21 09:15:37
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CycleBuyListRequest {


    /**
     * 周期购Ids
     */
    @ApiModelProperty(value = "批量SPU编号")
    private List<String> idList;

    /**
     * 周期购Id
     */
    @ApiModelProperty(value = "周期购Id")
    private Long id;

    /**
     * goodsId
     */
    @ApiModelProperty(value = "goodsId")
    private String goodsId;

    /**
     * 原商品Id
     */
    @ApiModelProperty(value = "原商品Id")
    private String originGoodsId;

    /**
     * 店铺ID
     */
    @ApiModelProperty(value = "店铺ID")
    private Long storeId;


    /**
     * 活动名称
     */
    @ApiModelProperty(value = "活动名称")
    private String activityName;


    /**
     * 上下架 0：上架 1:下架
     */
    @ApiModelProperty(value = "上下架 0：上架 1:下架")
    private AddedFlag addedFlag;


    /**
     * 删除标记
     */
    @ApiModelProperty(value = "删除标记", dataType = "com.wanmi.sbc.common.enums.DeleteFlag")
    private Integer delFlag;



}
