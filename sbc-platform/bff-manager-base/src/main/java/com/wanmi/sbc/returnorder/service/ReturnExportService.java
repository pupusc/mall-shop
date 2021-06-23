package com.wanmi.sbc.returnorder.service;

import com.wanmi.sbc.common.util.excel.Column;
import com.wanmi.sbc.common.util.excel.ExcelHelper;
import com.wanmi.sbc.common.util.excel.impl.SpelColumnRender;
import com.wanmi.sbc.order.bean.vo.ReturnItemVO;
import com.wanmi.sbc.order.bean.vo.ReturnOrderVO;
import com.wanmi.sbc.setting.api.provider.thirdplatformconfig.ThirdPlatformConfigQueryProvider;
import com.wanmi.sbc.setting.api.request.thirdplatformconfig.ThirdPlatformConfigByTypeRequest;
import com.wanmi.sbc.setting.api.response.thirdplatformconfig.ThirdPlatformConfigResponse;
import com.wanmi.sbc.setting.bean.enums.ConfigType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 退单导出service
 * Created by jinwei on 6/5/2017.
 */
@Service
public class ReturnExportService {

    @Autowired
    private ThirdPlatformConfigQueryProvider thirdPlatformConfigQueryProvider;

    public void export(List<ReturnOrderVO> returnOrderList, OutputStream outputStream, boolean existSupplier) {
        // 第三方渠道设置
        ThirdPlatformConfigResponse config = thirdPlatformConfigQueryProvider.get(
                ThirdPlatformConfigByTypeRequest.builder().configType(ConfigType.THIRD_PLATFORM_LINKED_MALL.toValue()).build()).getContext();

        ExcelHelper excelHelper = new ExcelHelper();
        List<Column> columnList = new ArrayList<>();
        if(existSupplier){
            columnList.add(new Column("退单编号", new SpelColumnRender<ReturnOrderVO>("id")));
            columnList.add(new Column("申请时间", new SpelColumnRender<ReturnOrderVO>("createTime")));
            columnList.add(new Column("订单编号", new SpelColumnRender<ReturnOrderVO>("tid")));
            columnList.add(new Column("子单编号", new SpelColumnRender<ReturnOrderVO>("ptid")));

            // LM相关字段
            if (Objects.nonNull(config)) {
                columnList.add(new Column("LinkedMall订单号", new SpelColumnRender<ReturnOrderVO>(
                        "thirdPlatformOrderId")));
                columnList.add(new Column("淘宝订单号", new SpelColumnRender<ReturnOrderVO>("outOrderId")));
            }

            columnList.add(new Column("商家", (cell,object)->{
                ReturnOrderVO returnOrderVO=(ReturnOrderVO) object;
                String supplierName=returnOrderVO.getCompany().getSupplierName();
                String supplierCode=returnOrderVO.getCompany().getCompanyCode();
                cell.setCellValue(supplierName+" "+supplierCode);
            }));
            columnList.add(new Column("供应商", (cell,object)->{
                ReturnOrderVO returnOrderVO=(ReturnOrderVO) object;
                String providerName=returnOrderVO.getProviderName()!=null?returnOrderVO.getProviderName():StringUtils.EMPTY;
                String providerCode=returnOrderVO.getProviderCode()!=null?returnOrderVO.getProviderCode():StringUtils.EMPTY;
                cell.setCellValue(providerName+" "+providerCode);
            }));
            columnList.add(new Column("客户名称", new SpelColumnRender<ReturnOrderVO>("buyer.name")));
            columnList.add(new Column("客户账号", new SpelColumnRender<ReturnOrderVO>("buyer.account")));
            columnList.add(new Column("客户级别", new SpelColumnRender<ReturnOrderVO>("buyer.levelName")));
            columnList.add(new Column("退货原因", new SpelColumnRender<ReturnOrderVO>("returnReason.getDesc()")));

            // LM相关字段
            if (Objects.nonNull(config)) {
                columnList.add(new Column("LM退单原因", new SpelColumnRender<ReturnOrderVO>("thirdReasonTips")));
            }

            columnList.add(new Column("退货说明", new SpelColumnRender<ReturnOrderVO>("description")));
            columnList.add(new Column("退货方式", new SpelColumnRender<ReturnOrderVO>("returnWay.getDesc()")));
            columnList.add(new Column("退货商品SKU编码", (cell, object) -> {
                String skuNos = ((ReturnOrderVO) object)
                        .getReturnItems()
                        .stream()
                        .map(ReturnItemVO::getSkuNo)
                        .collect(Collectors.joining(";"));
                cell.setCellValue(skuNos);
            }));
            columnList.add(new Column("退货商品种类", new SpelColumnRender<ReturnOrderVO>("returnItems.size()")));
            columnList.add(new Column("退货商品总数量", (cell, object) -> {
                Optional<Integer> size = ((ReturnOrderVO) object)
                        .getReturnItems()
                        .stream()
                        .map(ReturnItemVO::getNum)
                        .reduce((sum, item) -> {
                            sum += item;
                            return sum;
                        });
                cell.setCellValue((double) size.get());
            }));
            columnList.add(new Column("应退金额", new SpelColumnRender<ReturnOrderVO>("returnPrice.totalPrice")));
            columnList.add(new Column("实退金额", new SpelColumnRender<ReturnOrderVO>("null != returnPrice.actualReturnPrice ? returnPrice.actualReturnPrice : \"\"")));
            columnList.add(new Column("退单状态", new SpelColumnRender<ReturnOrderVO>("transformReturnFlowState(returnFlowState)")));

//            Column[] columns = {
//                    new Column("退单编号", new SpelColumnRender<ReturnOrderVO>("id")),
//                    new Column("申请时间", new SpelColumnRender<ReturnOrderVO>("createTime")),
//                    new Column("订单编号", new SpelColumnRender<ReturnOrderVO>("tid")),
//                    new Column("子单编号", new SpelColumnRender<ReturnOrderVO>("ptid")),
//                    new Column("商家", (cell,object)->{
//                        ReturnOrderVO returnOrderVO=(ReturnOrderVO) object;
//                        String supplierName=returnOrderVO.getCompany().getSupplierName();
//                        String supplierCode=returnOrderVO.getCompany().getCompanyCode();
//                        cell.setCellValue(supplierName+" "+supplierCode);
//                    }),
//                    new Column("供应商", (cell,object)->{
//                        ReturnOrderVO returnOrderVO=(ReturnOrderVO) object;
//                        String providerName=returnOrderVO.getProviderName()!=null?returnOrderVO.getProviderName():StringUtils.EMPTY;
//                        String providerCode=returnOrderVO.getProviderCode()!=null?returnOrderVO.getProviderCode():StringUtils.EMPTY;
//                        cell.setCellValue(providerName+" "+providerCode);
//                    }),
//                    new Column("客户名称", new SpelColumnRender<ReturnOrderVO>("buyer.name")),
//                    new Column("客户账号", new SpelColumnRender<ReturnOrderVO>("buyer.account")),
//                    new Column("客户级别", new SpelColumnRender<ReturnOrderVO>("buyer.levelName")),
//                    new Column("退货原因", new SpelColumnRender<ReturnOrderVO>("returnReason.getDesc()")),
//                    new Column("退货说明", new SpelColumnRender<ReturnOrderVO>("description")),
//                    new Column("退货方式", new SpelColumnRender<ReturnOrderVO>("returnWay.getDesc()")),
//                    new Column("退货商品SKU编码", (cell, object) -> {
//                        String skuNos = ((ReturnOrderVO) object)
//                                .getReturnItems()
//                                .stream()
//                                .map(ReturnItemVO::getSkuNo)
//                                .collect(Collectors.joining(";"));
//                        cell.setCellValue(skuNos);
//                    }),
//                    new Column("退货商品种类", new SpelColumnRender<ReturnOrderVO>("returnItems.size()")),
//                    new Column("退货商品总数量", (cell, object) -> {
//                        Optional<Integer> size = ((ReturnOrderVO) object)
//                                .getReturnItems()
//                                .stream()
//                                .map(ReturnItemVO::getNum)
//                                .reduce((sum, item) -> {
//                                    sum += item;
//                                    return sum;
//                                });
//                        cell.setCellValue((double) size.get());
//                    }),
//    //                new Column("退货商品总额", new SpelColumnRender<ReturnOrderVO>("returnPrice.applyStatus ? returnPrice.actualReturnPrice : returnPrice.totalPrice")),
//    //                new Column("申请退款金额", new SpelColumnRender<ReturnOrderVO>("returnPrice.applyStatus ? returnPrice.applyPrice: \"\"")),
//                    new Column("应退金额", new SpelColumnRender<ReturnOrderVO>("returnPrice.totalPrice")),
//                    new Column("实退金额", new SpelColumnRender<ReturnOrderVO>("null != returnPrice.actualReturnPrice ? returnPrice.actualReturnPrice : \"\"")),
//                    new Column("退单状态", new SpelColumnRender<ReturnOrderVO>("transformReturnFlowState(returnFlowState)"))
//            };

            Column[] columns = columnList.toArray(new Column[0]);

            excelHelper.addSheet(
                    "退单",
                    columns,
                    returnOrderList
            );
        }else {
            columnList.add(new Column("退单编号", new SpelColumnRender<ReturnOrderVO>("id")));
            columnList.add(new Column("申请时间", new SpelColumnRender<ReturnOrderVO>("createTime")));
            columnList.add(new Column("订单编号", new SpelColumnRender<ReturnOrderVO>("tid")));
            columnList.add(new Column("子单编号", new SpelColumnRender<ReturnOrderVO>("ptid")));

            // LM相关字段
            if (Objects.nonNull(config)) {
                columnList.add(new Column("LinkedMall订单号", new SpelColumnRender<ReturnOrderVO>(
                        "thirdPlatformOrderId")));
                columnList.add(new Column("淘宝订单号", new SpelColumnRender<ReturnOrderVO>("outOrderId")));
            }

            columnList.add(new Column("供应商", (cell,object)->{
                ReturnOrderVO returnOrderVO=(ReturnOrderVO) object;
                String providerName=returnOrderVO.getProviderName()==null?"":returnOrderVO.getProviderName();
                String providerCode=returnOrderVO.getProviderCode()==null?"":returnOrderVO.getProviderCode();
                cell.setCellValue(providerName+" "+providerCode);
            }));
            columnList.add(new Column("客户名称", new SpelColumnRender<ReturnOrderVO>("buyer.name")));
            columnList.add(new Column("客户账号", new SpelColumnRender<ReturnOrderVO>("buyer.account")));
            columnList.add(new Column("客户级别", new SpelColumnRender<ReturnOrderVO>("buyer.levelName")));
            columnList.add(new Column("退货原因", new SpelColumnRender<ReturnOrderVO>("returnReason.getDesc()")));

            // LM相关字段
            if (Objects.nonNull(config)) {
                columnList.add(new Column("LM退单原因", new SpelColumnRender<ReturnOrderVO>("thirdReasonTips")));
            }

            columnList.add(new Column("退货说明", new SpelColumnRender<ReturnOrderVO>("description")));
            columnList.add(new Column("退货方式", new SpelColumnRender<ReturnOrderVO>("returnWay.getDesc()")));
            columnList.add(new Column("退货商品SKU编码", (cell, object) -> {
                String skuNos = ((ReturnOrderVO) object)
                        .getReturnItems()
                        .stream()
                        .map(ReturnItemVO::getSkuNo)
                        .collect(Collectors.joining(";"));
                cell.setCellValue(skuNos);
            }));
            columnList.add(new Column("退货商品种类", new SpelColumnRender<ReturnOrderVO>("returnItems.size()")));
            columnList.add(new Column("退货商品总数量", (cell, object) -> {
                Optional<Integer> size = ((ReturnOrderVO) object)
                        .getReturnItems()
                        .stream()
                        .map(ReturnItemVO::getNum)
                        .reduce((sum, item) -> {
                            sum += item;
                            return sum;
                        });
                cell.setCellValue((double) size.get());
            }));
            columnList.add(new Column("应退金额", new SpelColumnRender<ReturnOrderVO>("returnPrice.totalPrice")));
            columnList.add(new Column("实退金额", new SpelColumnRender<ReturnOrderVO>("null != returnPrice.actualReturnPrice ? returnPrice.actualReturnPrice : \"\"")));
            columnList.add(new Column("退单状态", new SpelColumnRender<ReturnOrderVO>("transformReturnFlowState(returnFlowState)")));

//            Column[] columns = {
//                    new Column("退单编号", new SpelColumnRender<ReturnOrderVO>("id")),
//                    new Column("申请时间", new SpelColumnRender<ReturnOrderVO>("createTime")),
//                    new Column("订单编号", new SpelColumnRender<ReturnOrderVO>("tid")),
//                    new Column("子单编号", new SpelColumnRender<ReturnOrderVO>("ptid")),
//                    new Column("供应商", (cell,object)->{
//                        ReturnOrderVO returnOrderVO=(ReturnOrderVO) object;
//                        String providerName=returnOrderVO.getProviderName()==null?"":returnOrderVO.getProviderName();
//                        String providerCode=returnOrderVO.getProviderCode()==null?"":returnOrderVO.getProviderCode();
//                        cell.setCellValue(providerName+" "+providerCode);
//                    }),
//                    new Column("客户名称", new SpelColumnRender<ReturnOrderVO>("buyer.name")),
//                    new Column("客户账号", new SpelColumnRender<ReturnOrderVO>("buyer.account")),
//                    new Column("客户级别", new SpelColumnRender<ReturnOrderVO>("buyer.levelName")),
//                    new Column("退货原因", new SpelColumnRender<ReturnOrderVO>("returnReason.getDesc()")),
//                    new Column("退货说明", new SpelColumnRender<ReturnOrderVO>("description")),
//                    new Column("退货方式", new SpelColumnRender<ReturnOrderVO>("returnWay.getDesc()")),
//                    new Column("退货商品SKU编码", (cell, object) -> {
//                        String skuNos = ((ReturnOrderVO) object)
//                                .getReturnItems()
//                                .stream()
//                                .map(ReturnItemVO::getSkuNo)
//                                .collect(Collectors.joining(";"));
//                        cell.setCellValue(skuNos);
//                    }),
//                    new Column("退货商品种类", new SpelColumnRender<ReturnOrderVO>("returnItems.size()")),
//                    new Column("退货商品总数量", (cell, object) -> {
//                        Optional<Integer> size = ((ReturnOrderVO) object)
//                                .getReturnItems()
//                                .stream()
//                                .map(ReturnItemVO::getNum)
//                                .reduce((sum, item) -> {
//                                    sum += item;
//                                    return sum;
//                                });
//                        cell.setCellValue((double) size.get());
//                    }),
//                    //                new Column("退货商品总额", new SpelColumnRender<ReturnOrderVO>("returnPrice.applyStatus ? returnPrice.actualReturnPrice : returnPrice.totalPrice")),
//                    //                new Column("申请退款金额", new SpelColumnRender<ReturnOrderVO>("returnPrice.applyStatus ? returnPrice.applyPrice: \"\"")),
//                    new Column("应退金额", new SpelColumnRender<ReturnOrderVO>("returnPrice.totalPrice")),
//                    new Column("实退金额", new SpelColumnRender<ReturnOrderVO>("null != returnPrice.actualReturnPrice ? returnPrice.actualReturnPrice : \"\"")),
//                    new Column("退单状态", new SpelColumnRender<ReturnOrderVO>("transformReturnFlowState(returnFlowState)"))
//            };

            Column[] columns = columnList.toArray(new Column[0]);

            excelHelper.addSheet(
                    "退单",
                    columns,
                    returnOrderList
            );

        }


        excelHelper.write(outputStream);
    }

}
