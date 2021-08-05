package com.wanmi.sbc.marketing.markup.service;

import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.bean.enums.EnterpriseCheckState;
import com.wanmi.sbc.customer.bean.vo.CommonLevelVO;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoViewByIdsRequest;
import com.wanmi.sbc.marketing.api.request.buyoutprice.MarketingBuyoutPriceSearchRequest;
import com.wanmi.sbc.marketing.api.request.market.MarketingIdRequest;
import com.wanmi.sbc.marketing.api.request.markup.MarkupLevelByIdRequest;
import com.wanmi.sbc.marketing.api.request.markup.MarkupLevelBySkuRequest;
import com.wanmi.sbc.marketing.api.request.markup.MarkupListRequest;
import com.wanmi.sbc.marketing.api.request.markuplevel.MarkupLevelQueryRequest;
import com.wanmi.sbc.marketing.api.response.markup.MarkupLevelByIdResponse;
import com.wanmi.sbc.marketing.api.response.markup.MarkupLevelBySkuResponse;
import com.wanmi.sbc.marketing.bean.enums.MarketingJoinLevel;
import com.wanmi.sbc.marketing.bean.enums.MarketingSubType;
import com.wanmi.sbc.marketing.bean.enums.MarketingType;
import com.wanmi.sbc.marketing.bean.vo.MarkupLevelVO;
import com.wanmi.sbc.marketing.common.model.root.Marketing;
import com.wanmi.sbc.marketing.common.model.root.MarketingScope;
import com.wanmi.sbc.marketing.common.repository.MarketingRepository;
import com.wanmi.sbc.marketing.common.repository.MarketingScopeRepository;
import com.wanmi.sbc.marketing.common.service.MarketingService;
import com.wanmi.sbc.marketing.markup.model.root.MarkupLevel;
import com.wanmi.sbc.marketing.markup.model.root.MarkupLevelDetail;
import com.wanmi.sbc.marketing.markup.repository.MarkupLevelDetailRepository;
import com.wanmi.sbc.marketing.markup.repository.MarkupLevelRepository;
import com.wanmi.sbc.marketing.markup.request.MarkupAllSaveRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>加价购活动业务逻辑</p>
 *
 * @author he
 * @date 2021-02-04 16:09:09
 */
@Service("MarkupService")
public class MarkupService {

    @Autowired
    private MarkupLevelRepository markupLevelRepository;

    @Autowired
    private MarkupLevelService markupLevelService;

    @Autowired
    private MarkupLevelDetailRepository markupLevelDetailRepository;
    @Autowired
    private MarketingScopeRepository marketingScopeRepository;
    @Autowired
    private MarketingRepository marketingRepository;
    @Autowired
    private MarketingService marketingService;
    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;


    /**
     * 新增加价购活动
     *
     * @author he
     */
    @Transactional(rollbackFor = Exception.class)
    public Marketing add(MarkupAllSaveRequest markupAllSaveRequest) {
        Marketing marketing = marketingService.addMarketing(markupAllSaveRequest);

        // 处理加价购阶梯
        saveLevelList(markupAllSaveRequest.generateMarkupLevelList(marketing.getMarketingId()));

        // 处理加价购阶梯详情
        saveLevelGiftDetailList(markupAllSaveRequest.generateMarkupLeveDetailList(markupAllSaveRequest.getMarkupLevelList()));

        return marketing;
    }


    /**
     * 保存加价购阶梯信息
     */
    private void saveLevelList(List<MarkupLevel> markupLevelList) {
        if (CollectionUtils.isNotEmpty(markupLevelList)) {
            markupLevelRepository.saveAll(markupLevelList);
        } else {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
    }

    /**
     * 保存多级优惠赠品信息
     */
    private void saveLevelGiftDetailList(List<MarkupLevelDetail> markupLevelDetailList) {
        if (CollectionUtils.isNotEmpty(markupLevelDetailList)) {
            markupLevelDetailRepository.saveAll(markupLevelDetailList);
        } else {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
    }

    /**
     * 修改加价购活动
     *
     * @author he
     */
    @Transactional(rollbackFor = Exception.class)
    public void modify(MarkupAllSaveRequest markupSaveRequest) {
        marketingService.modifyMarketing(markupSaveRequest);

        // 先删除活动的阶梯数据，然后再保存
        markupLevelRepository.deleteByMarkupId(markupSaveRequest.getMarketingId());
        this.saveLevelList(markupSaveRequest.generateMarkupLevelList(markupSaveRequest.getMarketingId()));

        // 先删除已有的阶梯详细信息，然后再保存
        markupLevelDetailRepository.deleteByMarkupId(markupSaveRequest.getMarketingId());
        this.saveLevelGiftDetailList(markupSaveRequest.generateMarkupLeveDetailList(markupSaveRequest.getMarkupLevelList()));

    }


    /**
     * 单个查询加价购活动
     *
     * @author he
     */
    public MarkupLevelByIdResponse getLevelById(MarkupLevelByIdRequest markupLevelByIdRequest) {
        MarkupLevelByIdResponse markupLevelByIdResponse = new MarkupLevelByIdResponse();
        markupLevelByIdResponse.setMarketingSubType(MarketingSubType.MARKUP);
        List<MarkupLevel> markupLevel = getMarkupLevel(markupLevelByIdRequest.getMarketingId());
        markupLevelByIdResponse.setLevelList(KsBeanUtil.convert(markupLevel, MarkupLevelVO.class));
        // 不存在 直接返回
        if (CollectionUtils.isEmpty(markupLevel)) {
            return markupLevelByIdResponse;
        }
        List<String> goodsInfoIds = markupLevel.stream().flatMap(level -> level.getMarkupLevelDetailList().stream())
                .map(detail -> detail.getGoodsInfoId()).collect(Collectors.toList());
        // 获取商品数据
        markupLevelByIdResponse.setGoodsInfoVOList(
                goodsInfoQueryProvider.listViewByIds(
                        GoodsInfoViewByIdsRequest.builder().goodsInfoIds(goodsInfoIds).isHavSpecText(Constants.yes).build()
                ).getContext().getGoodsInfos()
        );

        return markupLevelByIdResponse;
    }

    /**
     * 获取 加价购的详细信息
     *
     * @param marketingId
     * @return
     */
    public List<MarkupLevel> getMarkupLevel(Long marketingId) {
        MarkupLevelQueryRequest markupLevelQueryRequest = MarkupLevelQueryRequest.builder().markupId(marketingId).build();
        HashMap<String, String> sortMap = new HashMap<>();
        sortMap.put("levelAmount", "asc");
        markupLevelQueryRequest.setSortMap(sortMap);
        return markupLevelService.list(markupLevelQueryRequest);

    }

    /**
     * 批量获取 加价购的详细信息
     *
     * @param marketingId
     * @return
     */
    public List<MarkupLevel> getMarkupLevelList(List<Long> marketingId) {
        return markupLevelService.list(MarkupLevelQueryRequest.builder().markupIds(marketingId).build());

    }

    /**
     * 通过活动id 批量查询
     *
     * @param markupListRequest
     * @return
     */
    public List<MarkupLevelVO> getMarkupList(MarkupListRequest markupListRequest) {
        List list = Optional.ofNullable(markupLevelService.list(MarkupLevelQueryRequest
                .builder().markupIds(markupListRequest.getMarketingId()).build())).orElse(Collections.EMPTY_LIST);
        return KsBeanUtil.convert(list, MarkupLevelVO.class);
    }

    /**
     *  h5 端,获取需要展示的加价购信息
     * @param markupLevelBySkuRequest
     * @return
     */
    public MarkupLevelBySkuResponse getMarkupListBySku(MarkupLevelBySkuRequest markupLevelBySkuRequest) {
        MarkupLevelBySkuResponse markupLevelBySkuResponse = new MarkupLevelBySkuResponse();

        List<MarketingScope> marketingScopeList = marketingScopeRepository.getByGoodsInfoId(markupLevelBySkuRequest.getSkuIds());
        if (CollectionUtils.isEmpty(marketingScopeList)) {
            return markupLevelBySkuResponse;
        }
        // 所有关联活动
        List<Long> marktingIds = marketingScopeList.stream().map(d -> d.getMarketingId()).collect(Collectors.toList());
        List<Marketing> marketingRepositoryAllById = marketingRepository.findAllById(marktingIds);
        if (CollectionUtils.isEmpty(marketingRepositoryAllById)) {
            return markupLevelBySkuResponse;
        }
        // 过滤出加价购活动
        Map<Long, CommonLevelVO> levelMap = markupLevelBySkuRequest.getLevelMap();
        List<Long> markupMarktingIds = marketingRepositoryAllById.stream()
                .filter(marketing -> {
                            boolean b = MarketingType.MARKUP.equals(marketing.getMarketingType())
                                    && marketing.getIsPause() != BoolFlag.YES && marketing.getDelFlag() != DeleteFlag.YES
                                    && marketing.getEndTime().isAfter(LocalDateTime.now())
                                    && marketing.getBeginTime().isBefore(LocalDateTime.now());
                            if (!b) {
                                return b;
                            }
                            //校验用户级别
                            CommonLevelVO level = levelMap.get(marketing.getStoreId());
                            switch (marketing.getMarketingJoinLevel()) {
                                case ALL_CUSTOMER:
                                    break;
                                case ALL_LEVEL:
//                                    if (level == null) {
//                                        return false;
//                                    }
                                    break;
                                case LEVEL_LIST:
                                    if (level == null || !marketing.getJoinLevelList().contains(level.getLevelId())) {
                                        return false;
                                    }
                                    break;
                                case PAID_CARD_CUSTOMER:
                                    if (CollectionUtils.isEmpty(markupLevelBySkuRequest.getMarketingJoinLevelList())
                                            || !markupLevelBySkuRequest.getMarketingJoinLevelList().contains( MarketingJoinLevel.PAID_CARD_CUSTOMER)) {
                                        return false;
                                    }
                                    break;
                                case ENTERPRISE_CUSTOMER:
                                    if (CollectionUtils.isEmpty(markupLevelBySkuRequest.getMarketingJoinLevelList())
                                            || !markupLevelBySkuRequest.getMarketingJoinLevelList().contains( MarketingJoinLevel.ENTERPRISE_CUSTOMER)) {
                                        return false;
                                    }
                                    break;
                                default:
                                    break;

                            }
                            return true;
                        }
                )
                .map(m -> m.getMarketingId()).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(markupMarktingIds)) {
            return markupLevelBySkuResponse;
        }
        List<MarkupLevel> markupLevel = getMarkupLevelList(markupMarktingIds);
        Map<Long, List<String>> scopeMap = marketingRepositoryAllById.stream()
                .filter(marketing -> MarketingType.MARKUP.equals(marketing.getMarketingType()))
                .collect(Collectors.toMap(m -> m.getMarketingId(),
                        m -> m.getMarketingScopeList().stream().map(s -> s.getScopeId()).collect(Collectors.toList())));
        // 过滤出符合条件的加价购活动
        markupLevel = markupLevel.stream()
                .filter(l -> l.getLevelAmount().compareTo(markupLevelBySkuRequest.getLevelAmount()) <= 0)
                .collect(Collectors.toList());
        // 只保留一个阶梯
        HashMap<Long, MarkupLevel> markupLevelMap = new HashMap<>();
        for (MarkupLevel level : markupLevel) {
            MarkupLevel markupLevelInMap = markupLevelMap.get(level.getMarkupId());
            if(Objects.isNull(markupLevelInMap)){
                markupLevelMap.put(level.getMarkupId(),level);
                continue;
            }
            if(markupLevelInMap.getLevelAmount().compareTo(level.getLevelAmount())<0){
                markupLevelMap.put(level.getMarkupId(),level); 
            }
        }
        markupLevel=new ArrayList<>(markupLevelMap.values());
        if (CollectionUtils.isEmpty(markupLevel)) {
            return markupLevelBySkuResponse;
        }
        List<MarkupLevelVO> markupLevelVOS = KsBeanUtil.convert(markupLevel, MarkupLevelVO.class);
        markupLevelVOS.stream().forEach(v -> v.setSkuIds(scopeMap.get(v.getMarkupId())));
        markupLevelBySkuResponse.setLevelList(markupLevelVOS);
        // 不存在 直接返回
        if (CollectionUtils.isEmpty(markupLevel)) {
            return markupLevelBySkuResponse;
        }
        List<String> goodsInfoIds = markupLevel.stream().flatMap(level -> level.getMarkupLevelDetailList().stream())
                .map(detail -> detail.getGoodsInfoId()).collect(Collectors.toList());
        // 获取商品数据
        markupLevelBySkuResponse.setGoodsInfoVOList(
                goodsInfoQueryProvider.listViewByIds(
                        GoodsInfoViewByIdsRequest.builder().goodsInfoIds(goodsInfoIds).isHavSpecText(Constants.yes).build()
                ).getContext().getGoodsInfos()
        );
        return markupLevelBySkuResponse;
    }

    /**
     * 获取已参加换购的商品sku
     * @param marketingIdRequest
     * @return
     */
    public List<String> getMarkupSku(MarketingIdRequest marketingIdRequest) {

        MarketingBuyoutPriceSearchRequest marketingBuyoutPriceSearchRequest = new MarketingBuyoutPriceSearchRequest();
        marketingBuyoutPriceSearchRequest.setStoreId(marketingIdRequest.getStoreId());
        marketingBuyoutPriceSearchRequest.setMarketingSubType(MarketingSubType.MARKUP);
        marketingBuyoutPriceSearchRequest.setEndTimeBegin(LocalDateTime.now());
        marketingBuyoutPriceSearchRequest.setDelFlag(DeleteFlag.NO);
        List<Marketing> marketingList = marketingRepository.findAll(marketingService.getWhereCriteria(marketingBuyoutPriceSearchRequest));
        if(CollectionUtils.isEmpty(marketingList)){
            return Collections.EMPTY_LIST;
        }
       /* List<Long> marktingIds = marketingList.stream().map(m -> m.getMarketingId()).collect(Collectors.toList());
        List<MarkupLevelDetail> markupSku = markupLevelDetailRepository.getMarkupSku(marktingIds,marketingIdRequest.getMarketingId());
        if(CollectionUtils.isEmpty(markupSku)){
            return Collections.EMPTY_LIST;
        }*/

        return  marketingList.stream().flatMap(m->m.getMarketingScopeList().stream())
                .map(s->s.getScopeId()).collect(Collectors.toList());
    }
}
