package com.wanmi.sbc.order.provider.impl.pointstrade;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.OrderType;
import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.provider.level.CustomerLevelQueryProvider;
import com.wanmi.sbc.customer.api.provider.store.StoreCustomerQueryProvider;
import com.wanmi.sbc.customer.api.provider.storelevel.StoreLevelQueryProvider;
import com.wanmi.sbc.customer.api.request.customer.CustomerGetByIdRequest;
import com.wanmi.sbc.customer.api.request.level.CustomerLevelByIdRequest;
import com.wanmi.sbc.customer.api.request.store.StoreCustomerRelaListByConditionRequest;
import com.wanmi.sbc.customer.api.request.storelevel.StoreLevelByIdRequest;
import com.wanmi.sbc.customer.api.response.customer.CustomerGetByIdResponse;
import com.wanmi.sbc.customer.api.response.level.CustomerLevelByIdResponse;
import com.wanmi.sbc.customer.bean.vo.StoreCustomerRelaVO;
import com.wanmi.sbc.customer.bean.vo.StoreLevelVO;
import com.wanmi.sbc.order.api.provider.pointstrade.PointsTradeQueryProvider;
import com.wanmi.sbc.order.api.request.pointstrade.PointsTradeGetByIdRequest;
import com.wanmi.sbc.order.api.request.pointstrade.PointsTradeListExportRequest;
import com.wanmi.sbc.order.api.request.pointstrade.PointsTradePageCriteriaRequest;
import com.wanmi.sbc.order.api.response.pointstrade.PointsTradeGetByIdResponse;
import com.wanmi.sbc.order.api.response.pointstrade.PointsTradeListExportResponse;
import com.wanmi.sbc.order.api.response.pointstrade.PointsTradePageCriteriaResponse;
import com.wanmi.sbc.order.bean.vo.PointsTradeVO;
import com.wanmi.sbc.order.bean.vo.TradeVO;
import com.wanmi.sbc.order.pointstrade.request.PointsTradeQueryRequest;
import com.wanmi.sbc.order.pointstrade.service.PointsTradeService;
import com.wanmi.sbc.order.returnorder.service.ReturnOrderService;
import com.wanmi.sbc.order.thirdplatformtrade.service.LinkedMallTradeService;
import com.wanmi.sbc.order.trade.model.root.ProviderTrade;
import com.wanmi.sbc.order.trade.model.root.Trade;
import com.wanmi.sbc.order.trade.request.ProviderTradeQueryRequest;
import com.wanmi.sbc.order.trade.service.ProviderTradeService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @ClassName PointsTradeQueryController
 * @Description 积分订单接口实现类
 * @Author lvzhenwei
 * @Date 2019/5/10 13:47
 **/
@Validated
@RestController
public class PointsTradeQueryController implements PointsTradeQueryProvider {

    @Autowired
    private PointsTradeService pointsTradeService;

    @Autowired
    private CustomerQueryProvider customerQueryProvider;

    @Autowired
    private CustomerLevelQueryProvider customerLevelQueryProvider;

    @Autowired
    private StoreCustomerQueryProvider storeCustomerQueryProvider;

    @Autowired
    private StoreLevelQueryProvider storeLevelQueryProvider;

    @Autowired
    private ProviderTradeService providerTradeService;

    @Autowired
    private LinkedMallTradeService thirdPlatformTradeService;

    /**
     * @return com.wanmi.sbc.common.base.BaseResponse<com.wanmi.sbc.order.api.response.pointstrade.PointsTradeGetByIdResponse>
     * @Author lvzhenwei
     * @Description 通过id获取订单信息
     * @Date 14:47 2019/5/10
     * @Param [pointsTradeGetByIdRequest]
     **/
    @Override
    public BaseResponse<PointsTradeGetByIdResponse> getById(@RequestBody @Valid PointsTradeGetByIdRequest pointsTradeGetByIdRequest) {
        Trade pointsTrade = pointsTradeService.getById(pointsTradeGetByIdRequest.getTid());
        if (Objects.nonNull(pointsTrade) && Objects.nonNull(pointsTrade.getBuyer()) && StringUtils.isNotEmpty(pointsTrade.getBuyer()
                .getAccount())) {
            pointsTrade.getBuyer().setAccount(ReturnOrderService.getDexAccount(pointsTrade.getBuyer().getAccount()));
            // 若是自营，获取平台等级，否则获取店铺等级
            if (pointsTrade.getSupplier().getIsSelf()) {
                CustomerGetByIdResponse getByIdResponse = customerQueryProvider.getCustomerById(new CustomerGetByIdRequest(pointsTrade.getBuyer().getId())).getContext();
                CustomerLevelByIdResponse levelByIdResponse = customerLevelQueryProvider.getCustomerLevelById(CustomerLevelByIdRequest.builder()
                        .customerLevelId(getByIdResponse.getCustomerLevelId())
                        .build()).getContext();
                pointsTrade.getBuyer().setLevelId(getByIdResponse.getCustomerLevelId());
                pointsTrade.getBuyer().setLevelName(levelByIdResponse.getCustomerLevelName());
            } else {
                Long storeId = pointsTrade.getSupplier().getStoreId();
                StoreCustomerRelaListByConditionRequest listByConditionRequest = new StoreCustomerRelaListByConditionRequest();
                listByConditionRequest.setCustomerId(pointsTrade.getBuyer().getId());
                listByConditionRequest.setStoreId(storeId);
                List<StoreCustomerRelaVO> relaVOList = storeCustomerQueryProvider.listByCondition(listByConditionRequest).getContext().getRelaVOList();
                if (Objects.nonNull(relaVOList) && relaVOList.size() > 0) {
                    StoreCustomerRelaVO storeCustomerRelaVO = relaVOList.get(0);
                    StoreLevelVO storeLevelVO = storeLevelQueryProvider.getById(StoreLevelByIdRequest.builder()
                            .storeLevelId(storeCustomerRelaVO.getStoreLevelId())
                            .build()).getContext().getStoreLevelVO();
                    pointsTrade.getBuyer().setLevelId(storeCustomerRelaVO.getStoreLevelId());
                    pointsTrade.getBuyer().setLevelName(storeLevelVO.getLevelName());
                }
            }
            pointsTrade.setBuyer(pointsTrade.getBuyer());
        }
        PointsTradeVO pointsTradeVO = KsBeanUtil.convert(pointsTrade, PointsTradeVO.class);
        //查询子订单
        List<ProviderTrade> providerTradeList = providerTradeService.queryAll(ProviderTradeQueryRequest.builder()
                .parentId(pointsTradeVO.getId()).orderType(OrderType.POINTS_ORDER).build());
        List<PointsTradeVO> providerVOList = KsBeanUtil.convert(providerTradeList, PointsTradeVO.class);
        pointsTradeVO.setPointsTradeVOList(providerVOList);

        // 查询linkedmall订单详情
        if(Boolean.TRUE.equals(pointsTradeGetByIdRequest.getNeedLmOrder()) && CollectionUtils.isNotEmpty(providerVOList) &&
                providerVOList.stream().anyMatch(t -> ThirdPlatformType.LINKED_MALL.equals(t.getThirdPlatformType()))) {
            // 填充并保存linkedmall订单配送信息
            TradeVO tradeVO = thirdPlatformTradeService.fillLinkedMallTradeDelivers(KsBeanUtil.convert(pointsTradeVO, TradeVO.class));
            pointsTradeVO = KsBeanUtil.convert(tradeVO,PointsTradeVO.class);
        }

        BaseResponse<PointsTradeGetByIdResponse> baseResponse = BaseResponse.success(PointsTradeGetByIdResponse.builder().
                pointsTradeVo(pointsTradeVO).build());
        return baseResponse;
    }

    /**
     * @return com.wanmi.sbc.common.base.BaseResponse<com.wanmi.sbc.order.api.response.pointstrade.PointsTradePageCriteriaResponse>
     * @Author lvzhenwei
     * @Description 根据查询条件分页查询订单信息
     * @Date 14:48 2019/5/10
     * @Param [pointsTradePageCriteriaRequest]
     **/
    @Override
    public BaseResponse<PointsTradePageCriteriaResponse> pageCriteria(@RequestBody @Valid PointsTradePageCriteriaRequest pointsTradePageCriteriaRequest) {
        //根据子单号或供应商名称查询时，先获取这些订单的父订单号
        if(StringUtils.isNotBlank(pointsTradePageCriteriaRequest.getPointsTradePageDTO().getProviderTradeId()) ||
        StringUtils.isNotBlank(pointsTradePageCriteriaRequest.getPointsTradePageDTO().getProviderName()) ||
                StringUtils.isNotBlank(pointsTradePageCriteriaRequest.getPointsTradePageDTO().getProviderCode())
        ) {
            List<String> parentIds = providerTradeService.findParentIdByCondition(ProviderTradeQueryRequest.builder()
                    .id(pointsTradePageCriteriaRequest.getPointsTradePageDTO().getProviderTradeId())
                    .providerName(pointsTradePageCriteriaRequest.getPointsTradePageDTO().getProviderName())
                    .providerCode(pointsTradePageCriteriaRequest.getPointsTradePageDTO().getProviderCode())
                    .orderType(OrderType.POINTS_ORDER)
                    .build());
            if(CollectionUtils.isNotEmpty(parentIds)){
                String[] ids = parentIds.toArray(new String[parentIds.size()]);
                pointsTradePageCriteriaRequest.getPointsTradePageDTO().setIds(ids);
            } else {
                return BaseResponse.success(PointsTradePageCriteriaResponse.builder().pointsTradePage(new MicroServicePage<PointsTradeVO>()).build());
            }
        }
        PointsTradeQueryRequest pointsTradeQueryRequest = KsBeanUtil.convert(
                pointsTradePageCriteriaRequest.getPointsTradePageDTO(), PointsTradeQueryRequest.class);
        Criteria criteria = pointsTradeQueryRequest.getWhereCriteria();
        Page<Trade> page = pointsTradeService.page(criteria, pointsTradeQueryRequest);
        MicroServicePage<PointsTradeVO> pointsTradeVOS = KsBeanUtil.convertPage(page, PointsTradeVO.class);

        //填充子订单数据
        if(CollectionUtils.isNotEmpty(pointsTradeVOS.getContent())){
            //查询所有的子订单
            List<String> parentIds = pointsTradeVOS.getContent().stream().map(PointsTradeVO::getId).collect(Collectors.toList());
            List<ProviderTrade> providerTradeList = providerTradeService.queryAll(ProviderTradeQueryRequest.builder()
                    .parentIds(parentIds).orderType(OrderType.POINTS_ORDER).build());
            if(CollectionUtils.isNotEmpty(providerTradeList)) {
                List<PointsTradeVO> pointsTradeVOList = pointsTradeVOS.getContent();
                //将子订单填充到相应的父订单中
                pointsTradeVOList.forEach(vo -> {
                    List<PointsTradeVO> items = new ArrayList<>();
                    for (ProviderTrade providerTrade: providerTradeList) {
                        if(vo.getId().equals(providerTrade.getParentId())) {
                            PointsTradeVO pointsTradeVO = KsBeanUtil.convert(providerTrade, PointsTradeVO.class);
                            items.add(pointsTradeVO);
                        }
                    }
                    vo.setPointsTradeVOList(items);
                });
                pointsTradeVOS.setContent(pointsTradeVOList);
            }
        }
        return BaseResponse.success(PointsTradePageCriteriaResponse.builder().pointsTradePage(pointsTradeVOS).build());
    }

    /**
     * @return com.wanmi.sbc.common.base.BaseResponse<com.wanmi.sbc.order.api.response.pointstrade.PointsTradeListExportResponse>
     * @Author lvzhenwei
     * @Description 查询积分订单导出数据
     * @Date 15:29 2019/5/10
     * @Param [pointsTradeListExportRequest]
     **/
    @Override
    public BaseResponse<PointsTradeListExportResponse> listPointsTradeExport(@RequestBody PointsTradeListExportRequest pointsTradeListExportRequest) {
        PointsTradeQueryRequest pointsTradeQueryRequest = KsBeanUtil.convert(pointsTradeListExportRequest.getPointsTradeQueryDTO(),
                PointsTradeQueryRequest.class);
        List<Trade> pointsTradeList = pointsTradeService.listPointsTradeExport(pointsTradeQueryRequest);
        return BaseResponse.success(PointsTradeListExportResponse.builder().
                pointsTradeVOList(KsBeanUtil.convert(pointsTradeList, PointsTradeVO.class)).build());
    }

    /**
     * @return com.wanmi.sbc.common.base.BaseResponse
     * @Author lvzhenwei
     * @Description 积分订单自动确认收货
     * @Date 11:39 2019/5/28
     * @Param []
     **/
    @Override
    public BaseResponse pointsOrderAutoReceive() {
        pointsTradeService.pointsOrderAutoReceive();
        return BaseResponse.SUCCESSFUL();
    }

}
