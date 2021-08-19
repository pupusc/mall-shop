package com.wanmi.sbc.goods.api.provider.adjustprice;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.adjustprice.PriceAdjustmentTemplateExportRequest;
import com.wanmi.sbc.goods.api.response.adjustprice.ExportAdjustmentPriceTemplateResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>商品批量改价导入接口</p>
 * Created by of628 on 2020-12-11-6:51 下午.
 */
@FeignClient(value = "${application.goods.name}", contextId = "PriceAdjustmentImportProvider")
public interface PriceAdjustmentImportProvider {

    /**
     *
     * 下载批量调价excel模板
     * @param request 包含调价类型的请求参数 {@link PriceAdjustmentTemplateExportRequest}
     * @return: base64位文件输出流字符串 {@link ExportAdjustmentPriceTemplateResponse}
     *
     */
    @PostMapping("/goods/${application.goods.version}/adjust-price/marketing-price/template")
    BaseResponse<ExportAdjustmentPriceTemplateResponse> exportAjustmentPriceTemplate(@RequestBody @Valid PriceAdjustmentTemplateExportRequest request);

}
