package com.wanmi.sbc.goods.provider.impl.storecate;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.storecate.StoreCateProvider;
import com.wanmi.sbc.goods.api.request.storecate.*;
import com.wanmi.sbc.goods.api.response.storecate.StoreCateAddResponse;
import com.wanmi.sbc.goods.api.response.storecate.StoreCateDeleteResponse;
import com.wanmi.sbc.goods.ares.GoodsAresService;
import com.wanmi.sbc.goods.storecate.request.StoreCateSaveRequest;
import com.wanmi.sbc.goods.storecate.request.StoreCateSortRequest;
import com.wanmi.sbc.goods.storecate.response.StoreCateResponse;
import com.wanmi.sbc.goods.storecate.service.StoreCateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Objects;

/**
 * @author: wanggang
 * @createDate: 2018/11/1 9:52
 * @version: 1.0
 */
@RestController
@Validated
public class StoreCateController implements StoreCateProvider {

    @Autowired
    private StoreCateService storeCateService;

    @Autowired
    private GoodsAresService goodsAresService;

    /**
     * 根据店铺ID初始化分类，生成默认分类
     * @param storeCateInitByStoreIdRequest {@link StoreCateInitByStoreIdRequest }
     * @return
     */
    
    @Override
    public BaseResponse initByStoreId(@RequestBody @Valid StoreCateInitByStoreIdRequest storeCateInitByStoreIdRequest){
        storeCateService.initStoreDefaultCate(storeCateInitByStoreIdRequest.getStoreId());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 新增商品店铺分类
     * @param storeCateAddRequest {@link StoreCateAddRequest }
     * @return 商品店铺分类 {@link StoreCateAddResponse }
     */
    
    @Override
    public BaseResponse<StoreCateAddResponse> add(@RequestBody @Valid StoreCateAddRequest storeCateAddRequest){
        StoreCateResponse storeCateResponse = storeCateService.add(storeCateAddRequest);
        if (Objects.isNull(storeCateResponse)){
            return BaseResponse.success(new StoreCateAddResponse());
        }
        return BaseResponse.success(new StoreCateAddResponse(StoreCateConvert.toStoreCateResponseVO(storeCateResponse)));
    }

    /**
     * 编辑店铺商品分类
     * @param storeCateModifyRequest {@link StoreCateModifyRequest }
     * @return
     */
    
    @Override
    public BaseResponse modify(@RequestBody @Valid StoreCateModifyRequest storeCateModifyRequest){
        storeCateService.edit(storeCateModifyRequest);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 删除店铺商品分类
     * @param storeCateDeleteRequest {@link StoreCateDeleteRequest }
     * @return {@link StoreCateDeleteResponse }
     */
    
    @Override
    public BaseResponse<StoreCateDeleteResponse> delete(@RequestBody @Valid StoreCateDeleteRequest storeCateDeleteRequest){
        HashMap map = storeCateService.delete(storeCateDeleteRequest);
        //ares埋点-商品-删除店铺分类
        goodsAresService.dispatchFunction("delStoreCate", new Object[]{map.get("allCate"), map.get("storeCateGoodsRelas")});
        return BaseResponse.success(new StoreCateDeleteResponse(map));
    }

    /**
     * 商家APP里店铺分类排序
     * @param storeCateBatchSortRequest {@link StoreCateBatchSortRequest }
     * @return
     */
    
    @Override
    public BaseResponse batchSort(@RequestBody @Valid StoreCateBatchSortRequest storeCateBatchSortRequest){
        StoreCateSaveRequest saveRequest = StoreCateConvert.toStoreCateSaveRequest(storeCateBatchSortRequest);
        storeCateService.batchSortCate(saveRequest);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 批量修改分类排序
     * @param request 批量分类排序信息结构 {@link StoreCateBatchModifySortRequest }
     * @return 操作结果 {@link BaseResponse}
     */
    
    @Override
    public BaseResponse batchModifySort(@RequestBody @Valid StoreCateBatchModifySortRequest request){
        storeCateService.dragSort(KsBeanUtil.convert(request.getStoreCateSortDTOList(), StoreCateSortRequest.class));
        return BaseResponse.SUCCESSFUL();
    }
}
