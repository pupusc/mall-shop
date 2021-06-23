package com.wanmi.sbc.goods.api.request.presellsale;

import com.wanmi.sbc.common.base.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.math.BigDecimal;

@Data
@ApiModel
public class PresellSaleGoodsSaveRequest extends BaseRequest {

    private static final long serialVersionUID = 5082316672258458036L;
    /**
     * 预售活动关联商品id
     */
    @ApiModelProperty(value = "预售活动关联商品id")
    private String presellSaleGoodsId;


    /**
     * 预售活动id
     */
    @ApiModelProperty(value = "预售活动id")
    private String presellSaleId;

    /**
     * 店铺ID
     */
    @ApiModelProperty(value = "店铺ID")
    private Long storeId;

    /**
     * 商品skuID
     */
    @ApiModelProperty(value = "商品skuID")
    private String goodsInfoId;


    /**
     * 商品spuId
     */
    @ApiModelProperty(value = "商品spuId")
    private String goodsId;


    /**
     * 预售商品定金金额
     */
    @ApiModelProperty(value = "预售商品定金金额")
    private BigDecimal handselPrice;


    /**
     * 预售商品定金膨胀金额
     */
    @ApiModelProperty(value = "预售商品定金膨胀金额")
    private BigDecimal inflationPrice;


    /**
     * 预售商品限购数量
     */
    @ApiModelProperty(value = "预售商品限购数量")
    private Integer presellSaleCount;

    /**
     * 支付定金人数
     */
    @ApiModelProperty(value = "支付定金人数")
    private Integer handselNum;


    /**
     * 支付尾款人数
     */
    @ApiModelProperty(value = "支付尾款人数")
    private Integer finalPaymentNum;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String create_person;


    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人")
    private String update_person;

    /**
     * 预售价格
     */
    @ApiModelProperty(value = "预售价格")
    private BigDecimal presellPrice;
}
