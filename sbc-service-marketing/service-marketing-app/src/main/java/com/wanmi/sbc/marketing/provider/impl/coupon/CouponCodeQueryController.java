package com.wanmi.sbc.marketing.provider.impl.coupon;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.marketing.api.provider.coupon.CouponCodeQueryProvider;
import com.wanmi.sbc.marketing.api.request.coupon.*;
import com.wanmi.sbc.marketing.api.response.coupon.*;
import com.wanmi.sbc.marketing.bean.dto.CouponCodeDTO;
import com.wanmi.sbc.marketing.bean.vo.CouponCodeVO;
import com.wanmi.sbc.marketing.common.mapper.TradeItemInfoMapper;
import com.wanmi.sbc.marketing.coupon.model.root.CouponCode;
import com.wanmi.sbc.marketing.coupon.request.CouponCodeListForUseRequest;
import com.wanmi.sbc.marketing.coupon.response.CouponCodeQueryResponse;
import com.wanmi.sbc.marketing.coupon.service.CouponCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>对优惠券码查询接口</p>
 * Created by daiyitian on 2018-11-23-下午6:23.
 */
@Validated
@RestController
public class CouponCodeQueryController implements CouponCodeQueryProvider {

    @Autowired
    private CouponCodeService couponCodeService;
    @Autowired
    private TradeItemInfoMapper tradeItemInfoMapper;

    /**
     * 根据客户id查询使用优惠券列表
     *
     * @param request 包含客户id查询请求结构 {@link CouponCodeListForUseByCustomerIdRequest}
     * @return 优惠券列表 {@link CouponCodeListForUseByCustomerIdResponse}
     */
    @Override
    public BaseResponse<CouponCodeListForUseByCustomerIdResponse> listForUseByCustomerId(@RequestBody @Valid
                                                                                          CouponCodeListForUseByCustomerIdRequest
                                                                                          request){
        CouponCodeListForUseRequest useRequest = new CouponCodeListForUseRequest();
        useRequest.setCustomerId(request.getCustomerId());
        useRequest.setTradeItems(tradeItemInfoMapper.tradeItemInfoDTOsToTradeItemInfos(request.getTradeItems()));
        useRequest.setStoreId(request.getStoreId());
        useRequest.setPrice(request.getPrice());
        List<CouponCodeVO> voList = couponCodeService.listCouponCodeForUse(useRequest);
        return BaseResponse.success(CouponCodeListForUseByCustomerIdResponse.builder().couponCodeList(voList).build());
    }

    /**
     * 分页查询优惠券列表
     *
     * @param request 分页查询优惠券列表请求结构 {@link CouponCodePageRequest}
     * @return 优惠券分页列表 {@link CouponCodePageResponse}
     */
    @Override
    public BaseResponse<CouponCodePageResponse> page(@RequestBody @Valid CouponCodePageRequest request){
        CouponCodeQueryResponse queryResponse = couponCodeService.listMyCouponList(
                KsBeanUtil.convert(request, com.wanmi.sbc.marketing.coupon.request.CouponCodePageRequest.class));
        return BaseResponse.success(CouponCodePageResponse.builder()
                .couponCodeVos(KsBeanUtil.convertPage(queryResponse.getCouponCodeVos(), CouponCodeVO.class))
                .overDueCount(queryResponse.getOverDueCount())
                .unUseCount(queryResponse.getUnUseCount())
                .usedCount(queryResponse.getUsedCount())
                .build());
    }

    /**
     * 分页查询优惠券列表
     *
     * @param request 分页查询优惠券列表请求结构 {@link CouponCodePageRequest}
     * @return 优惠券分页列表 {@link CouponCodePageResponse}
     */
    @Override
    public BaseResponse<CouponCodeSimplePageResponse> simplePage(@RequestBody @Valid CouponCodeSimplePageRequest request){
        Page<CouponCodeVO> queryResponse = couponCodeService.pageMyCouponList(
                KsBeanUtil.convert(request, com.wanmi.sbc.marketing.coupon.request.CouponCodePageRequest.class));
        return BaseResponse.success(CouponCodeSimplePageResponse.builder()
                .couponCodeVos(KsBeanUtil.convertPage(queryResponse, CouponCodeVO.class))
                .build());
    }

    /**
     * 根据客户和券码id查询不可用的平台券以及优惠券实际优惠总额的请求结构
     *
     * @param request 包含客户和券码id的查询请求结构 {@link CouponCheckoutRequest}
     * @return 操作结果 {@link CouponCheckoutResponse}
     */
    @Override
    public BaseResponse<CouponCheckoutResponse> checkout(@RequestBody @Valid CouponCheckoutRequest request){
        return BaseResponse.success(couponCodeService.checkoutCoupons(request));
    }

    /**
     * 根据条件查询优惠券码列表
     * @param request
     * @return
     */
    @Override
    public BaseResponse<CouponCodeListByConditionResponse> listCouponCodeByCondition(@RequestBody @Valid CouponCodeQueryRequest request) {
        List<CouponCode> couponCodeList = couponCodeService.listCouponCodeByCondition(request);
        CouponCodeListByConditionResponse response = new CouponCodeListByConditionResponse();
        response.setCouponCodeList(KsBeanUtil.copyListProperties(couponCodeList, CouponCodeDTO.class));
        return BaseResponse.success(response);
    }

    /**
     * 根据条件分頁查询优惠券码列表
     * @param request
     * @return
     */
    @Override
    public BaseResponse<CouponCodePageByConditionResponse> pageCouponCodeByCondition(@RequestBody @Valid CouponCodeQueryRequest request) {
        Page<CouponCode> couponCodePage = couponCodeService.pageCouponCodeByCondition(request);
        Page<CouponCodeDTO> newPage = couponCodePage.map(couponCode -> KsBeanUtil.convert(couponCode, CouponCodeDTO.class));
        return BaseResponse.success(CouponCodePageByConditionResponse.builder().couponCodeDTOPage(newPage).build());
    }

    /**
     * 根据条件查询优惠券码列表
     * @param request
     * @return
     */
    @Override
    public BaseResponse<CouponCodeListNotUseResponse> listNotUseStatus(@RequestBody @Valid CouponCodeQueryNotUseRequest request) {
        CouponCodeQueryRequest codeQueryRequest = KsBeanUtil.convert(request,CouponCodeQueryRequest.class);
        List<CouponCode> couponCodeList = couponCodeService.findNotUseStatusCouponCode(codeQueryRequest);
        CouponCodeListNotUseResponse response = new CouponCodeListNotUseResponse();
        response.setCouponCodeList(KsBeanUtil.copyListProperties(couponCodeList, CouponCodeVO.class));
        return BaseResponse.success(response);
    }

    @Override
    public BaseResponse<CouponCodeValidOrderCommitResponse> validOrderCommit(@Valid CouponCodeValidOrderCommitRequest request) {
        CouponCodeValidOrderCommitResponse response = couponCodeService.validOrderCommit(request);
        return BaseResponse.success(response);
    }
}
