package com.wanmi.sbc.goods.provider.impl.standard;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.standard.StandardImportProvider;
import com.wanmi.sbc.goods.api.request.standard.StandardImportGoodsRequest;
import com.wanmi.sbc.goods.api.response.standard.StandardImportGoodsResponse;
import com.wanmi.sbc.goods.api.response.standard.StandardImportStandardRequest;
import com.wanmi.sbc.goods.api.response.standard.StandardImportStandardResponse;
import com.wanmi.sbc.goods.info.request.GoodsRequest;
import com.wanmi.sbc.goods.standard.request.StandardImportRequest;
import com.wanmi.sbc.goods.standard.service.StandardImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * com.wanmi.sbc.goods.provider.impl.standard.StandardImportController
 *
 * @author lipeng
 * @dateTime 2018/11/9 下午2:55
 */
@RestController
@Validated
public class StandardImportController implements StandardImportProvider {

    @Autowired
    private StandardImportService standardImportService;

    /**
     * 商品库批量导入商品
     *
     * @param request 导入模板 {@link StandardImportGoodsRequest}
     * @return {@link StandardImportGoodsResponse}
     */
    @Override
    
    public BaseResponse<StandardImportGoodsResponse> importGoods(
            @RequestBody @Valid StandardImportGoodsRequest request) {
        StandardImportRequest standardImportRequest = KsBeanUtil.convert(request, StandardImportRequest.class);
        List<String> skuIdList = standardImportService.importGoods(standardImportRequest);

        StandardImportGoodsResponse response = new StandardImportGoodsResponse();
        response.setSkuIdList(skuIdList);
        return BaseResponse.success(response);
    }

    /**
     * 商品批量导入商品库
     *
     * @param request {@link StandardImportStandardRequest}
     * @return {@link BaseResponse}
     */
    @Override
    public BaseResponse<StandardImportStandardResponse> importStandard(@RequestBody @Valid StandardImportStandardRequest request) {
        GoodsRequest goodsRequest = KsBeanUtil.convert(request, GoodsRequest.class);
        StandardImportStandardResponse response = new StandardImportStandardResponse();
        response.setStandardIds(standardImportService.importStandard(goodsRequest));
        return BaseResponse.success(response);
    }
}
