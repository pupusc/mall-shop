package com.wanmi.sbc.order.trade.fsm.action;

import com.wanmi.sbc.marketing.bean.enums.MarketingSubType;
import com.wanmi.sbc.marketing.bean.vo.*;
import com.wanmi.sbc.order.trade.fsm.TradeAction;
import com.wanmi.sbc.order.trade.fsm.TradeStateContext;
import com.wanmi.sbc.order.trade.fsm.params.StateRequest;
import com.wanmi.sbc.order.trade.model.entity.TradeItem;
import com.wanmi.sbc.order.bean.enums.FlowState;
import com.wanmi.sbc.order.trade.model.root.Trade;
import com.wanmi.sbc.order.trade.model.entity.value.Consignee;
import com.wanmi.sbc.order.trade.model.entity.value.Invoice;
import com.wanmi.sbc.order.trade.model.entity.value.TradeEventLog;
import com.wanmi.sbc.order.trade.model.entity.value.TradePrice;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.Diff;
import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2017/4/21.
 */
@Component
public class RemedyAction extends TradeAction {

    @Override
    protected void evaluateInternal(Trade oldTrade, StateRequest request, TradeStateContext tsc) {
        // 只能修改未审核的订单
        StringBuilder remedyInfo = new StringBuilder(100);
        Trade newTrade = (Trade) tsc.getRequest().getData();

        // 购物清单
        checkTradeItems(remedyInfo, oldTrade.skuItemMap(), newTrade.skuItemMap());

        // 收货
        checkConsignee(remedyInfo, oldTrade.getConsignee(), newTrade.getConsignee());

        // 钱
        checkPrice(remedyInfo, oldTrade.getTradePrice(), newTrade.getTradePrice());

        // 发票
        checkInvoice(remedyInfo, oldTrade.getInvoice(), newTrade.getInvoice());

        // 其他
        checkOther(remedyInfo, oldTrade, newTrade);

        checkMarketing(remedyInfo, newTrade.getTradeMarketings());

        newTrade.setPlatform(oldTrade.getPlatform());

        remedyInfo.trimToSize();
        if (StringUtils.isNotBlank(remedyInfo)) {
            newTrade.appendTradeEventLog(
                    TradeEventLog
                            .builder()
                            .operator(tsc.getOperator())
                            .eventDetail(remedyInfo.toString())
                            .eventType(FlowState.REMEDY.getDescription())
                            .eventTime(LocalDateTime.now())
                            .build());
        }
        save(newTrade);
        if (StringUtils.isNotBlank(remedyInfo)) {
            super.operationLogMq.convertAndSend(tsc.getOperator(), FlowState.REMEDY.getDescription(), remedyInfo.toString());
        }
    }


    /**
     * 订单商品比对
     *
     * @param remedyInfo
     * @param oldSkuItemMap
     * @param newTradeItemMap @return
     */
    private void checkTradeItems(StringBuilder remedyInfo, ConcurrentHashMap<String, TradeItem> oldSkuItemMap, ConcurrentHashMap<String, TradeItem> newTradeItemMap) {
        //
        oldSkuItemMap
                .forEach((skuId, oldTradeItem) -> {
                    TradeItem newTradeItem = newTradeItemMap.remove(skuId);
                    if (newTradeItem == null) {
                        remedyInfo.append(String.format("删除商品， 商品编号：[ %s ], 商品名称：[ %s ]， 购买数量：[ %d ]；", oldTradeItem.getSkuId(), oldTradeItem.getSkuName(), oldTradeItem.getNum()));
                        oldSkuItemMap.remove(skuId);
                        return;
                    }

                    DiffBuilder diffBuilder = new DiffBuilder(oldTradeItem, newTradeItem, ToStringStyle.NO_CLASS_NAME_STYLE);
                    DiffResult result = diffBuilder
                            .append("购买数量", oldTradeItem.getNum(), newTradeItem.getNum())
                            .build();
                    List<Diff<?>> diffs = result.getDiffs();
//                    modifyObject(oldTradeItem, diffs);
                    appendRemedyInfo(String.format("商品：[ %s ]", oldTradeItem.getSkuName()), remedyInfo, diffs);
                });

        // 剩下就是新增的了
        newTradeItemMap
                .forEach((s, tradeItem) -> remedyInfo.append(String.format("新增商品，商品编号：[ %s ]， 商品名称：[ %s ]， 购买数量：[ %d ]；", tradeItem.getSkuId(), tradeItem.getSkuName(), tradeItem.getNum())));

    }

    /**
     * 其他属性比对
     *
     * @param remedyInfo
     * @param oldTrade
     * @param newTrade
     * @return
     */
    private void checkOther(StringBuilder remedyInfo, Trade oldTrade, Trade newTrade) {
        DiffBuilder diffBuilder = new DiffBuilder(oldTrade, newTrade, ToStringStyle.NO_CLASS_NAME_STYLE);
        DiffResult result = diffBuilder
                .append("买家备注", oldTrade.getBuyerRemark(), newTrade.getBuyerRemark())
                .append("卖家备注", oldTrade.getSellerRemark(), newTrade.getSellerRemark())
                .append("支付方式", oldTrade.getPayInfo().getDesc(), newTrade.getPayInfo().getDesc())
                .append("附件地址", oldTrade.getEncloses(), newTrade.getEncloses())
                .build();
        List<Diff<?>> diffs = result.getDiffs();
        appendRemedyInfo("订单", remedyInfo, diffs);
    }

    /**
     * @param remedyInfo
     * @param oldInvoice
     * @param newInvoice
     * @return
     */
    private List<Diff<?>> checkInvoice(StringBuilder remedyInfo, Invoice oldInvoice, Invoice newInvoice) {
        if (newInvoice == null) {
            return null;
        }
        DiffBuilder diffBuilder = new DiffBuilder(oldInvoice, newInvoice, ToStringStyle.NO_CLASS_NAME_STYLE);
        DiffResult result = diffBuilder
                .append("是否开票", oldInvoice.getType() == -1 ? "否" : "是", newInvoice.getType() == -1 ? "否" : "是")
//                .append("发票抬头|title", oldInvoice.getTitle(), newInvoice.getTitle())
                .build();
        List<Diff<?>> diffs = result.getDiffs();
        appendRemedyInfo("发票参数", remedyInfo, diffs);
        return diffs;
    }

    /**
     * @param remedyInfo
     * @param oldTradePrice
     * @param newTradePrice
     * @return
     */
    private List<Diff<?>> checkPrice(StringBuilder remedyInfo, TradePrice oldTradePrice, TradePrice newTradePrice) {
        if (newTradePrice == null) {
            return null;
        }
        DiffBuilder diffBuilder = new DiffBuilder(oldTradePrice, newTradePrice, ToStringStyle.NO_CLASS_NAME_STYLE);
        DiffResult result = diffBuilder
                .append("是否特价单", oldTradePrice.isSpecial() ? "是" : "否", newTradePrice.isSpecial() ? "是" : "否")
                .append("应付金额", "￥" + oldTradePrice.getTotalPrice(), "￥" + newTradePrice.getTotalPrice())
                .append("运费", "￥" + (oldTradePrice.getDeliveryPrice() == null ? "0" : oldTradePrice.getDeliveryPrice()),
                        "￥" + (newTradePrice.getDeliveryPrice() == null ? "0" : newTradePrice.getDeliveryPrice()))
                .build();
        List<Diff<?>> diffs = result.getDiffs();
        appendRemedyInfo("订单价格", remedyInfo, diffs);
        return diffs;
    }


    /**
     *
     */
    private List<Diff<?>> checkConsignee(StringBuilder remedyInfo, Consignee oldConsignee, Consignee newConsignee) {
        if (newConsignee == null) {
            return null;
        }
        DiffBuilder diffBuilder = new DiffBuilder(oldConsignee, newConsignee, ToStringStyle.NO_CLASS_NAME_STYLE);
        DiffResult result = diffBuilder
                .append("编号", oldConsignee.getId(), newConsignee.getId())
                .append("地址", oldConsignee.getDetailAddress(), newConsignee.getDetailAddress())
                .append("收货人", oldConsignee.getName(), newConsignee.getName())
                .append("联系电话", oldConsignee.getPhone(), newConsignee.getPhone())
                .build();
        List<Diff<?>> diffs = result.getDiffs();
        appendRemedyInfo("收货信息", remedyInfo, diffs);
        return diffs;
    }

    private void checkMarketing(StringBuilder remedyInfo, List<TradeMarketingVO> newMarketings) {
        Map<Long, List<TradeMarketingVO>> map = newMarketings.stream().collect(Collectors.groupingBy(TradeMarketingVO::getMarketingId));
        List<String> infos = new ArrayList<>();
        map.forEach((k, v) -> v.forEach(m -> {
                    String info = null;
                    DecimalFormat fm = new DecimalFormat("#.##");
                    MarketingSubType subType = m.getSubType();
                    switch (m.getMarketingType()) {
                        case REDUCTION:
                            MarketingFullReductionLevelVO reductionLevel = m.getReductionLevel();
                            if (reductionLevel != null) {
                                info = "满%s减%s";
                                if (subType == MarketingSubType.REDUCTION_FULL_AMOUNT) {
                                    info = String.format(info, fm.format(reductionLevel.getFullAmount()),
                                            reductionLevel.getReduction());
                                } else if (subType == MarketingSubType.REDUCTION_FULL_COUNT) {
                                    info = String.format(info, reductionLevel.getFullCount() + "件", reductionLevel.getReduction());
                                }
                            }
                            break;
                        case DISCOUNT:
                            MarketingFullDiscountLevelVO discountLevel = m.getFullDiscountLevel();
                            if (discountLevel != null) {
                                info = "满%s享%s折";
                                BigDecimal discount = discountLevel.getDiscount().multiply(BigDecimal.TEN)
                                        .setScale(1, BigDecimal.ROUND_HALF_UP);
                                if (subType == MarketingSubType.DISCOUNT_FULL_AMOUNT) {
                                    info = String.format(info, fm.format(discountLevel.getFullAmount()), discount);
                                } else if (subType == MarketingSubType.DISCOUNT_FULL_COUNT) {
                                    info = String.format(info, discountLevel.getFullCount() + "件", discount);
                                }
                            }
                            break;
                        case GIFT:
                            MarketingFullGiftLevelVO giftLevel = m.getGiftLevel();
                            if (giftLevel != null) {
                                info = "满%s获赠品";
                                if (subType == MarketingSubType.GIFT_FULL_AMOUNT) {
                                    info = String.format(info, fm.format(giftLevel.getFullAmount()));
                                } else if (subType == MarketingSubType.GIFT_FULL_COUNT) {
                                    info = String.format(info, giftLevel.getFullCount() + "件");
                                }
                            }
                            break;
                        case BUYOUT_PRICE:
                            MarketingBuyoutPriceLevelVO level = m.getBuyoutPriceLevel();
                            if (level != null) {
                                info = String.format("%s件%s元", level.getChoiceCount(), fm.format(level.getFullAmount()));
                            }
                            break;
                        default:
                            break;
                    }
                    if (StringUtils.isNotBlank(info)) {
                        infos.add(info);
                    }
                })
        );
        if (!infos.isEmpty()) {
             remedyInfo.append("更新后订单所参与的营销活动为：").append(
                 StringUtils.join(infos.stream().distinct().collect(Collectors.toList()),"、").concat("活动；\n")
             );
        }
    }

    /**
     * 增加日志
     *
     * @param remedyInfo
     * @param diffs
     */
    private void appendRemedyInfo(String prefix, StringBuilder remedyInfo, List<Diff<?>> diffs) {
        Function<Object, String> f = (s) -> {
            if (s == null || StringUtils.isBlank(s.toString())) {
                return "空";
            } else {
                return s.toString().trim();
            }
        };
        if (diffs != null) {
            diffs.forEach(diff -> {
                if (!(
                        (diff.getLeft() == null || StringUtils.isBlank(diff.getLeft().toString()))
                                &&
                                (diff.getRight() == null || StringUtils.isBlank(diff.getRight().toString()))
                )) {
                    remedyInfo.append(String.format("%s 的 %s 由 %s 变更为 %s； %n", prefix, diff.getFieldName(),
                            f.apply(diff.getLeft()), f.apply(diff.getRight())));
                }
            });

        }
    }

}
