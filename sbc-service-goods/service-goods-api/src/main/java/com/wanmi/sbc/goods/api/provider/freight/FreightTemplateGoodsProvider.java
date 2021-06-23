package com.wanmi.sbc.goods.api.provider.freight;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.freight.FreightTemplateGoodsCopyByIdAndStoreIdRequest;
import com.wanmi.sbc.goods.api.request.freight.FreightTemplateGoodsDeleteByIdAndStoreIdRequest;
import com.wanmi.sbc.goods.api.request.freight.FreightTemplateGoodsInitByStoreIdRequest;
import com.wanmi.sbc.goods.api.request.freight.FreightTemplateGoodsSaveRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>对单品运费模板操作接口</p>
 * Created by daiyitian on 2018-10-31-下午6:23.
 */
@FeignClient(value = "${application.goods.name}", contextId = "FreightTemplateGoodsProvider")
public interface FreightTemplateGoodsProvider {

    /**
     * 新增/更新单品运费模板
     *
     * @param request 保存单品运费模板数据结构 {@link FreightTemplateGoodsSaveRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/goods/${application.goods.version}/freight/goods/save")
    BaseResponse save(@RequestBody @Valid FreightTemplateGoodsSaveRequest request);

    /**
     * 根据单品运费模板id和店铺id删除单品运费模板
     *
     * @param request 删除单品运费模板数据结构 {@link FreightTemplateGoodsDeleteByIdAndStoreIdRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/goods/${application.goods.version}/freight/goods/delete-by-id-and-store-id")
    BaseResponse deleteByIdAndStoreId(@RequestBody @Valid FreightTemplateGoodsDeleteByIdAndStoreIdRequest request);

    /**
     * 根据单品运费模板id和店铺id复制单品运费模板
     *
     * @param request 复制单品运费模板数据结构 {@link FreightTemplateGoodsCopyByIdAndStoreIdRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/goods/${application.goods.version}/freight/goods/copy-by-id-and-store-id")
    BaseResponse copyByIdAndStoreId(@RequestBody @Valid FreightTemplateGoodsCopyByIdAndStoreIdRequest request);

    /**
     * 初始单品运费模板
     *
     * @param request 初始单品运费模板数据结构 {@link FreightTemplateGoodsInitByStoreIdRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/goods/${application.goods.version}/freight/goods/init-by-store-id")
    BaseResponse initByStoreId(@RequestBody @Valid FreightTemplateGoodsInitByStoreIdRequest request);

}
