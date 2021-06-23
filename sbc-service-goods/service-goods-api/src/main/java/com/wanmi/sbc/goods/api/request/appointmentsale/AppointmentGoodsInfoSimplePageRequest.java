package com.wanmi.sbc.goods.api.request.appointmentsale;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.enums.AppointmentStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

/**
 * <p>预约活动商品信息表分页查询请求参数</p>
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentGoodsInfoSimplePageRequest extends BaseQueryRequest {


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
    private Long storeId;

    /**
     * 店铺storeIdList
     */
    @ApiModelProperty(value = "店铺storeIdList")
    private List<Long> storeIds;

    /**
     * 排序标识
     * 0:销量倒序
     * 1:好评数倒序
     * 2:评论率倒序
     * 3:排序号倒序
     * 4:预约数倒序
     */
    @ApiModelProperty(value = "排序标识")
    private Integer sortFlag;

    /**
     * 是否需要显示规格明细
     */
    @ApiModelProperty(value = "是否显示规格")
    private Boolean havSpecTextFlag;


    /**
     * 状态 0：预约进行中
     */
    @ApiModelProperty(value = "预约状态 0：预约进行中")
    private Integer appointmentStatus;


}