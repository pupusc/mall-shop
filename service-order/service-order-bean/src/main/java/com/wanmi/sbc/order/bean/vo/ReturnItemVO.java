package com.wanmi.sbc.order.bean.vo;

import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.goods.bean.enums.GoodsType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.util.ReflectionUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class ReturnItemVO implements Serializable {

    private static final long serialVersionUID = -3797266710914781701L;

    @ApiModelProperty(value = "skuId")
    private String skuId;

    @ApiModelProperty(value = "sku 名称")
    private String skuName;

    @ApiModelProperty(value = "sku 编号")
    private String skuNo;

    /**
     * 规格信息
     */
    @ApiModelProperty(value = "规格信息")
    private String specDetails;

    /**
     * 退货商品单价 = 商品原单价 - 商品优惠单价
     */
    @ApiModelProperty(value = "退货商品单价 = 商品原单价 - 商品优惠单价")
    private BigDecimal price;

    /**
     * 平摊价格
     */
    @ApiModelProperty(value = "平摊价格")
    private BigDecimal splitPrice;
    /**
     * 商品类型
     */
    private GoodsType goodsType;
    /**
     * 供货价
     */
    @ApiModelProperty(value = "供货价")
    private BigDecimal supplyPrice;

    /**
     * 供货价小计
     */
    @ApiModelProperty(value = "供货价小计")
    private BigDecimal providerPrice;

    @ApiModelProperty(value = "供应商ID")
    private Long providerId;

    /**
     * 订单平摊价格
     */
    @ApiModelProperty(value = "订单平摊价格")
    private BigDecimal orderSplitPrice;

    /**
     * 申请退货数量
     */
    @ApiModelProperty(value = "申请退货数量")
    private Integer num;

    /**
     * 退货商品图片路径
     */
    @ApiModelProperty(value = "退货商品图片路径")
    private String pic;

    /**
     * 单位
     */
    @ApiModelProperty(value = "单位")
    private String unit;

    /**
     * 仍可退数量
     */
    @ApiModelProperty(value = "仍可退数量")
    private Integer canReturnNum;

    /**
     * 购买积分，被用于普通订单的积分+金额混合商品
     */
    @ApiModelProperty(value = "购买积分")
    private Long buyPoint;

    /**
     * 应退积分
     */
    @ApiModelProperty(value = "应退积分")
    private Long splitPoint;

    /**
     * 购买知豆
     */
    @ApiModelProperty(value = "购买知豆")
    private Long buyKnowledge;

    /**
     * 应退知豆
     */
    @ApiModelProperty(value = "应退知豆")
    private Long splitKnowledge;



    /**
     * 第三方平台的spuId
     */
    private String thirdPlatformSpuId;

    /**
     * 第三方平台的skuId
     */
    private String thirdPlatformSkuId;

    /**
     * 商品来源，0供应商，1商家 2linkedMall
     */
    private Integer goodsSource;

    /**
     *第三方平台类型，0，linkedmall
     */
    private ThirdPlatformType thirdPlatformType;


    /**
     * 第三方平台-明细子订单id
     */
    private String thirdPlatformSubOrderId;

    /**
     * 售后三连击
     */
    private BigDecimal applyRealPrice;

    private Long applyKnowledge;

    private Long applyPoint;


    /**
     * @param returnItem
     * @return
     */
    public DiffResult diff(ReturnItemVO returnItem) {
        return new DiffBuilder(this, returnItem, ToStringStyle.JSON_STYLE)
                .append("num", num, returnItem.getNum())
                .build();
    }

    public void merge(ReturnItemVO newReturnItem) {
        DiffResult diffResult = this.diff(newReturnItem);
        diffResult.getDiffs().stream().forEach(
                diff -> {
                    String fieldName = diff.getFieldName();
                    switch (fieldName) {
                        case "num":
                            mergeSimple(fieldName, diff.getRight());
                            break;
                        default:
                            break;
                    }

                }
        );
    }

    private void mergeSimple(String fieldName, Object right) {
        Field field = ReflectionUtils.findField(ReturnItemVO.class, fieldName);
        try {
            field.setAccessible(true);
            field.set(this, right);
        } catch (IllegalAccessException e) {
            throw new SbcRuntimeException("K-050113", new Object[]{ReturnItemVO.class, fieldName});
        }
    }

    /**
     * 拼接所有diff
     *
     * @param returnItem
     * @return
     */
    public List<String> buildDiffStr(ReturnItemVO returnItem) {
        DiffResult diffResult = this.diff(returnItem);
        return diffResult.getDiffs().stream().map(
                diff -> {
                    String result = "";
                    switch (diff.getFieldName()) {
                        case "num":
                            result = String.format("商品 %s 购买数量 由 %s 变更为 %s",
                                    skuId,
                                    diff.getLeft().toString(),
                                    diff.getRight().toString()
                            );
                        default:
                            break;
                    }
                    return result;
                }
        ).collect(Collectors.toList());

    }

}
