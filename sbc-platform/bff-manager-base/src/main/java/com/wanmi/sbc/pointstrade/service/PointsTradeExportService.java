package com.wanmi.sbc.pointstrade.service;

import com.wanmi.sbc.common.enums.PointsOrderType;
import com.wanmi.sbc.common.util.excel.Column;
import com.wanmi.sbc.common.util.excel.ExcelHelper;
import com.wanmi.sbc.common.util.excel.impl.SpelColumnRender;
import com.wanmi.sbc.order.bean.enums.FlowState;
import com.wanmi.sbc.order.bean.vo.PointsTradeVO;
import com.wanmi.sbc.order.bean.vo.ReturnOrderVO;
import com.wanmi.sbc.order.bean.vo.TradeItemVO;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @ClassName PointsTradeExportService
 * @Description 积分订单导出
 * @Author lvzhenwei
 * @Date 2019/5/10 15:14
 **/
@Service
public class PointsTradeExportService {

    public void export(List<PointsTradeVO> pointsTradeVOList, OutputStream outputStream, boolean existSupplier) {
        ExcelHelper excelHelper = new ExcelHelper();
        Column[] columns = {
                new Column("订单编号", new SpelColumnRender<PointsTradeVO>("id")),
                new Column("下单时间", new SpelColumnRender<PointsTradeVO>("tradeState.createTime")),
                new Column("客户名称", new SpelColumnRender<PointsTradeVO>("buyer.name")),
                new Column("客户账号", new SpelColumnRender<PointsTradeVO>("buyer.account")),
                new Column("客户级别", new SpelColumnRender<PointsTradeVO>("buyer.levelName")),
                new Column("收货人", (cell, object)->{
                    PointsTradeVO trade = (PointsTradeVO) object;
                    if(trade.getPointsOrderType() == PointsOrderType.POINTS_COUPON){
                        cell.setCellValue(trade.getBuyer().getName());
                    }else{
                        cell.setCellValue(trade.getConsignee().getName());
                    }
                }),
                new Column("收货人手机", (cell, object)->{
                    PointsTradeVO trade = (PointsTradeVO) object;
                    if(trade.getPointsOrderType() == PointsOrderType.POINTS_COUPON){
                        cell.setCellValue(trade.getBuyer().getPhone());
                    }else{
                        cell.setCellValue(trade.getConsignee().getPhone());
                    }
                }),
                new Column("收货人地址", new SpelColumnRender<PointsTradeVO>("consignee.detailAddress")),
                new Column("支付方式", new SpelColumnRender<PointsTradeVO>("payInfo.desc")),
                new Column("配送方式", new SpelColumnRender<PointsTradeVO>("deliverWay.getDesc()")),
                new Column("订单积分", new SpelColumnRender<PointsTradeVO>("tradePrice.points")),
                new Column("商品SKU", (cell, object) -> {
                    PointsTradeVO trade = (PointsTradeVO) object;
                    StringBuilder sb = new StringBuilder(trade.getTradeItems().size() * 32);
                    trade
                            .getTradeItems()
                            .forEach(i -> {
                                sb.append(i.getSkuNo()).append(",");
                            });
                    sb.trimToSize();
                    cell.setCellValue(sb.length() > 0 ? sb.substring(0, sb.length() - 1) : "");
                }),
                new Column("商品种类", (cell, object) -> {
                    PointsTradeVO trade = (PointsTradeVO) object;
                    cell.setCellValue(trade.getPointsOrderType() != PointsOrderType.POINTS_COUPON ? trade.skuItemMap().size() : 1);
                }),
                new Column("商品总数量", (cell, object) -> {
                    Optional<Long> size = ((PointsTradeVO) object)
                            .getTradeItems()
                            .stream()
                            .map(TradeItemVO::getNum)
                            .reduce((sum, item) -> {
                                sum += item;
                                return sum;
                            });
                    cell.setCellValue(size.isPresent() ? (double) size.get() : 1);
                }),
                new Column("买家备注", new SpelColumnRender<PointsTradeVO>("buyerRemark")),
                new Column("卖家备注", new SpelColumnRender<PointsTradeVO>("sellerRemark")),
                new Column("订单状态", (cell, object) -> {
                    PointsTradeVO trade = (PointsTradeVO) object;
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
                new Column("付款状态", new SpelColumnRender<PointsTradeVO>("tradeState.payState.getDescription()")),
                new Column("发货状态", new SpelColumnRender<PointsTradeVO>("tradeState.deliverStatus.getDescription()")),
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
                        "积分订单导出",
                        columns,
                        pointsTradeVOList
                );
        excelHelper.write(outputStream);
    }
}
