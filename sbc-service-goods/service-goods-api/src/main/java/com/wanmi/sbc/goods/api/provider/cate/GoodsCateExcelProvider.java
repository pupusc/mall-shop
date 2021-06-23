package com.wanmi.sbc.goods.api.provider.cate;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.cate.GoodsCateExcelImportRequest;
import com.wanmi.sbc.goods.api.response.cate.GoodsCateExcelImportResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Author: songhanlin
 * @Date: Created In 10:11 2018-12-18
 * @Description: 商品分类excel导出
 */
@FeignClient(value = "${application.goods.name}", contextId = "GoodsCateExcelProvider")
public interface GoodsCateExcelProvider {

    /**
     * 导入商品分类
     *
     * @param request 请求对象  {@link GoodsCateExcelImportRequest}
     * @return 返回实体 {@link GoodsCateExcelImportResponse}
     */
    @PostMapping("/goods/${application.goods.version}/cate/excel/import")
    BaseResponse<GoodsCateExcelImportResponse> importGoodsCate(@RequestBody GoodsCateExcelImportRequest request);
}
