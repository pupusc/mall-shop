package com.wanmi.sbc.goods.provider.impl.cate;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.cate.GoodsCateExcelProvider;
import com.wanmi.sbc.goods.api.request.cate.GoodsCateExcelImportRequest;
import com.wanmi.sbc.goods.api.response.cate.GoodsCateExcelImportResponse;
import com.wanmi.sbc.goods.cate.model.root.GoodsCate;
import com.wanmi.sbc.goods.cate.service.GoodsCateExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: songhanlin
 * @Date: Created In 10:19 2018-12-18
 * @Description: TODO
 */
@RestController
@Validated
public class GoodsCateExcelController implements GoodsCateExcelProvider {

    @Autowired
    private GoodsCateExcelService goodsCateExcelService;

    /**
     * 导入商品分类
     *
     * @param request {@link GoodsCateExcelImportRequest}
     * @return 返回 {@link GoodsCateExcelImportResponse}
     */
    @Override
    public BaseResponse<GoodsCateExcelImportResponse> importGoodsCate(@RequestBody GoodsCateExcelImportRequest request) {
        GoodsCateExcelImportResponse response = new GoodsCateExcelImportResponse();
        List<GoodsCate> goodsCates = KsBeanUtil.convertList(request.getGoodsCateList(), GoodsCate.class);
        response.setFlag(goodsCateExcelService.importGoodsCate(goodsCates));
        return BaseResponse.success(response);
    }
}
