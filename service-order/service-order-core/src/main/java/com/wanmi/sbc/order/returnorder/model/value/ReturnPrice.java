package com.wanmi.sbc.order.returnorder.model.value;

import com.wanmi.sbc.common.exception.SbcRuntimeException;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 退货总金额
 * Created by jinwei on 19/4/2017.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReturnPrice {

    private Map<String, String> applyDeliverPrice = new HashMap<>();
    /**
     * 申请金额状态，是否启用
     */
    private Boolean applyStatus;

    /**
     * 申请金额
     */
    private BigDecimal applyPrice;

    /**
     * 商品总金额
     */
    private BigDecimal totalPrice;

    /**
     * 运费金额
     */
    private BigDecimal deliverPrice;

    /**
     * 实际运费积分
     */
    private BigDecimal deliveryPointPrice;

    /**
     * 实际运费积分
     */
    private Long deliveryPoint;

    /**
     * 实际运费现金金额
     */
    private BigDecimal deliveryPayPrice;

    /**
     * 实退金额，从退款流水中取的
     */
    private BigDecimal actualReturnPrice;

    /**
     * 供货总额
     */
    private BigDecimal providerTotalPrice;

    /**
     * 应退定金
     */
    private BigDecimal earnestPrice;

    /**
     * 应退尾款
     */
    private BigDecimal tailPrice;

    /**
     * 是否是尾款申请
     */
    private Boolean isTailApply;

    /**
     * @param returnPrice
     * @return
     */
    public DiffResult diff(ReturnPrice returnPrice) {
        return new DiffBuilder(this, returnPrice, ToStringStyle.JSON_STYLE)
            .append("applyStatus", applyStatus, returnPrice.getApplyStatus())
            .append("applyPrice", applyPrice, returnPrice.getApplyPrice())
            .append("totalPrice", totalPrice, returnPrice.getTotalPrice())
                .append("providerTotalPrice", providerTotalPrice, returnPrice.getProviderTotalPrice())
            .build();
    }

    /**
     * 合并
     *
     * @param newPrice
     */
    public void merge(ReturnPrice newPrice) {
        DiffResult diffResult = this.diff(newPrice);
        diffResult.getDiffs().stream().forEach(
            diff -> {
                String fieldName = diff.getFieldName();
                switch(fieldName){
                    case "applyStatus":
                        mergeSimple(fieldName, diff.getRight());
                        break;
                    case "applyPrice":
                        mergeSimple(fieldName, diff.getRight());
                        break;
                    case "totalPrice":
                        mergeSimple(fieldName, diff.getRight());
                        break;
                    case "providerTotalPrice":
                        mergeSimple(fieldName, diff.getRight());
                        break;
                    default:
                        break;
                }

                }
        );

    }

    private void mergeSimple(String fieldName, Object right) {
        Field field = ReflectionUtils.findField(ReturnPrice.class, fieldName);
        try {
            field.setAccessible(true);
            field.set(this, right);
        } catch (IllegalAccessException e) {
            throw new SbcRuntimeException("K-050113", new Object[]{ReturnPrice.class, fieldName});
        }
    }

    /**
     * 拼接所有diff
     *
     * @param price
     * @return
     */
    public List<String> buildDiffStr(ReturnPrice price) {
        Function<Object, String> f = (s) -> {
            if (s == null || StringUtils.isBlank(s.toString())) {
                return "空";
            } else {
                return s.toString().trim();
            }
        };
        DiffResult diffResult = this.diff(price);
        return diffResult.getDiffs().stream().map(
                diff -> {
                    String result = "";
                    switch (diff.getFieldName()) {
                        case "applyStatus":
                            if ((Boolean) diff.getRight()) {
                                result = "申请退款金额";
                            } else {
                                result = "取消申请退款金额";
                            }
                            break;
                        case "applyPrice":
                            result = String.format("申请退款金额 由 %s 变更为 %s",
                                    f.apply(diff.getLeft().toString()),
                                    f.apply(diff.getRight().toString())
                            );
                            break;
                        default:
                            break;
                    }
                    return result;
                }
        ).collect(Collectors.toList());

    }

}
