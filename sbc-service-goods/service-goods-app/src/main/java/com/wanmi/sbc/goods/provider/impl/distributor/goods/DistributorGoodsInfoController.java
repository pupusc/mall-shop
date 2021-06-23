package com.wanmi.sbc.goods.provider.impl.distributor.goods;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.constant.DistributorGoodsCountsCode;
import com.wanmi.sbc.goods.api.constant.GoodsErrorCode;
import com.wanmi.sbc.goods.api.provider.distributor.goods.DistributorGoodsInfoProvider;
import com.wanmi.sbc.goods.api.request.distributor.goods.*;
import com.wanmi.sbc.goods.api.response.distributor.goods.DistributorGoodsInfoAddResponse;
import com.wanmi.sbc.goods.api.response.distributor.goods.DistributorGoodsInfoCountsResponse;
import com.wanmi.sbc.goods.api.response.distributor.goods.DistributorGoodsInfoDeleteResponse;
import com.wanmi.sbc.goods.distributor.goods.model.root.DistributorGoodsInfo;
import com.wanmi.sbc.goods.distributor.goods.service.DistributorGoodsInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 分销员商品控制层
 *
 * @author: Geek Wang
 * @createDate: 2019/3/1 11:15
 * @version: 1.0
 */
@RestController
@Validated
public class DistributorGoodsInfoController implements DistributorGoodsInfoProvider {

    @Autowired
    private DistributorGoodsInfoService distributorGoodsInfoService;


    /**
     * 新增分销商品
     *
     * @param distributorGoodsInfoAddRequest {@link DistributorGoodsInfoAddRequest}
     * @return {@link DistributorGoodsInfoAddResponse}
     */
    @Override
    public BaseResponse<DistributorGoodsInfoAddResponse> add(@RequestBody @Valid DistributorGoodsInfoAddRequest distributorGoodsInfoAddRequest) {
        DistributorGoodsInfo distributorGoodsInfo = distributorGoodsInfoService.add(distributorGoodsInfoAddRequest);
        DistributorGoodsInfoAddResponse distributorGoodsInfoAddResponse = new DistributorGoodsInfoAddResponse();
        KsBeanUtil.copyPropertiesThird(distributorGoodsInfo, distributorGoodsInfoAddResponse);
        return BaseResponse.success(distributorGoodsInfoAddResponse);
    }

    /**
     * 根据分销员-会员ID和SKU编号删除分销员商品
     *
     * @param request {@link DistributorGoodsInfoDeleteRequest}
     * @return {@link DistributorGoodsInfoDeleteResponse}
     */
    @Override
    public BaseResponse<DistributorGoodsInfoDeleteResponse> delete(@RequestBody @Valid DistributorGoodsInfoDeleteRequest request) {
        int result = distributorGoodsInfoService.deleteByCustomerIdAndGoodsInfoId(request.getCustomerId(), request.getGoodsInfoId());
        return BaseResponse.success(new DistributorGoodsInfoDeleteResponse(result));
    }

    /**
     * 修改分销员商品排序
     *
     * @param request {@link DistributorGoodsInfoModifySequenceRequest}
     * @return
     */
    @Override
    public BaseResponse modifySequence(@RequestBody @Valid DistributorGoodsInfoModifySequenceRequest request) {
        Boolean result = distributorGoodsInfoService.modifySequence(request);
        return BaseResponse.success(result);
    }

    /**
     * 商家-社交分销开关，更新对应的分销员商品状态
     *
     * @param request {@link DistributorGoodsInfoModifyByStoreIdAndStatusRequest}
     * @return
     */
    @Override
    public BaseResponse modifyByStoreIdAndStatus(@RequestBody @Valid DistributorGoodsInfoModifyByStoreIdAndStatusRequest request) {
        int result = distributorGoodsInfoService.modifyByStoreIdAndStatus(request.getStoreId(), request.getStatus());
        return BaseResponse.success(result);
    }

    /**
     * 根据店铺ID删除分销员商品表数据
     *
     * @param request
     * @return
     */
    @Override
    public BaseResponse deleteByStoreId(@RequestBody @Valid DistributorGoodsInfoDeleteByStoreIdRequest request) {
        int result = distributorGoodsInfoService.deleteByStoreId(request.getStoreId());
        return BaseResponse.success(result);
    }

    /**
     * 根据店铺ID集合批量删除分销员商品表数据
     *
     * @param request
     * @return
     */
    @Override
    public BaseResponse deleteByStoreIds(@RequestBody @Valid DistributorGoodsInfoDeleteByStoreIdsRequest request) {
        int result = distributorGoodsInfoService.deleteByStoreIdsIn(request.getStoreIds());
        return BaseResponse.success(result);
    }

    /**
     * 根据会员id查询这个店铺下的分销商品数校验
     *
     * @param request
     * @return
     */
    @Override
    public BaseResponse checkCountsByCustomerId(@RequestBody @Valid DistributorGoodsInfoByCustomerIdRequest request) {
        Long counts = distributorGoodsInfoService.getCountsByCustomerId(request.getCustomerId());
        if (counts != null && counts <= DistributorGoodsCountsCode.COUNTS) {
            return BaseResponse.success(new DistributorGoodsInfoCountsResponse(counts));
        }
        return new BaseResponse(GoodsErrorCode.CHECK_COUNTS_BY_CUSTOMER_ID);
    }
}
