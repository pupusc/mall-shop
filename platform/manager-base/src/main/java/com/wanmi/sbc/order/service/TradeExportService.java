package com.wanmi.sbc.order.service;

import com.wanmi.sbc.common.util.excel.Column;
import com.wanmi.sbc.common.util.excel.ExcelHelper;
import com.wanmi.sbc.common.util.excel.impl.SpelColumnRender;
import com.wanmi.sbc.goods.bean.enums.DeliverWay;
import com.wanmi.sbc.order.bean.enums.FlowState;
import com.wanmi.sbc.order.bean.vo.ProviderTradeExportVO;
import com.wanmi.sbc.order.bean.vo.ReturnOrderVO;
import com.wanmi.sbc.order.bean.vo.TradeItemVO;
import com.wanmi.sbc.order.bean.vo.TradeVO;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by mac on 2017/5/6.
 */
@Service
public class TradeExportService {

    /**
     * @param tradeList
     * @param outputStream
     */
    public void export(List<TradeVO> tradeList, OutputStream outputStream, boolean existSupplier) {
        ExcelHelper excelHelper = new ExcelHelper();
        Column[] columns = {
                new Column("订单编号", new SpelColumnRender<TradeVO>("id")),
                new Column("下单时间", new SpelColumnRender<TradeVO>("tradeState.createTime")),
                new Column("客户名称", new SpelColumnRender<TradeVO>("buyer.name")),
                new Column("客户账号", new SpelColumnRender<TradeVO>("buyer.account")),
                new Column("客户级别", new SpelColumnRender<TradeVO>("buyer.levelName")),
                new Column("收货人", new SpelColumnRender<TradeVO>("consignee.name")),
                new Column("收货人手机", new SpelColumnRender<TradeVO>("consignee.phone")),
                new Column("收货人地址", new SpelColumnRender<TradeVO>("consignee.detailAddress")),
                new Column("支付方式", new SpelColumnRender<TradeVO>("payInfo.desc")),
                new Column("配送方式", (cell, object) -> {
                    cell.setCellValue(DeliverWay.EXPRESS.getDesc());
                }),
                new Column("配送费用", new SpelColumnRender<TradeVO>("tradePrice.deliveryPrice != null ? tradePrice.deliveryPrice : '0.00'")),
                new Column("订单商品金额", new SpelColumnRender<TradeVO>("tradePrice.goodsPrice")),
                new Column("订单特价金额", new SpelColumnRender<TradeVO>("tradePrice.special ? tradePrice.privilegePrice : '0.00'")),
                new Column("订单应付金额", new SpelColumnRender<TradeVO>("tradePrice.totalPrice")),
                new Column("商品SKU", (cell, object) -> {
                    TradeVO trade = (TradeVO) object;
                    StringBuilder sb = new StringBuilder(trade.getTradeItems().size() * 32);
                    trade
                            .getTradeItems()
                            .forEach(i -> {
                                sb.append(i.getSkuNo()).append(",");
                            });
                    sb.trimToSize();
                    cell.setCellValue(sb.substring(0, sb.length() - 1));
                }),
                new Column("商品种类", (cell, object) -> {
                    TradeVO trade = (TradeVO) object;
                    cell.setCellValue(trade.skuItemMap().size());
                }),
                new Column("商品总数量", (cell, object) -> {
                    Optional<Long> size = ((TradeVO) object)
                            .getTradeItems()
                            .stream()
                            .map(TradeItemVO::getNum)
                            .reduce((sum, item) -> {
                                sum += item;
                                return sum;
                            });
                    cell.setCellValue((double) size.get());
                }),
                new Column("买家备注", new SpelColumnRender<TradeVO>("buyerRemark")),
                new Column("卖家备注", new SpelColumnRender<TradeVO>("sellerRemark")),
                new Column("直充手机号", new SpelColumnRender<TradeVO>("directChargeMobile")),
                new Column("订单状态", (cell, object) -> {
                    TradeVO trade = (TradeVO) object;
                    FlowState flowState = trade.getTradeState().getFlowState();
                    String cellValue = "";
                    switch (flowState) {
                        case INIT:
                            cellValue = "待审核";
                            break;
                        case AUDIT:
                        case DELIVERED_PART:
                            cellValue = "待发货";
                            break;
                        case DELIVERED:
                            cellValue = "待收货";
                            break;
                        case CONFIRMED:
                            cellValue = "已收货";
                            break;
                        case COMPLETED:
                            cellValue = "已完成";
                            break;
                        case VOID:
                            cellValue = "已作废";
                            break;
                        default:
                    }

                    cell.setCellValue(cellValue);
                }),
                new Column("付款状态", new SpelColumnRender<TradeVO>("tradeState.payState.getDescription()")),
                new Column("发货状态", new SpelColumnRender<TradeVO>("tradeState.deliverStatus.getDescription()")),
                new Column("发票类型", new SpelColumnRender<TradeVO>("invoice.type == 0 ? '普通发票' : invoice.type == 1 ?'增值税发票':'不需要发票' ")),
                new Column("开票项目", new SpelColumnRender<TradeVO>("invoice.projectName")),
                new Column("发票抬头", new SpelColumnRender<TradeVO>("invoice.type == 0 ? invoice.generalInvoice.title:" +
                        "invoice.specialInvoice.companyName")),
        };
        if (existSupplier) {
            List<Column> columnList = Stream.of(columns).collect(Collectors.toList());
            columnList.add(
                    new Column("商家", new SpelColumnRender<ReturnOrderVO>("supplier.supplierName"))
            );
            columns = columnList.toArray(new Column[0]);
        }
        excelHelper
                .addSheet(
                        "订单导出",
                        columns,
                        tradeList
                );
        excelHelper.write(outputStream);
    }
}
