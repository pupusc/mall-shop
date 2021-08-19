package com.wanmi.sbc.goods.provider.impl.freight;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.provider.freight.FreightTemplateGoodsProvider;
import com.wanmi.sbc.goods.api.request.freight.FreightTemplateGoodsCopyByIdAndStoreIdRequest;
import com.wanmi.sbc.goods.api.request.freight.FreightTemplateGoodsDeleteByIdAndStoreIdRequest;
import com.wanmi.sbc.goods.api.request.freight.FreightTemplateGoodsInitByStoreIdRequest;
import com.wanmi.sbc.goods.api.request.freight.FreightTemplateGoodsSaveRequest;
import com.wanmi.sbc.goods.freight.service.FreightTemplateGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>对单品运费模板操作接口</p>
 * Created by daiyitian on 2018-10-31-下午6:23.
 */
@RestController
@Validated
public class FreightTemplateGoodsController implements FreightTemplateGoodsProvider {

    @Autowired
    private FreightTemplateGoodsService freightTemplateGoodsService;

    /**
     * 新增/更新单品运费模板
     *
     * @param request 保存单品运费模板数据结构 {@link FreightTemplateGoodsSaveRequest}
     * @return {@link BaseResponse}
     */
    @Override

    public BaseResponse save(@RequestBody @Valid FreightTemplateGoodsSaveRequest request){
        freightTemplateGoodsService.renewalFreightTemplateGoods(request);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 根据单品运费模板id和店铺id删除单品运费模板
     *
     * @param request 删除单品运费模板数据结构 {@link FreightTemplateGoodsDeleteByIdAndStoreIdRequest}
     * @return {@link BaseResponse}
     */
    @Override

    public BaseResponse deleteByIdAndStoreId(@RequestBody @Valid FreightTemplateGoodsDeleteByIdAndStoreIdRequest
                                                     request) {
        freightTemplateGoodsService.delById(request.getFreightTempId(), request.getStoreId());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 根据单品运费模板id和店铺id复制单品运费模板
     *
     * @param request 复制单品运费模板数据结构 {@link FreightTemplateGoodsCopyByIdAndStoreIdRequest}
     * @return {@link BaseResponse}
     */
    @Override

    public BaseResponse copyByIdAndStoreId(@RequestBody @Valid FreightTemplateGoodsCopyByIdAndStoreIdRequest request){
        freightTemplateGoodsService.copyFreightTemplateGoods(request.getFreightTempId(), request.getStoreId());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 初始单品运费模板
     *
     * @param request 初始单品运费模板数据结构 {@link FreightTemplateGoodsInitByStoreIdRequest}
     * @return {@link BaseResponse}
     */
    @Override

    public BaseResponse initByStoreId(@RequestBody @Valid FreightTemplateGoodsInitByStoreIdRequest request){
        freightTemplateGoodsService.initFreightTemplate(request.getStoreId());
        return BaseResponse.SUCCESSFUL();
    }

}
