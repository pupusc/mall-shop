package com.wanmi.sbc.goods.provider.impl.freight;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.goods.api.provider.freight.FreightTemplateGoodsProvider;
import com.wanmi.sbc.goods.api.request.freight.*;
import com.wanmi.sbc.goods.api.request.supplier.SecondLevelSupplierCreateUpdateRequest;
import com.wanmi.sbc.goods.bean.vo.ExpressNotSupportVo;
import com.wanmi.sbc.goods.bean.vo.SupplierSecondVo;
import com.wanmi.sbc.goods.freight.model.root.ExpressNotSupport;
import com.wanmi.sbc.goods.freight.service.FreightTemplateGoodsService;
import com.wanmi.sbc.goods.supplier.model.SupplierModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Override
    public BaseResponse saveOrUpdateNotSupportArea(ExpressNotSupportCreateUpdateRequest request){
        freightTemplateGoodsService.saveOrUpdateNotSupportArea(request);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse<String> importNotSupportArea(String areas, Long supplierId){
        String errorNames = freightTemplateGoodsService.importNotSupportArea(areas, supplierId);
        return BaseResponse.success(errorNames);
    }

    @Override
    public BaseResponse deleteNotSupportArea(Long id){
        freightTemplateGoodsService.deleteNotSupportArea(id);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse<ExpressNotSupportVo> findNotSupportArea(Long id){
        ExpressNotSupport notSupportArea = freightTemplateGoodsService.findNotSupportArea(id);
        ExpressNotSupportVo expressNotSupportVo = new ExpressNotSupportVo();
        BeanUtils.copyProperties(notSupportArea, expressNotSupportVo);
        return BaseResponse.success(expressNotSupportVo);
    }

    @Override
    public BaseResponse saveOrUpdateSecondLevelSupplier(SecondLevelSupplierCreateUpdateRequest request){
        freightTemplateGoodsService.saveOrUpdateSecondLevelSupplier(request);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse<List<SupplierSecondVo>> findSecondLevelSupplier(){
        List<SupplierModel> secondLevelSupplier = freightTemplateGoodsService.findSecondLevelSupplier();
        List<SupplierSecondVo> collect = secondLevelSupplier.stream().map(supplierModel -> {
            SupplierSecondVo supplierSecondVo = new SupplierSecondVo();
            BeanUtils.copyProperties(supplierModel, supplierSecondVo);
            return supplierSecondVo;
        }).collect(Collectors.toList());
        return BaseResponse.success(collect);
    }

    @Override
    public BaseResponse deleteSecondLevelSupplier(Long id){
        freightTemplateGoodsService.deleteSecondLevelSupplier(id);
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
