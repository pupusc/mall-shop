package com.wanmi.sbc.goods.api.provider.standard;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.standard.StandardGoodsBatchAddRequest;
import com.wanmi.sbc.goods.api.response.standard.StandardExcelExportTemplateResponse;
import com.wanmi.sbc.goods.api.response.standard.StandardGoodsBatchAddResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>商品库导入导出操作接口</p>
 * author: sunkun
 * Date: 2018-11-07
 */
@FeignClient(value = "${application.goods.name}", contextId = "StandardExcelProvider")
public interface StandardExcelProvider {

    /**
     * 批量新增商品库
     * @param request 商品库批量数据请求结构 {@link StandardGoodsBatchAddRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/goods/${application.goods.version}/standard/excel/batch-add")
    BaseResponse<StandardGoodsBatchAddResponse> batchAdd(@RequestBody @Valid StandardGoodsBatchAddRequest request);

    /**
     * 导出商品库模板
     * 加载xls已有模板，填充商品分类、品牌数据，实现excel下拉列表
     * @return base64位文件流字符串 {@link StandardExcelExportTemplateResponse}
     */
    @PostMapping("/goods/${application.goods.version}/standard/export-template")
    BaseResponse<StandardExcelExportTemplateResponse> exportTemplate();
}
