package com.wanmi.sbc.goods.api.provider.standard;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.standard.StandardImportGoodsRequest;
import com.wanmi.sbc.goods.api.response.standard.StandardImportGoodsResponse;
import com.wanmi.sbc.goods.api.response.standard.StandardImportStandardRequest;
import com.wanmi.sbc.goods.api.response.standard.StandardImportStandardResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * com.wanmi.sbc.goods.api.provider.standard.StandardImportProvider
 * 商品库服务
 * @author lipeng
 * @dateTime 2018/11/9 下午2:45
 */
@FeignClient(value = "${application.goods.name}", contextId = "StandardImportProvider")
public interface StandardImportProvider {

    /**
     * 商品库批量导入商品
     *
     * @param request 导入模板 {@link StandardImportGoodsRequest}
     * @return {@link StandardImportGoodsResponse}
     */
    @PostMapping("/goods/${application.goods.version}/standard/import-goods")
    BaseResponse<StandardImportGoodsResponse> importGoods(@RequestBody @Valid StandardImportGoodsRequest request);

    /**
     * 商品批量导入商品库
     *
     * @param request {@link StandardImportStandardRequest}
     * @return {@link StandardImportStandardResponse}
     */
    @PostMapping("/goods/${application.goods.version}/standard/import-standard")
    BaseResponse<StandardImportStandardResponse> importStandard(@RequestBody @Valid StandardImportStandardRequest request);
}
