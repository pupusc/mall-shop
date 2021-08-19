package com.wanmi.sbc.marketing.buyoutprice.service;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.level.CustomerLevelQueryProvider;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.provider.storelevel.StoreLevelQueryProvider;
import com.wanmi.sbc.customer.api.request.level.CustomerLevelListByCustomerLevelNameRequest;
import com.wanmi.sbc.customer.api.request.store.ListStoreByNameRequest;
import com.wanmi.sbc.customer.api.request.store.StoreByIdRequest;
import com.wanmi.sbc.customer.bean.dto.MarketingCustomerLevelDTO;
import com.wanmi.sbc.customer.bean.vo.MarketingCustomerLevelVO;
import com.wanmi.sbc.marketing.api.request.buyoutprice.MarketingBuyoutPriceIdRequest;
import com.wanmi.sbc.marketing.api.request.buyoutprice.MarketingBuyoutPriceSearchRequest;
import com.wanmi.sbc.marketing.api.response.buyoutprice.MarketingBuyoutPriceMarketingIdResponse;
import com.wanmi.sbc.marketing.bean.dto.MarketingBuyoutPriceLevelDTO;
import com.wanmi.sbc.marketing.bean.enums.MarketingSubType;
import com.wanmi.sbc.marketing.bean.vo.MarketingPageVO;
import com.wanmi.sbc.marketing.buyoutprice.model.entry.MarketingBuyoutPriceLevel;
import com.wanmi.sbc.marketing.buyoutprice.model.request.MarketingBuyoutPriceSaveRequest;
import com.wanmi.sbc.marketing.buyoutprice.repository.MarketingBuyoutPriceLevelRepository;
import com.wanmi.sbc.marketing.common.model.root.Marketing;
import com.wanmi.sbc.marketing.common.service.MarketingService;
import com.wanmi.sbc.marketing.halfpricesecondpiece.service.MarketingHalfPriceSecondPieceLevelService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 营销一口价业务
 */
@Service
public class MarketingBuyoutPriceService {

    @Autowired
    private MarketingBuyoutPriceLevelRepository marketingBuyoutPriceLevelRepository;

    @Autowired
    private MarketingService marketingService;

    @Autowired
    MarketingBuyoutPriceFullBaleLevelService buyoutPriceFullBaleLevelService;

    @Autowired
    private StoreQueryProvider storeQueryProvider;

    @Autowired
    private MarketingHalfPriceSecondPieceLevelService  marketingHalfPriceSecondPieceLevelService;

    @Autowired
    private CustomerLevelQueryProvider customerLevelQueryProvider;

    @Autowired
    private StoreLevelQueryProvider storeLevelQueryProvider;


    /**
     * 新增一口价
     */
    @Transactional(rollbackFor = Exception.class)
    public Marketing addMarketingBuyoutPrice(MarketingBuyoutPriceSaveRequest request) throws SbcRuntimeException {
        Marketing marketing = marketingService.addMarketing(request);

        // 保存多级优惠信息
        this.saveLevelList(request.generateBuyoutPriceLevelList(marketing.getMarketingId()));

        return marketing;
    }

    /**
     * 修改一口价
     */
    @Transactional(rollbackFor = Exception.class)
    public void modifyMarketingBuyoutPrice(MarketingBuyoutPriceSaveRequest request) throws SbcRuntimeException {
        //修改营销信息
        marketingService.modifyMarketing(request);

        // 先删除已有的多级优惠信息，然后再保存
        marketingBuyoutPriceLevelRepository.deleteByMarketingId(request.getMarketingId());
        //保存多节营销活动
        this.saveLevelList(request.generateBuyoutPriceLevelList(request.getMarketingId()));
    }


    /**
     * 保存多级优惠信息
     */
    private void saveLevelList(List<MarketingBuyoutPriceLevel> buyoutPriceLevelList) {
        if (CollectionUtils.isNotEmpty(buyoutPriceLevelList)) {
            marketingBuyoutPriceLevelRepository.saveAll(buyoutPriceLevelList);
        } else {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
    }

    /**
     * 查询一口价营销活动的详情
     *
     * @param request
     * @return
     */
    public MarketingBuyoutPriceMarketingIdResponse details(MarketingBuyoutPriceIdRequest request) {
        Marketing marketing = marketingService.queryById(request.getMarketingId());
        MarketingBuyoutPriceMarketingIdResponse marketingBuyoutPriceMarketing = KsBeanUtil.convert(marketing,
                MarketingBuyoutPriceMarketingIdResponse.class);
        //获取营销规则
        List<MarketingBuyoutPriceLevel> buyoutPriceLevel =
                marketingBuyoutPriceLevelRepository.findByMarketingId(request.getMarketingId());
        marketingBuyoutPriceMarketing.setMarketingBuyoutPriceLevelVO(KsBeanUtil.copyListProperties(buyoutPriceLevel,
                MarketingBuyoutPriceLevelDTO.class));
        return marketingBuyoutPriceMarketing;
    }


    /**
     * 根据字段模糊查询
     *
     * @param request
     * @return
     */
    public MicroServicePage<MarketingPageVO> search(MarketingBuyoutPriceSearchRequest request) {
        List<Long> storeIds = null;
        //获取店铺信息
        if (StringUtils.isNotBlank(request.getShopName())) {
            ListStoreByNameRequest stores = new ListStoreByNameRequest();
            stores.setStoreName(request.getShopName());
            storeIds = storeQueryProvider.listByName(stores).getContext().getStoreVOList().stream().map(store -> store.getStoreId()).collect(Collectors.toList());
        }
        Page<Marketing> page = marketingService.page(request, storeIds,
                request.getStoreId());
        MicroServicePage<MarketingPageVO> marketingPage = KsBeanUtil.convertPage(page,MarketingPageVO.class);
        if(request.getMarketingSubType()== MarketingSubType.BUYOUT_PRICE){
            //设置一口价营销规则
            buyoutPriceFullBaleLevelService.listFullBaleLevel(marketingPage.getContent());
            //设置店铺名称
//            this.getStoreName(marketingPage.getContent());
        }

        if(request.getMarketingSubType()== MarketingSubType.HALF_PRICE_SECOND_PIECE){
            //设置第二件半价营销规则
            marketingHalfPriceSecondPieceLevelService.halfPriceSecondPieceLevel(marketingPage.getContent());
            //设置店铺名称
//            this.getStoreName(marketingPage.getContent());
        }
        //填充目标客户+客户名称
        this.setCustomerLevelAndStoreName(marketingPage.getContent());

        return marketingPage;
    }

    /**
     * 查询关联的店铺信息
     *
     * @param marketingList
     */
    public void getStoreName(List<MarketingPageVO> marketingList) {
        for (MarketingPageVO marketingId : marketingList) {
            StoreByIdRequest storeByIdRequest = new StoreByIdRequest();
            if (marketingId.getStoreId() != null) {
                storeByIdRequest.setStoreId(marketingId.getStoreId());
                String storeName =
                        storeQueryProvider.getById(storeByIdRequest).getContext().getStoreVO().getStoreName();
                if (storeName != null) {
                    marketingId.setStoreName(storeName);
                }
            }

        }
    }

    /**
     * 查询目标客户
     * @param marketingList
     */
    private void setCustomerLevelAndStoreName(List<MarketingPageVO> marketingList){
        if(CollectionUtils.isEmpty(marketingList)){
            return;
        }
        List<MarketingCustomerLevelDTO> customerLevelDTOList = marketingList.stream().map(m -> {
            MarketingCustomerLevelDTO dto = new MarketingCustomerLevelDTO();
            dto.setMarketingId(m.getMarketingId());
            dto.setStoreId(m.getStoreId());
            dto.setJoinLevel(m.getJoinLevel());
            return dto;
        }).collect(Collectors.toList());
        List<MarketingCustomerLevelVO> marketingCustomerLevelVOList = customerLevelQueryProvider.listByCustomerLevelName(new CustomerLevelListByCustomerLevelNameRequest(customerLevelDTOList)).getContext().getCustomerLevelVOList();
        Map<Long,MarketingCustomerLevelVO> levelVOMap = marketingCustomerLevelVOList.stream().collect(Collectors.toMap(MarketingCustomerLevelVO::getMarketingId, Function.identity()));
        marketingList.stream().forEach(marketingPageVO -> {
            MarketingCustomerLevelVO levelVO = levelVOMap.get(marketingPageVO.getMarketingId());
            marketingPageVO.setLevelName(levelVO.getLevelName());
            marketingPageVO.setStoreName(levelVO.getStoreName());
        });

        //店铺信息
//        Map<Long, StoreVO> storeVOMap = storeQueryProvider.listByIds(ListStoreByIdsRequest.builder().storeIds(
//                marketingList.stream().map(m -> m.getStoreId()).collect(Collectors.toList())
//        ).build()).getContext().getStoreVOList().stream().collect(Collectors.toMap(StoreVO::getStoreId, v -> v));
//        //平台客户等级
//        List<CustomerLevelVO> customerLevelVOList = customerLevelQueryProvider.listAllCustomerLevel().getContext().getCustomerLevelVOList();
//
//        if(CollectionUtils.isNotEmpty(marketingList)){
//            marketingList.stream().forEach(marketingPageVO -> {
//                //填充客户等级名称
//                BoolFlag companyType = storeVOMap.get(marketingPageVO.getStoreId()).getCompanyType();
//                List<String> levels = Arrays.asList(marketingPageVO.getJoinLevel().split(","));
//                String levelName = "";
//                if(BoolFlag.NO.equals(companyType)){
//                    //平台
//                    if(CollectionUtils.isNotEmpty(customerLevelVOList) && CollectionUtils.isNotEmpty(levels)){
//                        levelName = levels.stream().flatMap(level -> customerLevelVOList.stream()
//                                .filter(customerLevelVO -> level.equals(customerLevelVO.getCustomerLevelId().toString()))
//                                .map(v -> v.getCustomerLevelName())).collect(Collectors.joining(","));
//                    }
//                }else{
//                    //商家
//                    StoreLevelListRequest storeLevelListRequest = StoreLevelListRequest.builder().storeId(marketingPageVO.getStoreId()).build();
//                    List<StoreLevelVO> storeLevelVOList = storeLevelQueryProvider
//                            .listAllStoreLevelByStoreId(storeLevelListRequest)
//                            .getContext().getStoreLevelVOList();
//                    if(CollectionUtils.isNotEmpty(storeLevelVOList) && CollectionUtils.isNotEmpty(levels)){
//                        levelName = levels.stream().flatMap(level -> storeLevelVOList.stream()
//                                .filter(storeLevelVO -> level.equals(storeLevelVO.getStoreLevelId().toString()))
//                                .map(v -> v.getLevelName())).collect(Collectors.joining(","));
//                    }
//                }
//                marketingPageVO.setLevelName(levelName);
//            });
//        }
    }
}
