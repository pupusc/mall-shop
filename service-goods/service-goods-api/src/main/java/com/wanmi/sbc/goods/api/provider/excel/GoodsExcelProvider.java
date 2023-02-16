package com.wanmi.sbc.goods.api.provider.excel;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.response.excel.GoodsExcelExportTemplateResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * com.wanmi.sbc.goods.api.provider.goodsexcel.GoodsExcelProvider
 * 商品excel操作接口，对应改造之前的GoodsExcelService
 * @author lipeng
 * @dateTime 2018/11/6 上午11:14
 */
@FeignClient(value = "${application.goods.name}", contextId = "GoodsExcelProvider")
public interface GoodsExcelProvider {

    /**
     * 获取商品excel模板
     * @return base64位文件流字符串 {@link GoodsExcelExportTemplateResponse}
     */
    @PostMapping("/goods/${application.goods.version}/excel/export-template")
    BaseResponse<GoodsExcelExportTemplateResponse> exportTemplate();

    /**
     * 商品excel导入商品到数据可
     */
    @PostMapping("/goods/${application.goods.version}/excel/load-excel")
    BaseResponse loadExcel(@RequestParam(value = "file") MultipartFile file, @RequestParam(value = "topicStoreId") Integer topicStoreyId,@RequestParam(value = "topicStoreySearchId") Integer topicStoreySearchId);
}
