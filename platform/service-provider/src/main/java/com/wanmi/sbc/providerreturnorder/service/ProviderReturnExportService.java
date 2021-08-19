package com.wanmi.sbc.providerreturnorder.service;

import com.wanmi.sbc.common.util.excel.Column;
import com.wanmi.sbc.common.util.excel.ExcelHelper;
import com.wanmi.sbc.common.util.excel.impl.SpelColumnRender;
import com.wanmi.sbc.order.bean.vo.CompanyVO;
import com.wanmi.sbc.order.bean.vo.ReturnItemVO;
import com.wanmi.sbc.order.bean.vo.ReturnOrderVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Description: 供应商退单导出服务层
 * @Autho qiaokang
 * @Date：2020-03-29 17:41
 */
@Service
public class ProviderReturnExportService {

    /**
     * 导出供应商退单信息
     * @param returnOrderList
     * @param outputStream
     */
    public void export(List<ReturnOrderVO> returnOrderList, OutputStream outputStream) {
        ExcelHelper excelHelper = new ExcelHelper();
        Column[] columns = {
                new Column("退单编号", new SpelColumnRender<ReturnOrderVO>("id")),
                new Column("申请时间", new SpelColumnRender<ReturnOrderVO>("createTime")),
                new Column("订单编号", new SpelColumnRender<ReturnOrderVO>("tid")),
                new Column("子单编号", new SpelColumnRender<ReturnOrderVO>("ptid")),
                new Column("代销商家", (cell, object) -> {
                    ReturnOrderVO returnOrderVO = ((ReturnOrderVO) object);
                    CompanyVO companyVO = returnOrderVO.getCompany();
                    String supplierName = StringUtils.isNotEmpty(companyVO.getSupplierName()) ?
                            companyVO.getSupplierName() : "";
                    String supplierCode = StringUtils.isNotEmpty(companyVO.getCompanyCode()) ?
                            companyVO.getCompanyCode() : "";
                    cell.setCellValue(supplierName + "  " + supplierCode);
                }),
                new Column("退货原因", new SpelColumnRender<ReturnOrderVO>("returnReason.getDesc()")),
                new Column("退货说明", new SpelColumnRender<ReturnOrderVO>("description")),
                new Column("退货方式", new SpelColumnRender<ReturnOrderVO>("returnWay.getDesc()")),
                new Column("退货商品SKU编码", (cell, object) -> {
                    String skuNos = ((ReturnOrderVO) object)
                            .getReturnItems()
                            .stream()
                            .map(ReturnItemVO::getSkuNo)
                            .collect(Collectors.joining(";"));
                    cell.setCellValue(skuNos);
                }),
                new Column("退货商品种类", new SpelColumnRender<ReturnOrderVO>("returnItems.size()")),
                new Column("退货商品总数量", (cell, object) -> {
                    Optional<Integer> size = ((ReturnOrderVO) object)
                            .getReturnItems()
                            .stream()
                            .map(ReturnItemVO::getNum)
                            .reduce((sum, item) -> {
                                sum += item;
                                return sum;
                            });
                    cell.setCellValue((double) size.get());
                }),
                new Column("应退金额", new SpelColumnRender<ReturnOrderVO>("returnPrice.providerTotalPrice")),
                new Column("退单状态", new SpelColumnRender<ReturnOrderVO>("transformReturnFlowState(returnFlowState)"))
        };

        excelHelper.addSheet(
                "退单",
                columns,
                returnOrderList
        );
        excelHelper.write(outputStream);
    }
}
