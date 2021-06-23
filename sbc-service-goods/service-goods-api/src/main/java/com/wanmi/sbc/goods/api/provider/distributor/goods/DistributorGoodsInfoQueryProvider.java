package com.wanmi.sbc.goods.api.provider.distributor.goods;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.distributor.goods.DistributorGoodsInfoListByCustomerIdAndGoodsIdRequest;
import com.wanmi.sbc.goods.api.request.distributor.goods.DistributorGoodsInfoListByCustomerIdRequest;
import com.wanmi.sbc.goods.api.request.distributor.goods.DistributorGoodsInfoPageByCustomerIdRequest;
import com.wanmi.sbc.goods.api.request.distributor.goods.DistributorGoodsInfoVerifyRequest;
import com.wanmi.sbc.goods.api.response.distributor.goods.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @author: Geek Wang
 * @createDate: 2019/3/1 11:15
 * @version: 1.0
 */
@FeignClient(value = "${application.goods.name}", contextId = "DistributorGoodsInfoQueryProvider")
public interface DistributorGoodsInfoQueryProvider {

    /**
     * 根据分销员的会员ID查询分销员商品
     * @param distributorGoodsInfoListByCustomerIdRequest  {@link DistributorGoodsInfoListByCustomerIdRequest}
     * @return {@link DistributorGoodsInfoListByCustomerIdResponse}
     */
    @PostMapping("/goods/${application.goods.version}/distributor/goods/list-by-customer-id")
    BaseResponse<DistributorGoodsInfoListByCustomerIdResponse> listByCustomerId(@RequestBody @Valid DistributorGoodsInfoListByCustomerIdRequest distributorGoodsInfoListByCustomerIdRequest);

    /**
     * 根据分销员，过滤非精选的单品
     * @param request  {@link DistributorGoodsInfoVerifyRequest}
     * @return {@link DistributorGoodsInfoVerifyResponse}
     */
    @PostMapping("/goods/${application.goods.version}/distributor/goods/verify")
    BaseResponse<DistributorGoodsInfoVerifyResponse> verifyDistributorGoodsInfo(@RequestBody @Valid DistributorGoodsInfoVerifyRequest request);

    /**
     * 根据分销员的会员ID和SPU编号查询分销员商品
     * @param distributorGoodsInfoListByCustomerIdAndGoodsIdRequest  {@link DistributorGoodsInfoListByCustomerIdAndGoodsIdRequest}
     * @return {@link DistributorGoodsInfoListByCustomerIdAndGoodsIdResponse}
     */
    @PostMapping("/goods/${application.goods.version}/distributor/goods/list-by-customer-id-and-goods-id")
    BaseResponse<DistributorGoodsInfoListByCustomerIdAndGoodsIdResponse> listByCustomerIdAndGoodsId(@RequestBody @Valid DistributorGoodsInfoListByCustomerIdAndGoodsIdRequest distributorGoodsInfoListByCustomerIdAndGoodsIdRequest);

    /**
     * 根据分销员的会员ID查询分销员商品分页数据
     * @param request  {@link DistributorGoodsInfoPageByCustomerIdRequest}
     * @return {@link DistributorGoodsInfoPageByCustomerIdResponse}
     */
    @PostMapping("/goods/${application.goods.version}/distributor/goods/page-by-customer-id")
    BaseResponse<DistributorGoodsInfoPageByCustomerIdResponse> pageByCustomerId(@RequestBody @Valid DistributorGoodsInfoPageByCustomerIdRequest request);

    /**
     * 查询分销员商品表-店铺ID集合数据
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/distributor/goods/list-all-store-id")
    BaseResponse<DistributorGoodsInfoListAllStoreIdResponse>  findAllStoreId();

    /**
     * 查询分销员商品表-销量排序前10
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/distributor/goods/saleNum-desc-limit10")
    BaseResponse<DistributorGoodsIdsResp>  salesNumDescLimit10();
}
