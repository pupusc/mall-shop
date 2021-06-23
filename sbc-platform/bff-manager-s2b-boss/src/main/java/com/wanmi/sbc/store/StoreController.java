package com.wanmi.sbc.store;

import com.google.common.collect.Lists;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.EnableStatus;
import com.wanmi.sbc.common.enums.StoreType;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.store.StoreCustomerQueryProvider;
import com.wanmi.sbc.customer.api.provider.store.StoreProvider;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.provider.storelevel.StoreLevelQueryProvider;
import com.wanmi.sbc.customer.api.request.store.*;
import com.wanmi.sbc.customer.api.request.store.validGroups.StoreUpdate;
import com.wanmi.sbc.customer.api.request.storelevel.StoreLevelByStoreIdRequest;
import com.wanmi.sbc.customer.api.request.storelevel.StoreLevelListRequest;
import com.wanmi.sbc.customer.api.response.store.StoreBaseInfoResponse;
import com.wanmi.sbc.customer.api.response.store.StoreInfoResponse;
import com.wanmi.sbc.customer.api.response.storelevel.StoreLevelListResponse;
import com.wanmi.sbc.customer.api.response.storelevel.StroeLevelInfoResponse;
import com.wanmi.sbc.customer.bean.enums.CheckState;
import com.wanmi.sbc.customer.bean.enums.StoreState;
import com.wanmi.sbc.customer.bean.vo.CustomerLevelVO;
import com.wanmi.sbc.customer.bean.vo.StoreCustomerVO;
import com.wanmi.sbc.customer.bean.vo.StoreSimpleVO;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticProvider;
import com.wanmi.sbc.elastic.api.provider.storeInformation.EsStoreInformationProvider;
import com.wanmi.sbc.elastic.api.provider.storeInformation.EsStoreInformationQueryProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoRequest;
import com.wanmi.sbc.elastic.api.request.storeInformation.*;
import com.wanmi.sbc.goods.api.provider.distributor.goods.DistributorGoodsInfoProvider;
import com.wanmi.sbc.goods.api.provider.goods.GoodsQueryProvider;
import com.wanmi.sbc.goods.api.provider.pointsgoods.PointsGoodsQueryProvider;
import com.wanmi.sbc.goods.api.provider.pointsgoods.PointsGoodsSaveProvider;
import com.wanmi.sbc.goods.api.request.distributor.goods.DistributorGoodsInfoDeleteByStoreIdRequest;
import com.wanmi.sbc.goods.api.request.pointsgoods.PointsGoodsByStoreIdRequest;
import com.wanmi.sbc.goods.api.request.pointsgoods.PointsGoodsSwitchRequest;
import com.wanmi.sbc.goods.bean.vo.PointsGoodsVO;
import com.wanmi.sbc.mq.GoodsProducer;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: songhanlin
 * @Date: Created In 下午2:20 2017/11/2
 * @Description: 店铺信息Controller
 */
@Api(tags = "StoreController", description = "店铺信息相关API")
@RestController("bossStoreController")
@RequestMapping("/store")
public class StoreController {

    @Autowired
    private StoreQueryProvider storeQueryProvider;

    @Autowired
    private StoreProvider storeProvider;

    @Autowired
    private StoreBaseService baseService;

    @Autowired
    private StoreSelfService selfService;

    @Autowired
    private StoreCustomerQueryProvider storeCustomerQueryProvider;

    @Autowired
    private StoreLevelQueryProvider storeLevelQueryProvider;

    @Autowired
    private EsGoodsInfoElasticProvider esGoodsInfoElasticProvider;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    @Autowired
    private DistributorGoodsInfoProvider distributorGoodsInfoProvider;

    @Autowired
    private PointsGoodsQueryProvider pointsGoodsQueryProvider;

    @Autowired
    private PointsGoodsSaveProvider pointsGoodsSaveProvider;

    @Autowired
    private GoodsQueryProvider goodsQueryProvider;

    @Autowired
    private GoodsProducer goodsProducer;

    @Autowired
    private EsStoreInformationProvider esStoreInformationProvider;

    @Autowired
    private EsStoreInformationQueryProvider esStoreInformationQueryProvider;

    /**
     * 编辑店铺结算日期
     */
    @ApiOperation(value = "编辑店铺结算日期")
    @RequestMapping(value = "/days", method = RequestMethod.PUT)
    public BaseResponse<StoreVO> edit(@Valid @RequestBody AccountDateModifyRequest request) {
        StoreVO store = storeProvider.accountDateModify(request).getContext().getStoreVO();
        return BaseResponse.success(store);
    }


    /**
     * 查询店铺信息
     */
    @ApiOperation(value = "查询店铺信息")
    @ApiImplicitParam(paramType = "path", dataType = "Long", name = "storeId",
            value = "店铺Id", required = true)
    @RequestMapping(value = "/{storeId}", method = RequestMethod.GET)
    public BaseResponse<StoreVO> info(@PathVariable Long storeId) {
        StoreVO store = storeQueryProvider.getNoDeleteStoreById(new NoDeleteStoreByIdRequest(storeId))
                .getContext().getStoreVO();
        return BaseResponse.success(store);
    }

    /**
     * 根据商家id获取店铺信息
     *
     * @param companyInfoId
     * @return
     */
    @ApiOperation(value = "根据商家id获取店铺信息")
    @ApiImplicitParam(paramType = "path", dataType = "Long", name = "companyInfoId",
            value = "商家Id", required = true)
    @RequestMapping("/from/company/{companyInfoId}")
    public BaseResponse<StoreVO> fromCompanyInfoId(@PathVariable Long companyInfoId) {
        StoreVO store = storeProvider.initStoreByCompany(new InitStoreByCompanyRequest(companyInfoId))
                .getContext().getStoreVO();
        return BaseResponse.success(store);
    }

    /**
     * 开/关 店铺
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "开/关 店铺")
    @RequestMapping(value = "/close", method = RequestMethod.PUT)
    public BaseResponse<StoreVO> closeStore(@Valid @RequestBody StoreSwitchRequest request) {
        StoreVO store = storeProvider.switchStore(request).getContext().getStoreVO();

        //更新es店铺状态
        esStoreInformationProvider.modifyStoreState(StoreInfoStateModifyRequest
                .builder()
                .storeId(store.getStoreId())
                .storeState(request.getStoreState().toValue())
                .storeClosedReason(request.getStoreClosedReason())
                .build());

        Integer providerStatus = Constants.yes;
        if (StoreState.CLOSED.equals(request.getStoreState())){
            // 店铺关店。同时删除分销员关联的分销商品
            DistributorGoodsInfoDeleteByStoreIdRequest distributorGoodsInfoDeleteByStoreIdRequest =
                    new DistributorGoodsInfoDeleteByStoreIdRequest();
            distributorGoodsInfoDeleteByStoreIdRequest.setStoreId(request.getStoreId());
            distributorGoodsInfoProvider.deleteByStoreId(distributorGoodsInfoDeleteByStoreIdRequest);
            // 停用该店铺关联的积分商品
            List<PointsGoodsVO> pointsGoodsVOList = pointsGoodsQueryProvider.getByStoreId(PointsGoodsByStoreIdRequest.builder()
                    .storeId(store.getStoreId())
                    .build()).getContext().getPointsGoodsVOList();
            pointsGoodsVOList.forEach(pointsGoodsVO -> pointsGoodsSaveProvider.modifyStatus(PointsGoodsSwitchRequest.builder()
                    .pointsGoodsId(pointsGoodsVO.getPointsGoodsId())
                    .status(EnableStatus.DISABLE)
                    .build()));
            providerStatus = Constants.no;
        }

        if(StoreType.PROVIDER.equals(store.getStoreType())){
            //更新代销商品的店铺状态，刷新es
            goodsProducer.updateProviderStatus(providerStatus, Lists.newArrayList(request.getStoreId()));
        }else {
            esGoodsInfoElasticProvider.initEsGoodsInfo(EsGoodsInfoRequest.builder().storeId(request.getStoreId()).build());
        }

        //记录操作日志
        if (request.getStoreState() == StoreState.OPENING) {
            operateLogMQUtil.convertAndSend("商家", "开店",
                    "开店：商家编号" + store.getCompanyInfo().getCompanyCode());
        } else {
            operateLogMQUtil.convertAndSend("商家", "关店",
                    "关店：商家编号" + store.getCompanyInfo().getCompanyCode());
        }
        return BaseResponse.success(store);
    }

    /**
     * 通过/驳回 审核
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "通过/驳回 审核")
    @RequestMapping(value = "/reject", method = RequestMethod.PUT)
    public BaseResponse<StoreVO> rejectStore(@Valid @RequestBody StoreAuditRequest request) {
        StoreVO store = selfService.rejectOrPass(request);



        //记录操作日志
        if (request.getAuditState() == CheckState.CHECKED) {
            //同步es店铺信息
            StoreInfoRejectModifyRequest  storeInfoRequest = new StoreInfoRejectModifyRequest();
            KsBeanUtil.copyPropertiesThird(store,storeInfoRequest);
            esStoreInformationProvider.modifyStoreReject(storeInfoRequest);

            operateLogMQUtil.convertAndSend("商家", "审核商家",
                    "审核商家：商家编号" + store.getCompanyInfo().getCompanyCode());
        } else if (request.getAuditState() == CheckState.NOT_PASS) {
            //同步es店铺信息
            StoreInfoRejectModifyRequest  storeInfoRequest = new StoreInfoRejectModifyRequest();
            storeInfoRequest.setAuditState(request.getAuditState());
            storeInfoRequest.setAuditReason(request.getAuditReason());
            esStoreInformationProvider.modifyStoreReject(storeInfoRequest);

            operateLogMQUtil.convertAndSend("商家", "驳回商家",
                    "驳回商家：商家编号" + store.getCompanyInfo().getCompanyCode());
        }

        return BaseResponse.success(store);
    }

    /**
     * 查询店铺基本信息
     *
     * @return
     */
    @ApiOperation(value = "查询店铺基本信息")
    @ApiImplicitParam(paramType = "path", dataType = "Long", name = "storeId",
            value = "店铺Id", required = true)
    @RequestMapping(value = "/store-info/{storeId}", method = RequestMethod.GET)
    public BaseResponse<StoreInfoResponse> queryStore(@PathVariable Long storeId) {
        StoreInfoResponse storeInfoResponse = storeQueryProvider.getStoreInfoById(new StoreInfoByIdRequest(storeId))
                .getContext();
        return BaseResponse.success(storeInfoResponse);
    }

    /**
     * 修改店铺基本信息
     *
     * @param saveRequest
     * @return
     */
    @ApiOperation(value = "修改店铺基本信息")
    @RequestMapping(value = "/store-info", method = RequestMethod.PUT)
    public BaseResponse<StoreVO> updateStore(@Validated({StoreUpdate.class}) @RequestBody StoreSaveRequest saveRequest) {
        //saveRequest.setAccountType(AccountType.s2bSupplier);
        StoreVO store = baseService.updateStore(saveRequest);
        //更新数据到es
        StoreInfoModifyRequest storeInfoModifyRequest = new StoreInfoModifyRequest();
        KsBeanUtil.copyPropertiesThird(saveRequest,storeInfoModifyRequest);
        esStoreInformationProvider.modifyStoreBasicInfo(storeInfoModifyRequest);

        //记录操作日志
        operateLogMQUtil.convertAndSend("商家", "编辑商家信息",
                "编辑商家信息：商家编号" + store.getCompanyInfo().getCompanyCode());
        return BaseResponse.success(store);
    }

    /**
     * 修改签约日期和商家类型
     *
     * @param saveRequest
     * @return
     */
    @ApiOperation(value = "修改签约日期和商家类型")
    @RequestMapping(value = "/contract/date", method = RequestMethod.PUT)
    public BaseResponse<StoreVO> updateStoreContract(@RequestBody StoreContractModifyRequest saveRequest) {
        StoreVO store = storeProvider.modifyStoreContract(saveRequest).getContext().getStoreVO();
        //签约信息重新刷入es
        esStoreInformationProvider.modifyStoreContractInfo( KsBeanUtil.copyPropertiesThird(saveRequest,StoreInfoContractRequest.class));
        //如果店铺审核通过
        if (Objects.equals(CheckState.CHECKED.toValue(), store.getAuditState().toValue())) {
            //更新代销商品的店铺状态，刷新es
            goodsProducer.updateProviderStatus(Constants.yes, Lists.newArrayList(saveRequest.getStoreId()));
        }
        return BaseResponse.success(store);
    }

    /**
     * 查询店铺的会员信息，不区分会员的禁用状态
     * bail 2017-11-16
     *
     * @return 会员信息
     */
    @ApiOperation(value = "查询店铺的会员信息，不区分会员的禁用状态")
    @ApiImplicitParam(paramType = "path", dataType = "Long", name = "storeId",
            value = "店铺Id", required = true)
    @RequestMapping(value = "/allCustomers/{storeId}", method = RequestMethod.POST)
    public BaseResponse<List<StoreCustomerVO>> customers(@PathVariable Long storeId) {
        StoreCustomerQueryRequest request = new StoreCustomerQueryRequest();
        request.setStoreId(storeId);

        return BaseResponse.success(storeCustomerQueryProvider.listAllCustomer(request).getContext().getStoreCustomerVOList());
    }

    /**
     * 查询所有会员等级
     *
     * @return
     */
    @ApiOperation(value = "查询所有会员等级")
    @ApiImplicitParam(paramType = "path", dataType = "Long", name = "storeId",
            value = "店铺Id", required = true)
    @RequestMapping(value = "/levels/{storeId}", method = RequestMethod.GET)
    public BaseResponse<List<CustomerLevelVO>> levels(@PathVariable Long storeId) {
        BaseResponse<StroeLevelInfoResponse> stroeLevelInfoResponseBaseResponse
                = storeLevelQueryProvider.queryStoreLevelInfo(StoreLevelByStoreIdRequest.builder().storeId(storeId).build());
        StroeLevelInfoResponse context = stroeLevelInfoResponseBaseResponse.getContext();
        if (Objects.nonNull(context)) {
            return BaseResponse.success(context.getCustomerLevelVOList());
        }
        return BaseResponse.success(Collections.emptyList());
    }

    @ApiOperation(value = "根据店铺名称模糊匹配店铺列表，自动关联5条信息", notes = "返回Map, 以店铺Id为key, 店铺名称为value")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "storeName", value = "店铺名称", required = true)
    @RequestMapping(value = "/name", method = RequestMethod.GET)
    public BaseResponse<Map<Long, String>> queryStoreByNameForAutoComplete(@RequestParam("storeName") String storeName) {
        List<StoreVO> storeList = storeQueryProvider.listByNameForAutoComplete(
                ListStoreByNameForAutoCompleteRequest.builder().storeName(storeName).build()).getContext().getStoreVOList();
        Map<Long, String> storeMap = new HashMap<>();
        storeList.stream().forEach(store -> storeMap.put(store.getStoreId(), store.getStoreName()));
        return BaseResponse.success(storeMap);
    }

    @ApiOperation(value = "根据店铺名称模糊匹配店铺列表，自动关联5条信息", notes = "返回Map, 以店铺Id为key, 店铺名称为value")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "storeName", value = "店铺名称", required = true)
    @RequestMapping(value = "/old/provider/name", method = RequestMethod.GET)
    public BaseResponse<Map<Long, String>> queryProviderStoreByNameForAutoComplete(@RequestParam("storeName") String storeName) {
        List<StoreVO> storeList = storeQueryProvider.listByNameForAutoComplete(ListStoreByNameForAutoCompleteRequest.builder().storeName(storeName).storeType(StoreType.PROVIDER).build()).getContext().getStoreVOList();
        Map<Long, String> storeMap = new HashMap<>();
        storeList.stream().forEach(store -> storeMap.put(store.getStoreId(), store.getStoreName()));
        return BaseResponse.success(storeMap);
    }

    @ApiOperation(value = "根据店铺名称模糊匹配店铺列表，自动关联5条信息", notes = "返回Map, 以店铺Id为key, 店铺名称为value")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "storeName", value = "店铺名称", required = true)
    @RequestMapping(value = "/provider/name", method = RequestMethod.GET)
    public BaseResponse<Map<Long, String>> queryProviderStoreByNameForAutoCompleteForEs(@RequestParam("storeName") String storeName) {
        List<StoreVO> storeList = esStoreInformationQueryProvider.queryStoreByNameAndStoreTypeForAutoComplete(
                StoreInfoQueryPageRequest
                        .builder()
                        .storeName(storeName)
                        .storeType(StoreType.PROVIDER)
                        .build()).getContext().getStoreVOList();
        Map<Long, String> storeMap = new HashMap<>();
        storeList.stream().forEach(store -> storeMap.put(store.getStoreId(), store.getStoreName()));
        return BaseResponse.success(storeMap);
    }

    @ApiOperation(value = "根据店铺名称模糊匹配店铺列表，自动关联5条信息", notes = "返回Map, 以店铺Id为key, 店铺名称为value")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "storeName", value = "店铺名称", required = true)
    @RequestMapping(value = "/old/supplier/name", method = RequestMethod.GET)
    public BaseResponse<Map<Long, String>> querySupplierStoreByNameForAutoComplete(@RequestParam("storeName") String storeName) {
        List<StoreVO> storeList = storeQueryProvider.listByNameForAutoComplete(ListStoreByNameForAutoCompleteRequest.builder().storeName(storeName).storeType(StoreType.SUPPLIER).build()).getContext().getStoreVOList();
        Map<Long, String> storeMap = new HashMap<>();
        storeList.stream().forEach(store -> storeMap.put(store.getStoreId(), store.getStoreName()));
        return BaseResponse.success(storeMap);
    }

    @ApiOperation(value = "根据店铺名称模糊匹配店铺列表，自动关联5条信息", notes = "返回Map, 以店铺Id为key, 店铺名称为value")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "storeName", value = "店铺名称", required = true)
    @RequestMapping(value = "/supplier/name", method = RequestMethod.GET)
    public BaseResponse<Map<Long, String>> querySupplierStoreByNameForAutoCompleteForEs(@RequestParam("storeName") String storeName) {
        List<StoreVO> storeList = esStoreInformationQueryProvider.queryStoreByNameAndStoreTypeForAutoComplete(
                StoreInfoQueryPageRequest
                        .builder()
                        .storeName(storeName)
                        .storeType(StoreType.SUPPLIER)
                        .build()).getContext().getStoreVOList();
        Map<Long, String> storeMap = new HashMap<>();
        storeList.stream().forEach(store -> storeMap.put(store.getStoreId(), store.getStoreName()));
        return BaseResponse.success(storeMap);
    }

    /**
     * 店铺列表
     */
    @ApiOperation(value = "店铺列表")
    @RequestMapping(method = RequestMethod.GET)
    public BaseResponse<List<StoreBaseInfoResponse>> list() {
        ListStoreRequest queryRequest = new ListStoreRequest();
        queryRequest.setAuditState(CheckState.CHECKED);
        queryRequest.setStoreState(StoreState.OPENING);
        queryRequest.setGteContractStartDate(LocalDateTime.now());
        queryRequest.setLteContractEndDate(LocalDateTime.now());
        List<StoreBaseInfoResponse> list =
                storeQueryProvider.listStore(queryRequest).getContext().getStoreVOList().stream().map(s -> {
                    StoreBaseInfoResponse response = new StoreBaseInfoResponse().convertFromEntity(s);
                    return response;
                }).collect(Collectors.toList());
        return BaseResponse.success(list);
    }


    /**
     * 店铺列表
     */
    @ApiOperation(value = "店铺列表")
    @RequestMapping(value="/listByName",method = RequestMethod.POST)
    public BaseResponse<List<StoreSimpleVO>> listByName(@RequestBody StorePageRequest pageRequest) {
        pageRequest.setAuditState(CheckState.CHECKED);
        pageRequest.setStoreState(StoreState.OPENING);
        MicroServicePage<StoreVO> page = storeQueryProvider.page(pageRequest).getContext().getStoreVOPage();
        List<StoreSimpleVO> list = null;
        if(page.getTotalElements()>0){
            list =KsBeanUtil.convert(page.getContent(), StoreSimpleVO.class);
        }
        return BaseResponse.success(ListUtils.emptyIfNull(list));
    }

    /**
     * 查询店铺等级信息
     *
     * @param storeId
     * @return
     */
    @ApiOperation(value = "查询店铺等级信息")
    @ApiImplicitParam(paramType = "path", dataType = "Long", name = "storeId", value = "店铺Id", required = true)
    @RequestMapping(value = "/level/list/{storeId}", method = RequestMethod.GET)
    public BaseResponse<StoreLevelListResponse> queryStoreLevelList(@PathVariable Long storeId) {
        StoreLevelListRequest request = StoreLevelListRequest.builder().storeId(storeId).build();
        return BaseResponse.success(storeLevelQueryProvider.listAllStoreLevelByStoreId(request).getContext());
    }
}
