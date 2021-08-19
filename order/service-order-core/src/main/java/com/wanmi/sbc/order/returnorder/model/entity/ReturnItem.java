package com.wanmi.sbc.order.returnorder.model.entity;

import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.goods.bean.enums.GoodsType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 退货商品类目
 * Created by jinwei on 19/4/2017.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReturnItem {

    private String skuId;

    private String skuName;

    private String skuNo;

    /**
     * 规格信息
     */
    private String specDetails;

    /**
     * 退货商品单价 = 商品原单价 - 商品优惠单价
     */
    private BigDecimal price;

    /**
     * 平摊价格
     */
    private BigDecimal splitPrice;

    /**
     * 供货价
     */
    private BigDecimal supplyPrice;

    /**
     * 供货价小计
     */
    private BigDecimal providerPrice;

    /**
     * 供货商id
     */
    private Long providerId;
    /**
     * 订单平摊价格
     */
    private BigDecimal orderSplitPrice;

    /**
     * 申请退货数量
     */
    private Integer num;

    /**
     * 退货商品图片路径
     */
    private String pic;

    /**
     * 单位
     */
    private String unit;

    /**
     * 仍可退数量
     */
    private Integer canReturnNum;

    /**
     * 购买积分，被用于普通订单的积分+金额混合商品
     */
    private Long buyPoint;

    /**
     * 应退积分
     */
    private Long splitPoint;


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
     * 商品spuId
     */
    private String goodsId;
    /**
     * 商品类型
     */
    private GoodsType goodsType;

    /**
     * 商品spuId
     */
    private String spuId;

    /**
     * 商品spuId
     */
    private String spuName;

    /**
     * 商品分类
     */
    private Long cateId;

    /**
     * 商品一级分类
     */
    private Long cateTopId;

    /**
     * 品牌
     */
    private Long brandId;

    /**
     *
     * @param returnItem
     * @return
     */
    public DiffResult diff(ReturnItem returnItem){
        return new DiffBuilder(this, returnItem, ToStringStyle.JSON_STYLE)
            .append("num", num, returnItem.getNum())
            .build();
    }

    public void merge(ReturnItem newReturnItem){
        DiffResult diffResult = this.diff(newReturnItem);
        diffResult.getDiffs().stream().forEach(
            diff -> {
                String fieldName = diff.getFieldName();
                switch(fieldName){
                    case "num":
                        mergeSimple(fieldName, diff.getRight());
                        break;
                    default:
                        break;
                }

            }
        );
    }

    private void mergeSimple(String fieldName, Object right){
        Field field = ReflectionUtils.findField(ReturnItem.class, fieldName);
        try {
            field.setAccessible(true);
            field.set(this, right);
        } catch (IllegalAccessException e) {
            throw new SbcRuntimeException("K-050113", new Object[]{ReturnItem.class, fieldName });
        }
    }

    /**
     * 拼接所有diff
     * @param returnItem
     * @return
     */
    public List<String> buildDiffStr(ReturnItem returnItem){
        DiffResult diffResult = this.diff(returnItem);
        return diffResult.getDiffs().stream().map(
            diff -> {
                String result = "";
                switch(diff.getFieldName()){
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
