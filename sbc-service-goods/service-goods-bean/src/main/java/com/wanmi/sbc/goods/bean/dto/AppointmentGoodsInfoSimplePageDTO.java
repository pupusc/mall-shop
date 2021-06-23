package com.wanmi.sbc.goods.bean.dto;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.enums.AppointmentStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

/**
 * <p>预约活动商品信息表分页查询请求参数</p>
 *
 * @author groupon
 * @date 2019-05-15 14:49:12
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentGoodsInfoSimplePageDTO extends BaseQueryRequest {


    private static final long serialVersionUID = 1154213435297457823L;


    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    /**
     * 店铺名称
     */
    @ApiModelProperty(value = "店铺名称")
    private String storeName;

    /**
     * 预约类型 0：不预约不可购买  1：不预约可购买
     */
    @ApiModelProperty(value = "预约类型 0：不预约不可购买  1：不预约可购买")
    private Integer appointmentType;


    /**
     * 状态  0:全部 1:进行中，2 已暂停 3 未开始 4. 已结束
     */
    @ApiModelProperty(value = "状态  0:全部 1:进行中，2 已暂停 3 未开始 4. 已结束")
    private AppointmentStatus queryTab;


    /**
     * 商品skuId
     */
    @ApiModelProperty(value = "批量商品skuId")
    private List<String> goodsInfoIds;

    /**
     * 店铺storeId
     */
    @ApiModelProperty(value = "店铺storeId")
    private List<Long> storeIds;

    /**
     * 店铺storeId
     */
    @ApiModelProperty(value = "店铺storeId")
    private Long storeId;

    /**
     * 排序标识
     * 0:销量倒序
     * 1:好评数倒序
     * 2:评论率倒序
     * 3:排序号倒序
     * 4:排序号倒序
     * 5:成才数倒序
     */
    @ApiModelProperty(value = "排序标识")
    private Integer sortFlag;

}