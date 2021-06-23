package com.wanmi.sbc.goods.provider.impl.standard;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.standard.StandardExcelProvider;
import com.wanmi.sbc.goods.api.request.standard.StandardGoodsBatchAddRequest;
import com.wanmi.sbc.goods.api.response.standard.StandardExcelExportTemplateResponse;
import com.wanmi.sbc.goods.api.response.standard.StandardGoodsBatchAddResponse;
import com.wanmi.sbc.goods.info.request.GoodsRequest;
import com.wanmi.sbc.goods.standard.service.StandardExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2018-11-08 15:29
 */
@Validated
@RestController
public class StandardExcelController implements StandardExcelProvider {

    @Autowired
    private StandardExcelService standardExcelService;

    /**
     * 批量新增商品库
     * @param request 商品库批量数据请求结构 {@link StandardGoodsBatchAddRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @Override
    public BaseResponse<StandardGoodsBatchAddResponse> batchAdd(@RequestBody @Valid StandardGoodsBatchAddRequest request) {
        List<String> standardIds = standardExcelService.batchAdd(request);
        return BaseResponse.success(StandardGoodsBatchAddResponse.builder().standardIds(standardIds).build());
    }

    /**
     * 导出商品库模板
     * 加载xls已有模板，填充商品分类、品牌数据，实现excel下拉列表
     * @return base64位文件流字符串 {@link StandardExcelExportTemplateResponse}
     */
    @Override
    public BaseResponse<StandardExcelExportTemplateResponse> exportTemplate() {
        return BaseResponse.success(StandardExcelExportTemplateResponse.builder()
                .file(standardExcelService.exportTemplate()).build());
    }
}
