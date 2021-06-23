package com.wanmi.sbc.goods.provider.impl.excel;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.provider.excel.GoodsSupplierExcelProvider;
import com.wanmi.sbc.goods.api.request.excel.GoodsSupplierExcelExportTemplateByStoreIdRequest;
import com.wanmi.sbc.goods.api.request.excel.GoodsSupplierExcelExportTemplateIEPByStoreIdRequest;
import com.wanmi.sbc.goods.api.response.excel.GoodsSupplierExcelExportTemplateIEPResponse;
import com.wanmi.sbc.goods.api.response.excel.GoodsSupplierExcelExportTemplateResponse;
import com.wanmi.sbc.goods.info.service.S2bGoodsExcelService;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * com.wanmi.sbc.goods.provider.impl.excel.SupplierGoodsExcelController
 *
 * @author lipeng
 * @dateTime 2018/11/7 下午3:19
 */
@RestController
@Validated
public class GoodsSupplierExcelController implements GoodsSupplierExcelProvider {

    @Autowired
    private S2bGoodsExcelService s2bGoodsExcelService;

    /**
     * 获取商家excel模板
     *
     * @param request {@link GoodsSupplierExcelExportTemplateByStoreIdRequest}
     * @return base64位文件流字符串 {@link GoodsSupplierExcelExportTemplateResponse}
     */
    @Override
    public BaseResponse<GoodsSupplierExcelExportTemplateResponse> supplierExportTemplate(
            @RequestBody @Valid GoodsSupplierExcelExportTemplateByStoreIdRequest request) {
        return BaseResponse.success(GoodsSupplierExcelExportTemplateResponse.builder()
                .file(s2bGoodsExcelService.exportTemplate(request.getStoreId(), NumberUtils.INTEGER_ZERO)).build());
    }

    @Override
    public BaseResponse<GoodsSupplierExcelExportTemplateIEPResponse> supplierExportTemplateIEP(
            @RequestBody @Valid GoodsSupplierExcelExportTemplateIEPByStoreIdRequest request) {
        return BaseResponse.success(GoodsSupplierExcelExportTemplateIEPResponse.builder()
                .file(s2bGoodsExcelService.exportTemplate(request.getStoreId(),NumberUtils.INTEGER_ONE)).build());
    }
}
