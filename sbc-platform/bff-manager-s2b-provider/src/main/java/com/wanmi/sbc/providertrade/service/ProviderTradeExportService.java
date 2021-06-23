package com.wanmi.sbc.providertrade.service;

import com.wanmi.sbc.common.util.excel.Column;
import com.wanmi.sbc.common.util.excel.ExcelHelper;
import com.wanmi.sbc.common.util.excel.impl.SpelColumnRender;
import com.wanmi.sbc.goods.bean.enums.DeliverWay;
import com.wanmi.sbc.order.bean.enums.FlowState;
import com.wanmi.sbc.order.bean.vo.ProviderTradeExportVO;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.util.List;
import java.util.Objects;

/**
 * @Description: 供应商订单导出服务类
 * @Autho qiaokang
 * @Date：2020-03-29 10:41
 */
@Service
public class ProviderTradeExportService {

    /**
     * @param tradeList
     * @param outputStream
     */
    public void export(List<ProviderTradeExportVO> tradeList, OutputStream outputStream) {
        ExcelHelper excelHelper = new ExcelHelper();
        Column[] columns = {
                new Column("订单编号", new SpelColumnRender<ProviderTradeExportVO>("parentId")),
                new Column("子单号", new SpelColumnRender<ProviderTradeExportVO>("id")),
                new Column("下单时间", new SpelColumnRender<ProviderTradeExportVO>("createTime")),
                new Column("商家", new SpelColumnRender<ProviderTradeExportVO>("supplierInfo")),
                new Column("收货人", new SpelColumnRender<ProviderTradeExportVO>("consigneeName")),
                new Column("收货人手机", new SpelColumnRender<ProviderTradeExportVO>("consigneePhone")),
                new Column("收货人地址", new SpelColumnRender<ProviderTradeExportVO>("detailAddress")),
                new Column("配送方式", (cell, object) -> {
//                    ProviderTradeExportVO exportTrade = (ProviderTradeExportVO) object;
//                    if (Objects.nonNull(exportTrade.getDeliverWay())) {
//                        cell.setCellValue(exportTrade.getDeliverWay().getDesc());
//                    }
                    cell.setCellValue(DeliverWay.EXPRESS.getDesc());
                }),
                new Column("订单商品金额", new SpelColumnRender<ProviderTradeExportVO>("orderGoodsPrice")),
                new Column("商品名称", new SpelColumnRender<ProviderTradeExportVO>("skuName")),
                new Column("商品规格", new SpelColumnRender<ProviderTradeExportVO>("specDetails")),
                new Column("SKU编码", new SpelColumnRender<ProviderTradeExportVO>("skuNo")),
                new Column("商品种类", new SpelColumnRender<ProviderTradeExportVO>("goodsSpecies")),
                new Column("商品总数量", new SpelColumnRender<ProviderTradeExportVO>("totalNum")),
                new Column("购买数量", new SpelColumnRender<ProviderTradeExportVO>("num")),
                new Column("卖家备注", new SpelColumnRender<ProviderTradeExportVO>("buyerRemark")),
                new Column("订单状态", (cell, object) -> {
                    ProviderTradeExportVO exportTrade = (ProviderTradeExportVO) object;
                    FlowState flowState = exportTrade.getFlowState();
                    if (Objects.nonNull(flowState)) {
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
                    }
                }),
                new Column("发货状态", (cell, object) -> {
                    ProviderTradeExportVO exportTrade = (ProviderTradeExportVO) object;
                    if (Objects.nonNull(exportTrade.getDeliverStatus())) {
                        cell.setCellValue(exportTrade.getDeliverStatus().getDescription());
                    }
                }),
                new Column("付款状态", (cell, object) -> {
                    ProviderTradeExportVO exportTrade = (ProviderTradeExportVO) object;
                    if (Objects.nonNull(exportTrade.getPayState())) {
                        cell.setCellValue(exportTrade.getPayState().getDescription());
                    }
                }),

        };
//        if (existSupplier) {
//            List<Column> columnList = Stream.of(columns).collect(Collectors.toList());
//            columnList.add(
//                    new Column("商家", new SpelColumnRender<ReturnOrderVO>("supplier.supplierName"))
//            );
//            columns = columnList.toArray(new Column[0]);
//        }
        excelHelper.addSheet(
                        "订单导出",
                        columns,
                        tradeList
                );
        excelHelper.write(outputStream);
    }

}
