package com.wanmi.sbc.store;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.provider.follow.StoreCustomerFollowQueryProvider;
import com.wanmi.sbc.customer.api.provider.level.CustomerLevelQueryProvider;
import com.wanmi.sbc.customer.api.provider.store.StoreCustomerQueryProvider;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.provider.storeevaluatesum.StoreEvaluateSumQueryProvider;
import com.wanmi.sbc.customer.api.provider.storelevel.StoreLevelQueryProvider;
import com.wanmi.sbc.customer.api.request.customer.CustomerGetByIdRequest;
import com.wanmi.sbc.customer.api.request.follow.StoreCustomerFollowExistsRequest;
import com.wanmi.sbc.customer.api.request.follow.StoreFollowBystoreIdRequest;
import com.wanmi.sbc.customer.api.request.level.CustomerLevelGetRequest;
import com.wanmi.sbc.customer.api.request.store.*;
import com.wanmi.sbc.customer.api.request.storeevaluatesum.StoreEvaluateSumQueryRequest;
import com.wanmi.sbc.customer.api.request.storelevel.StoreLevelByIdRequest;
import com.wanmi.sbc.customer.api.request.storelevel.StoreLevelByStoreIdRequest;
import com.wanmi.sbc.customer.api.response.customer.CustomerGetByIdResponse;
import com.wanmi.sbc.customer.api.response.follow.StoreFollowCountBystoreIdResponse;
import com.wanmi.sbc.customer.api.response.store.*;
import com.wanmi.sbc.customer.api.response.storeevaluatesum.StoreEvaluateSumByIdResponse;
import com.wanmi.sbc.customer.api.response.storelevel.StroeLevelInfoByIdResponse;
import com.wanmi.sbc.customer.api.response.storelevel.StroeLevelInfoResponse;
import com.wanmi.sbc.customer.bean.enums.ScoreCycle;
import com.wanmi.sbc.customer.bean.vo.CustomerLevelVO;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import com.wanmi.sbc.goods.api.provider.brand.ContractBrandQueryProvider;
import com.wanmi.sbc.goods.api.provider.cate.ContractCateQueryProvider;
import com.wanmi.sbc.goods.api.provider.goods.GoodsQueryProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.request.brand.ContractBrandListRequest;
import com.wanmi.sbc.goods.api.request.cate.ContractCateListByConditionRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoCountByConditionRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoStoreIdBySkuIdRequest;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoCountByConditionResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoStoreIdBySkuIdResponse;
import com.wanmi.sbc.goods.bean.enums.AddedFlag;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import com.wanmi.sbc.goods.bean.vo.ContractBrandVO;
import com.wanmi.sbc.goods.bean.vo.ContractCateVO;
import com.wanmi.sbc.marketing.api.provider.coupon.CouponActivityProvider;
import com.wanmi.sbc.marketing.api.request.coupon.GetCouponGroupRequest;
import com.wanmi.sbc.marketing.api.response.coupon.GetRegisterOrStoreCouponResponse;
import com.wanmi.sbc.marketing.bean.enums.CouponActivityType;
import com.wanmi.sbc.order.api.provider.trade.TradeQueryProvider;
import com.wanmi.sbc.order.api.request.trade.TradeListAllRequest;
import com.wanmi.sbc.order.bean.dto.TradeQueryDTO;
import com.wanmi.sbc.order.bean.dto.TradeStateDTO;
import com.wanmi.sbc.order.bean.enums.PayState;
import com.wanmi.sbc.store.response.StoreCustSystemResponse;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 店铺信息查询bff
 * Created by bail on 2017/11/29.
 */
@Api(tags = "StoreBaseController", description = "店铺信息查询 API")
@RestController
@RequestMapping("/store")
public class StoreBaseController {

    @Autowired
    private StoreQueryProvider storeQueryProvider;

    @Autowired
    private ContractBrandQueryProvider contractBrandQueryProvider;

    @Autowired
    private ContractCateQueryProvider contractCateQueryProvider;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private StoreCustomerFollowQueryProvider storeCustomerFollowQueryProvider;

    @Autowired
    private StoreCustomerQueryProvider storeCustomerQueryProvider;

    @Autowired
    private StoreLevelQueryProvider storeLevelQueryProvider;

    @Autowired
    private CustomerLevelQueryProvider customerLevelQueryProvider;

    @Autowired
    private CustomerQueryProvider customerQueryProvider;

    @Autowired
    private StoreEvaluateSumQueryProvider storeEvaluateSumQueryProvider;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    @Autowired
    private GoodsQueryProvider goodsQueryProvider;

    @Autowired
    private CouponActivityProvider couponActivityProvider;

    @Autowired
    private TradeQueryProvider tradeQueryProvider;

    private static Pattern PATTERN = Pattern.compile(",");

    /**
     * 查询店铺基本信息
     *
     * @return
     */
    @ApiOperation(value = "查询店铺基本信息")
    @RequestMapping(value = "/storeInfo", method = RequestMethod.POST)
    public BaseResponse<StoreBaseResponse> queryStore(@RequestBody StoreQueryRequest request) {
        StoreBaseResponse response = storeQueryProvider.getStoreBaseInfoById(new StoreBaseInfoByIdRequest(request
                .getStoreId())).getContext();
        response.setIsFollowed(
                storeCustomerFollowQueryProvider.queryStoreCustomerFollowByStoreId(
                        StoreCustomerFollowExistsRequest.builder()
                                .storeId(request.getStoreId())
                                .customerId(commonUtil.getOperatorId()).build()
                ).getContext().getIsFollowed());

        //店铺评分信息
        StoreEvaluateSumByIdResponse storeEvaluateSumByIdResp = storeEvaluateSumQueryProvider.getByStoreId(
                StoreEvaluateSumQueryRequest.builder().storeId(request.getStoreId())
                        .scoreCycle(ScoreCycle.ONE_HUNDRED_AND_EIGHTY.toValue()).build()).getContext();
        response.setStoreEvaluateSumVO(storeEvaluateSumByIdResp.getStoreEvaluateSumVO());

        //店铺的关注总数
        StoreFollowCountBystoreIdResponse storeFollowCountBystoreIdResponse = storeCustomerFollowQueryProvider
                .queryStoreCustomerFollowCountByStoreId(StoreFollowBystoreIdRequest.builder().storeId(request.getStoreId())
                        .build()).getContext();
        response.setFollowSum(storeFollowCountBystoreIdResponse.getCount());

        //sku店铺的总数
        List<Integer> list = new ArrayList();
        list.add(AddedFlag.YES.toValue());
        list.add(AddedFlag.PART.toValue());
        GoodsInfoCountByConditionResponse goodsInfoCountByConditionResponse = goodsInfoQueryProvider.countByCondition(
                GoodsInfoCountByConditionRequest.builder().storeId(request.getStoreId()).delFlag(DeleteFlag.NO.toValue())
                        .addedFlags(list).auditStatus(CheckStatus.CHECKED)
                        .build()).getContext();
        response.setGoodsSum(goodsInfoCountByConditionResponse.getCount());

        return BaseResponse.success(response);
    }

    /**
     * 查询店铺基本信息
     *
     * @return
     */
    @ApiOperation(value = "查询店铺基本信息")
    @RequestMapping(value = "/storeInfoBySkuId", method = RequestMethod.POST)
    public BaseResponse<StoreBaseResponse> queryStoreBySkuId(@RequestBody StoreQueryBySkuIdRequest request) {


        GoodsInfoStoreIdBySkuIdResponse storeIdResponse= goodsInfoQueryProvider.getStoreIdByGoodsId(GoodsInfoStoreIdBySkuIdRequest
                .builder()
                .skuId(request.getSkuId()).build()).getContext();

            StoreBaseResponse response = storeQueryProvider.getStoreBaseInfoById(new StoreBaseInfoByIdRequest(storeIdResponse
                .getStoreId())).getContext();
        response.setIsFollowed(
                storeCustomerFollowQueryProvider.queryStoreCustomerFollowByStoreId(
                        StoreCustomerFollowExistsRequest.builder()
                                .storeId(storeIdResponse.getStoreId())
                                .customerId(commonUtil.getOperatorId()).build()
                ).getContext().getIsFollowed());

        //店铺评分信息
        StoreEvaluateSumByIdResponse storeEvaluateSumByIdResp = storeEvaluateSumQueryProvider.getByStoreId(
                StoreEvaluateSumQueryRequest.builder().storeId(storeIdResponse.getStoreId())
                        .scoreCycle(ScoreCycle.ONE_HUNDRED_AND_EIGHTY.toValue()).build()).getContext();
        response.setStoreEvaluateSumVO(storeEvaluateSumByIdResp.getStoreEvaluateSumVO());

        //店铺的关注总数
        StoreFollowCountBystoreIdResponse storeFollowCountBystoreIdResponse = storeCustomerFollowQueryProvider
                .queryStoreCustomerFollowCountByStoreId(StoreFollowBystoreIdRequest.builder().storeId(storeIdResponse.getStoreId())
                        .build()).getContext();
        response.setFollowSum(storeFollowCountBystoreIdResponse.getCount());

        //sku店铺的总数
        List<Integer> list = new ArrayList();
        list.add(AddedFlag.YES.toValue());
        list.add(AddedFlag.PART.toValue());
        GoodsInfoCountByConditionResponse goodsInfoCountByConditionResponse = goodsInfoQueryProvider.countByCondition(
                GoodsInfoCountByConditionRequest.builder().storeId(storeIdResponse.getStoreId()).delFlag(DeleteFlag.NO.toValue())
                        .addedFlags(list).auditStatus(CheckStatus.CHECKED)
                        .build()).getContext();
        response.setGoodsSum(goodsInfoCountByConditionResponse.getCount());

        return BaseResponse.success(response);
    }



    /**
     * 未登录状态下查询店铺基本信息
     *
     * @return
     */
    @ApiOperation(value = "未登录状态下查询店铺基本信息")
    @RequestMapping(value = "/unLogin/storeInfo", method = RequestMethod.POST)
    public BaseResponse<BossStoreBaseInfoResponse> queryStoreUnlogin(@RequestBody StoreQueryRequest request) {
        BossStoreBaseInfoResponse response =
                storeQueryProvider.getBossStoreBaseInfoById(new BossStoreBaseInfoByIdRequest
                        (request.getStoreId())).getContext();
        response.setIsFollowed(false);
        //店铺评分信息
        StoreEvaluateSumByIdResponse storeEvaluateSumByIdResp = storeEvaluateSumQueryProvider.getByStoreId(
                StoreEvaluateSumQueryRequest.builder().storeId(request.getStoreId())
                        .scoreCycle(ScoreCycle.ONE_HUNDRED_AND_EIGHTY.toValue()).build()).getContext();
        response.setStoreEvaluateSumVO(storeEvaluateSumByIdResp.getStoreEvaluateSumVO());

        //店铺的关注总数
        StoreFollowCountBystoreIdResponse storeFollowCountBystoreIdResponse = storeCustomerFollowQueryProvider
                .queryStoreCustomerFollowCountByStoreId(StoreFollowBystoreIdRequest.builder().storeId(request.getStoreId())
                        .build()).getContext();
        response.setFollowSum(storeFollowCountBystoreIdResponse.getCount());

        //sku店铺的总数
        List<Integer> list = new ArrayList();
        list.add(AddedFlag.YES.toValue());
        list.add(AddedFlag.PART.toValue());
        GoodsInfoCountByConditionResponse goodsInfoCountByConditionResponse = goodsInfoQueryProvider.countByCondition(
                GoodsInfoCountByConditionRequest.builder().storeId(request.getStoreId()).delFlag(DeleteFlag.NO.toValue())
                        .addedFlags(list).auditStatus(CheckStatus.CHECKED)
                        .build()).getContext();
        response.setGoodsSum(goodsInfoCountByConditionResponse.getCount());

        return BaseResponse.success(response);
    }


    /**
     * 根据skuId未登录状态下查询店铺基本信息
     *
     * @return
     */
    @ApiOperation(value = "根据skuId未登录状态下查询店铺基本信息")
    @RequestMapping(value = "/unLogin/storeInfoBySkuId", method = RequestMethod.POST)
    public BaseResponse<BossStoreBaseInfoResponse> queryStoreUnlogin(@RequestBody StoreQueryBySkuIdRequest request) {
        GoodsInfoStoreIdBySkuIdResponse storeIdResponse= goodsInfoQueryProvider.getStoreIdByGoodsId(GoodsInfoStoreIdBySkuIdRequest
                .builder()
                .skuId(request.getSkuId()).build()).getContext();

        BossStoreBaseInfoResponse response =
                storeQueryProvider.getBossStoreBaseInfoById(new BossStoreBaseInfoByIdRequest
                        (storeIdResponse.getStoreId())).getContext();
        response.setIsFollowed(false);
        //店铺评分信息
        StoreEvaluateSumByIdResponse storeEvaluateSumByIdResp = storeEvaluateSumQueryProvider.getByStoreId(
                StoreEvaluateSumQueryRequest.builder().storeId(storeIdResponse.getStoreId())
                        .scoreCycle(ScoreCycle.ONE_HUNDRED_AND_EIGHTY.toValue()).build()).getContext();
        response.setStoreEvaluateSumVO(storeEvaluateSumByIdResp.getStoreEvaluateSumVO());

        //店铺的关注总数
        StoreFollowCountBystoreIdResponse storeFollowCountBystoreIdResponse = storeCustomerFollowQueryProvider
                .queryStoreCustomerFollowCountByStoreId(StoreFollowBystoreIdRequest.builder().storeId(storeIdResponse.getStoreId())
                        .build()).getContext();
        response.setFollowSum(storeFollowCountBystoreIdResponse.getCount());

        //sku店铺的总数
        List<Integer> list = new ArrayList();
        list.add(AddedFlag.YES.toValue());
        list.add(AddedFlag.PART.toValue());
        GoodsInfoCountByConditionResponse goodsInfoCountByConditionResponse = goodsInfoQueryProvider.countByCondition(
                GoodsInfoCountByConditionRequest.builder().storeId(storeIdResponse.getStoreId()).delFlag(DeleteFlag.NO.toValue())
                        .addedFlags(list).auditStatus(CheckStatus.CHECKED)
                        .build()).getContext();
        response.setGoodsSum(goodsInfoCountByConditionResponse.getCount());

        return BaseResponse.success(response);
    }

    /**
     * 查询店铺档案信息(包括签约分类,品牌图片信息)
     *
     * @return
     */
    @ApiOperation(value = "查询店铺档案信息(包括签约分类,品牌图片信息)")
    @RequestMapping(value = "/storeDocument", method = RequestMethod.POST)
    public BaseResponse<StoreDocumentResponse> queryStoreDocument(@RequestBody StoreQueryRequest request) {
        //1.查询店铺主表信息
        StoreDocumentResponse response = storeQueryProvider.getStoreDocumentById(new StoreDocumentByIdRequest(request
                .getStoreId())).getContext();

        //2.查询签约品牌图片信息
        ContractBrandListRequest brandRequest = new ContractBrandListRequest();
        brandRequest.setStoreId(request.getStoreId());
        List<ContractBrandVO> brands =
                contractBrandQueryProvider.list(brandRequest).getContext().getContractBrandVOList();
        if (CollectionUtils.isNotEmpty(brands)) {
            response.setBrandPicList(brands.stream().filter(brand -> StringUtils.isNotBlank(brand.getAuthorizePic()))
                    .flatMap(brand -> PATTERN.splitAsStream(brand.getAuthorizePic()))
                    .collect(Collectors.toList()));
        }

        //3.查询签约分类图片信息
        ContractCateListByConditionRequest cateRequest = new ContractCateListByConditionRequest();
        cateRequest.setStoreId(request.getStoreId());
        List<ContractCateVO> cates =
                contractCateQueryProvider.listByCondition(cateRequest).getContext().getContractCateList();
        if (CollectionUtils.isNotEmpty(cates)) {
            response.setCatePicList(cates.stream().filter(cate -> StringUtils.isNotBlank(cate.getQualificationPics()))
                    .flatMap(cate -> PATTERN.splitAsStream(cate.getQualificationPics()))
                    .collect(Collectors.toList()));
        }

        //店铺的服务评分
        StoreEvaluateSumByIdResponse storeEvaluateSumByIdResponse = storeEvaluateSumQueryProvider.getByStoreId(
                StoreEvaluateSumQueryRequest.builder().storeId(request.getStoreId())
                        .scoreCycle(ScoreCycle.ONE_HUNDRED_AND_EIGHTY.toValue()).build()).getContext();
        response.setStoreEvaluateSumVO(storeEvaluateSumByIdResponse.getStoreEvaluateSumVO());

        //店铺的关注总数
        StoreFollowCountBystoreIdResponse storeFollowCountBystoreIdResponse = storeCustomerFollowQueryProvider
                .queryStoreCustomerFollowCountByStoreId(StoreFollowBystoreIdRequest.builder().storeId(request.getStoreId())
                        .build()).getContext();
        response.setFollowCount(storeFollowCountBystoreIdResponse.getCount());

        //sku店铺的总数
        List<Integer> list = new ArrayList();
        list.add(AddedFlag.YES.toValue());
        list.add(AddedFlag.PART.toValue());
        GoodsInfoCountByConditionResponse goodsInfoCountByConditionResponse = goodsInfoQueryProvider.countByCondition(
                GoodsInfoCountByConditionRequest.builder().storeId(request.getStoreId()).delFlag(DeleteFlag.NO.toValue())
                        .addedFlags(list).auditStatus(CheckStatus.CHECKED)
                        .build()).getContext();
        response.setGoodsInfoCount(goodsInfoCountByConditionResponse.getCount());

//        //spu店铺的总数
//        GoodsCountByStoreIdResponse goodsCountByStoreIdResponse = goodsQueryProvider.countByStoreId(GoodsCountByStoreIdRequest
//                .builder().storeId(request.getStoreId()).build()).getContext();
//        response.setGoodsCount(goodsCountByStoreIdResponse.getGoodsCount());

        return BaseResponse.success(response);
    }

    /**
     * 查询操作人在店铺的会员等级与折扣信息
     *
     * @return
     */
    @ApiOperation(value = "查询操作人在店铺的会员等级与折扣信息")
    @RequestMapping(value = "/storeVip", method = RequestMethod.POST)
    public BaseResponse<StoreCustSystemResponse> queryStoreVip(@RequestBody StoreQueryRequest request) {
        if (request.getStoreId() == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        //1.查询店铺信息
        StoreVO store = storeQueryProvider.getValidStoreById(new ValidStoreByIdRequest(request.getStoreId()))
                .getContext().getStoreVO();

        //2.查询会员在商家的vip信息(会员等级与折扣)
        CustomerLevelVO level = null;
        if (store.getCompanyInfo() != null && store.getCompanyInfo().getCompanyInfoId() != null) {
            StoreCustomerRelaQueryRequest relaQueryRequest = new StoreCustomerRelaQueryRequest();
            relaQueryRequest.setQueryPlatform(false);
            relaQueryRequest.setCompanyInfoId(store.getCompanyInfo().getCompanyInfoId());
            relaQueryRequest.setCustomerId(commonUtil.getOperatorId());

            StoreCustomerRelaResponse customerBelong =
                    storeCustomerQueryProvider.getCustomerRelated(relaQueryRequest).getContext();
            //商家的会员
            if (customerBelong != null) {
                StoreLevelByIdRequest levelByIdRequest = StoreLevelByIdRequest.builder().storeLevelId(customerBelong.getStoreLevelId()).build();
                BaseResponse<StroeLevelInfoByIdResponse> response = storeLevelQueryProvider.queryStoreLevelInfoByStoreLevelId(levelByIdRequest);
                StroeLevelInfoByIdResponse context = response.getContext();
                if (Objects.nonNull(context)) {
                    //店铺里对应的等级与折扣
                    level = context.getCustomerLevelVO();
                    //去除敏感信息
                    level.setCreatePerson(null);
                    level.setCreateTime(null);
                    level.setUpdatePerson(null);
                    level.setUpdateTime(null);
                }
            } else if (store.getCompanyType().equals(BoolFlag.NO)) {
                CustomerGetByIdResponse context = customerQueryProvider.getCustomerById(new CustomerGetByIdRequest(commonUtil.getOperatorId())).getContext();
                level = customerLevelQueryProvider.getCustomerLevel(CustomerLevelGetRequest.builder().customerLevelId(context.getCustomerLevelId()).build()).getContext();
            }
        }

        //3.查询店铺的会员折扣信息列表
        BaseResponse<StroeLevelInfoResponse> response = storeLevelQueryProvider.queryStoreLevelInfo(StoreLevelByStoreIdRequest.builder().storeId(request.getStoreId()).build());
        StroeLevelInfoResponse context = response.getContext();
        List<CustomerLevelVO> levelList = Collections.emptyList();
        if (Objects.nonNull(context)) {
            levelList = context.getCustomerLevelVOList();
        }

        //TODO 去除敏感信息
        store.setContactPerson(null);
        if (Objects.nonNull(level)) {
            level.setCreatePerson(null);
            level.setCreateTime(null);
            level.setUpdatePerson(null);
            level.setUpdateTime(null);
        }

        levelList.forEach(levelObj->{
            levelObj.setCreatePerson(null);
            levelObj.setCreateTime(null);
            levelObj.setUpdatePerson(null);
            levelObj.setUpdateTime(null);
        });


        //4.组装店铺会员页的返回值
        return BaseResponse.success(new StoreCustSystemResponse(store, level, levelList));
    }


    /**
     * 查询操作人在店铺的会员等级与折扣信息
     *
     * @return
     */
    @ApiOperation(value = "查询操作人在店铺的会员等级与折扣信息")
    @RequestMapping(value = "/storeVipFront", method = RequestMethod.POST)
    public BaseResponse<StoreCustSystemResponse> queryStoreVipFront(@RequestBody StoreQueryRequest request) {
        if (request.getStoreId() == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        //1.查询店铺信息
        BaseResponse<ValidStoreByIdResponse> baseResponse =
                storeQueryProvider.getValidStoreById(ValidStoreByIdRequest.builder().storeId(request.getStoreId()).build());
        ValidStoreByIdResponse validStoreByIdResponse = baseResponse.getContext();
        StoreVO storeVO = validStoreByIdResponse.getStoreVO();

        //3.查询店铺的会员折扣信息列表
        BaseResponse<StroeLevelInfoResponse> baseResponse1 = storeLevelQueryProvider.queryStoreLevelInfo(StoreLevelByStoreIdRequest.builder().storeId(request.getStoreId()).build());

        StroeLevelInfoResponse response = baseResponse1.getContext();
        List<CustomerLevelVO> customerLevelVOList = response.getCustomerLevelVOList();

        //4.组装店铺会员页的返回值
        return BaseResponse.success(new StoreCustSystemResponse(storeVO, null, customerLevelVOList));
    }

    /**
     * 查询店铺主页信息(店铺主页用)
     *
     * @return
     */
    @ApiOperation(value = "查询店铺主页信息(店铺主页用)")
    @ApiImplicitParam(paramType = "path", dataType = "Long", name = "storeId", value = "店铺Id", required = true)
    @RequestMapping(value = "/homeInfo/{storeId}", method = RequestMethod.GET)
    public BaseResponse<StoreHomeInfoResponse> queryStore(@PathVariable Long storeId) {
//        StoreHomeResponse response = storeService.queryStoreHomeInfo(storeId);
        StoreHomeInfoResponse response =
                storeQueryProvider.getStoreHomeInfo(new StoreHomeInfoRequest(storeId)).getContext();

        Boolean isFollowed = storeCustomerFollowQueryProvider.queryStoreCustomerFollowByStoreId(
                StoreCustomerFollowExistsRequest.builder()
                        .storeId(storeId)
                        .customerId(commonUtil.getOperatorId()).build()
        ).getContext().getIsFollowed();
        response.setIsFollowed(isFollowed);
        return BaseResponse.success(response);
    }


    /**
     * 查询店铺主页信息(店铺主页用)
     *
     * @return
     */
    @ApiOperation(value = "查询店铺主页信息(店铺主页用)")
    @ApiImplicitParam(paramType = "path", dataType = "Long", name = "storeId", value = "店铺Id", required = true)
    @RequestMapping(value = "/homeInfoFront/{storeId}", method = RequestMethod.GET)
    public BaseResponse<StoreHomeInfoResponse> queryStoreFront(@PathVariable Long storeId) {
        StoreHomeInfoResponse response = storeQueryProvider.
                getStoreHomeInfo(StoreHomeInfoRequest.builder()
                        .storeId(storeId).build()).getContext();
        response.setIsFollowed(false);
        return BaseResponse.success(response);
    }

    /**
     * 进店赠券活动(已登陆)
     * @param customerId
     * @param storeId
     * @return
     */
    @ApiOperation(value = "进店赠券活动(已登陆)")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "String", name = "storeId", value = "店铺Id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "customerId", value = "客户Id", required = true)
    })
    @RequestMapping(value = "/storeCoupons/{storeId}/{customerId}", method = RequestMethod.GET)
    public BaseResponse<GetRegisterOrStoreCouponResponse> entryCoupons(@PathVariable String customerId, @PathVariable Long storeId){

        //获取三个月前的日期
        LocalDateTime date = LocalDateTime.of(LocalDate.now(), LocalTime.MIN).plusMonths(-3);
        //封装订单请求
        TradeListAllRequest tradeListAllRequest = TradeListAllRequest.builder()
                .tradeQueryDTO(TradeQueryDTO.builder()
                        .buyerId(customerId)
                        .storeId(storeId)
                        .beginTime(DateUtil.getDate(date))
                        .tradeState(TradeStateDTO.builder()
                                .payState(PayState.PAID)
                                .build())
                        .build())
                .build();
        //用户三个月内在该店铺有订单，返回一个空的response
        if(tradeQueryProvider.listAll(tradeListAllRequest).getContext().getTradeVOList().size() > 0){
            return BaseResponse.success(null);
        }
        //封装couponGroupRequest，获取一组优惠券
        GetCouponGroupRequest couponGroupRequest = new GetCouponGroupRequest();
        couponGroupRequest.setCustomerId(customerId);
        couponGroupRequest.setStoreId(storeId);
        couponGroupRequest.setType(CouponActivityType.STORE_COUPONS);
        return BaseResponse.success(couponActivityProvider.getCouponGroup(couponGroupRequest).getContext());
    }
}
