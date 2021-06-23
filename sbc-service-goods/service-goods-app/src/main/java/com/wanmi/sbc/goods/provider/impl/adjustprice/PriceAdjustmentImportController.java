package com.wanmi.sbc.goods.provider.impl.adjustprice;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.adjustprice.PriceAdjustmentImportService;
import com.wanmi.sbc.goods.api.provider.adjustprice.PriceAdjustmentImportProvider;
import com.wanmi.sbc.goods.api.request.adjustprice.PriceAdjustmentTemplateExportRequest;
import com.wanmi.sbc.goods.api.response.adjustprice.ExportAdjustmentPriceTemplateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>商品批量改价导入操作实现</p>
 * Created by of628-wenzhi on 2020-12-11-2:11 下午.
 */
@RestController
@Validated
public class PriceAdjustmentImportController implements PriceAdjustmentImportProvider {
    @Autowired
    private PriceAdjustmentImportService priceAdjustmentImportService;

    @Override
    public BaseResponse<ExportAdjustmentPriceTemplateResponse> exportAjustmentPriceTemplate(@RequestBody @Valid PriceAdjustmentTemplateExportRequest request) {
        return BaseResponse.success(new ExportAdjustmentPriceTemplateResponse(priceAdjustmentImportService.
                exportAdjustmentPriceTemplate(request.getPriceAdjustmentType(),request.getStoreId())));
    }

}
