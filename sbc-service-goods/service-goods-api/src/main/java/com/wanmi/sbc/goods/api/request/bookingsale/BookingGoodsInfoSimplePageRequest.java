package com.wanmi.sbc.goods.api.request.bookingsale;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.enums.AppointmentStatus;
import com.wanmi.sbc.goods.bean.enums.BookingSaleTimeStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

/**
 * <p>预售活动商品信息表分页查询请求参数</p>
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingGoodsInfoSimplePageRequest extends BaseQueryRequest {


    private static final long serialVersionUID = -9027973540251141452L;
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
     * 预售类型 0：全款预售  1：定金预售
     */
    @ApiModelProperty(value = "预售类型 0：全款预售  1：定金预售")
    private Integer bookingType;


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
     * 4:按定金支付数倒序
     */
    @ApiModelProperty(value = "排序标识")
    private Integer sortFlag;

    /**
     * 是否需要显示规格明细
     */
    @ApiModelProperty(value = "是否显示规格")
    private Boolean havSpecTextFlag;

    /**
     * 预售时间筛选枚举，每个枚举之间是以or关系
     */
    @ApiModelProperty(value = "预售时间筛选枚举")
    private List<BookingSaleTimeStatus> bookingSaleTimeStatuses;


}