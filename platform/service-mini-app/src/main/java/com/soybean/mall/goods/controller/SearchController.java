package com.soybean.mall.goods.controller;
import com.alibaba.fastjson.JSON;
import com.soybean.elastic.api.enums.SearchBookListCategoryEnum;
import com.soybean.elastic.api.enums.SearchSpuNewAggsCategoryEnum;
import com.soybean.elastic.api.enums.SearchSpuNewCategoryEnum;
import com.soybean.elastic.api.enums.SearchSpuNewSortTypeEnum;

import com.soybean.elastic.api.provider.booklistmodel.EsBookListModelProvider;
import com.soybean.elastic.api.provider.spu.EsSpuNewProvider;
import com.soybean.elastic.api.resp.EsBookListModelResp;
import com.soybean.common.resp.CommonPageResp;
import com.soybean.elastic.api.resp.EsSpuNewAggResp;
import com.soybean.elastic.api.resp.EsSpuNewResp;
import com.soybean.mall.cart.vo.PromoteFitGoodsResultVO;
import com.soybean.mall.cart.vo.PromoteGoodsParamVO;
import com.soybean.mall.cart.vo.PromoteGoodsResultVO;
import com.soybean.mall.common.CommonUtil;
import com.soybean.mall.goods.req.KeyWordBookListQueryReq;
import com.soybean.mall.goods.req.KeyWordQueryReq;
import com.soybean.mall.goods.req.KeyWordSpuQueryReq;
import com.soybean.mall.goods.response.BookListSpuResp;
import com.soybean.mall.goods.response.GoodsSearchBySpuIdResponse;
import com.soybean.mall.goods.response.SearchHomeResp;
import com.soybean.mall.goods.response.SpuNewBookListResp;
import com.soybean.mall.goods.service.BookListSearchService;
import com.soybean.mall.goods.service.SpuComponentService;
import com.soybean.mall.goods.service.SpuNewSearchService;
import com.wanmi.sbc.bookmeta.bo.GoodsNameBySpuIdBO;
import com.wanmi.sbc.bookmeta.provider.GoodsSearchKeyProvider;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.common.base.Page;
import com.wanmi.sbc.common.enums.ChannelType;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.customer.CustomerProvider;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.request.customer.CustomerGetByIdRequest;
import com.wanmi.sbc.customer.api.response.customer.CustomerGetByIdResponse;
import com.wanmi.sbc.customer.bean.dto.CounselorDto;
import com.wanmi.sbc.customer.bean.enums.StoreState;
import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticQueryProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoQueryRequest;
import com.wanmi.sbc.elastic.api.response.goods.EsGoodsInfoResponse;
import com.wanmi.sbc.elastic.bean.vo.goods.EsGoodsInfoVO;
import com.wanmi.sbc.goods.api.enums.GoodsBlackListCategoryEnum;
import com.wanmi.sbc.goods.api.provider.blacklist.GoodsBlackListProvider;
import com.wanmi.sbc.goods.api.provider.spec.GoodsInfoSpecDetailRelQueryProvider;
import com.wanmi.sbc.goods.api.request.blacklist.GoodsBlackListPageProviderRequest;
import com.wanmi.sbc.goods.api.request.spec.GoodsInfoSpecDetailRelBySpuIdsRequest;
import com.wanmi.sbc.goods.api.response.blacklist.GoodsBlackListPageProviderResponse;
import com.wanmi.sbc.goods.api.response.spec.GoodsInfoSpecDetailRelBySpuIdsResponse;
import com.wanmi.sbc.goods.bean.enums.AddedFlag;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoSpecDetailRelVO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.marketing.api.provider.market.MarketingQueryProvider;
import com.wanmi.sbc.marketing.api.request.market.MarketingGetByIdRequest;
import com.wanmi.sbc.marketing.api.response.market.MarketingGetByIdForCustomerResponse;
import com.wanmi.sbc.marketing.bean.enums.MarketingSubType;
import com.wanmi.sbc.marketing.bean.vo.MarketingForEndVO;
import com.wanmi.sbc.marketing.bean.vo.MarketingScopeVO;
import com.wanmi.sbc.order.api.provider.purchase.PurchaseQueryProvider;
import com.wanmi.sbc.order.api.request.purchase.PurchaseInfoRequest;
import com.wanmi.sbc.order.api.response.purchase.PurchaseListResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Description: 搜索 h5 小程序 公共部分
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/9 2:19 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/

@RequestMapping("/search")
@RestController
@Slf4j
public class SearchController {

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Autowired
    private EsBookListModelProvider esBookListModelProvider;

    @Autowired
    private EsSpuNewProvider esSpuNewProvider;

    @Autowired
    private BookListSearchService bookListSearchService;

    @Resource
    private GoodsSearchKeyProvider goodsSearchKeyProvider;

    @Autowired
    private SpuNewSearchService spuNewSearchService;

    @Autowired
    private MarketingQueryProvider marketingQueryProvider;

    @Autowired
    private CustomerQueryProvider customerQueryProvider;

    @Autowired
    private PurchaseQueryProvider purchaseQueryProvider;

    @Autowired
    private SpuComponentService spuComponentService;

    @Autowired
    private EsGoodsInfoElasticQueryProvider esGoodsInfoElasticQueryProvider;

    @Autowired
    private GoodsInfoSpecDetailRelQueryProvider goodsInfoSpecDetailRelQueryProvider;

    @Autowired
    private CustomerProvider customerProvider;

    @Autowired
    private CommonUtil commonUtil;

    /**
     * 前端 关键词搜索
     *  @menu 搜索功能
     * @return
     */
    @PostMapping("/keyword")
    public BaseResponse<SearchHomeResp> keywordSearch(@RequestBody KeyWordQueryReq keyWordQueryReq) {
        SearchHomeResp searchHomeResp = new SearchHomeResp();
        try {
            //图书
            KeyWordSpuQueryReq spuKeyWordQueryReq = new KeyWordSpuQueryReq();
            spuKeyWordQueryReq.setChannelTypes(Collections.singletonList(commonUtil.getTerminal().getCode()));
            spuKeyWordQueryReq.setKeyword(keyWordQueryReq.getKeyword());
            spuKeyWordQueryReq.setSearchSpuNewCategory(SearchSpuNewCategoryEnum.BOOK.getCode());
            spuKeyWordQueryReq.setSpuSortType(SearchSpuNewSortTypeEnum.DEFAULT.getCode());
            CommonPageResp<List<SpuNewBookListResp>> bookPage = this.keywordSpuSearch(spuKeyWordQueryReq).getContext().getResult();
            searchHomeResp.setBooks(new SearchHomeResp.SubSearchHomeResp<>("图书", bookPage));
        } catch (Exception ex) {
            log.error("SearchController keywordSearch book", ex);
        }

        try {
            //商品
            KeyWordSpuQueryReq spuKeyWordQueryReq = new KeyWordSpuQueryReq();
            spuKeyWordQueryReq.setChannelTypes(Collections.singletonList(commonUtil.getTerminal().getCode()));
            spuKeyWordQueryReq.setKeyword(keyWordQueryReq.getKeyword());
            spuKeyWordQueryReq.setSearchSpuNewCategory(SearchSpuNewCategoryEnum.SPU.getCode());
            spuKeyWordQueryReq.setSpuSortType(SearchSpuNewSortTypeEnum.DEFAULT.getCode());
            CommonPageResp<List<SpuNewBookListResp>> spuPage = this.keywordSpuSearch(spuKeyWordQueryReq).getContext().getResult();
            searchHomeResp.setSpus(new SearchHomeResp.SubSearchHomeResp<>("商品", spuPage));
        } catch (Exception ex) {
            log.error("SearchController keywordSearch spu", ex);
        }

        //查询榜单
        try {
            KeyWordBookListQueryReq bookListKeyWordQueryReq = new KeyWordBookListQueryReq();
            bookListKeyWordQueryReq.setSpuNum(3);
            bookListKeyWordQueryReq.setPageNum(1);
            bookListKeyWordQueryReq.setPageSize(5);
            bookListKeyWordQueryReq.setSearchBookListCategory(SearchBookListCategoryEnum.RANKING_LIST.getCode());
            bookListKeyWordQueryReq.setKeyword(keyWordQueryReq.getKeyword());
            CommonPageResp<List<BookListSpuResp>> rankingListPage = this.keywordBookListSearch(bookListKeyWordQueryReq).getContext();
            searchHomeResp.setRankingLists(new SearchHomeResp.SubSearchHomeResp<>("榜单", rankingListPage));
        } catch (Exception ex) {
            log.error("SearchController keywordSearch ranking", ex);
        }


        //查询书单
        try {
            KeyWordBookListQueryReq bookListKeyWordQueryReq = new KeyWordBookListQueryReq();
            bookListKeyWordQueryReq.setSpuNum(5);
            bookListKeyWordQueryReq.setPageNum(1);
            bookListKeyWordQueryReq.setPageSize(5);
            bookListKeyWordQueryReq.setSearchBookListCategory(SearchBookListCategoryEnum.BOOK_LIST.getCode());
            bookListKeyWordQueryReq.setKeyword(keyWordQueryReq.getKeyword());
            CommonPageResp<List<BookListSpuResp>> bookListPage = this.keywordBookListSearch(bookListKeyWordQueryReq).getContext();
            searchHomeResp.setBookLists(new SearchHomeResp.SubSearchHomeResp<>("书单", bookListPage));
        } catch (Exception ex) {
            log.error("SearchController keywordSearch book_list", ex);
        }

        return BaseResponse.success(searchHomeResp);
    }

    /**
     * 前端 获取书单/榜单
     * @menu 搜索功能
     * @param request
     * @return
     */
    @PostMapping("/keyword/keywordBookListSearch")
    public BaseResponse<CommonPageResp<List<BookListSpuResp>>> keywordBookListSearch(@Validated @RequestBody KeyWordBookListQueryReq request) {

        CommonPageResp<List<EsBookListModelResp>> context = esBookListModelProvider.listKeyWorldEsBookListModel(request).getContext();
        List<BookListSpuResp> bookListSpuResps = bookListSearchService.listBookListSearch(context.getContent(), request.getSpuNum());
        CommonPageResp<List<BookListSpuResp>> commonPageResp = new CommonPageResp<>(context.getTotal(), bookListSpuResps);
        return BaseResponse.success(commonPageResp);
    }


    /**
     * 图书商品
     * @param request
     * @return
     */
    private EsSpuNewAggResp<List<SpuNewBookListResp>> spuSearch(KeyWordSpuQueryReq request) {
        request.setChannelTypes(Collections.singletonList(commonUtil.getTerminal().getCode()));
        //获取搜索黑名单
        List<String> unSpuIds = spuComponentService.listSearchBlackList(
                Arrays.asList(GoodsBlackListCategoryEnum.GOODS_SESRCH_H5_AT_INDEX.getCode(), GoodsBlackListCategoryEnum.GOODS_SESRCH_AT_INDEX.getCode()));
        request.setUnSpuIds(unSpuIds);

        //获取是否知识顾问用户
        //获取客户信息
        CustomerGetByIdResponse customer = null;
        String userId = commonUtil.getOperatorId();
        if (!StringUtils.isEmpty(userId)) {
            customer = customerQueryProvider.getCustomerById(new CustomerGetByIdRequest(userId)).getContext();
            String isCounselor = customerProvider.isCounselorCache(Integer.valueOf(customer.getFanDengUserNo())).getContext();
            //非知识顾问用户
            if (!Objects.isNull(isCounselor) && "true".equals(isCounselor)) {
                request.setCpsSpecial(1);// 表示知识顾问，显示所有商品
            }
        }

        EsSpuNewAggResp<List<EsSpuNewResp>> esSpuNewAggResp = esSpuNewProvider.listKeyWorldEsSpu(request).getContext();
        List<SpuNewBookListResp> spuNewBookListResps = spuNewSearchService.listSpuNewSearch(esSpuNewAggResp.getResult().getContent(), customer);
        EsSpuNewAggResp<List<SpuNewBookListResp>> result = new EsSpuNewAggResp<>();
        result.setReq(esSpuNewAggResp.getReq());
        result.setAggsCategorys(esSpuNewAggResp.getAggsCategorys());
        result.setReq(esSpuNewAggResp.getReq());
        result.setResult(new CommonPageResp<>(esSpuNewAggResp.getResult().getTotal(), spuNewBookListResps));
        return result;
    }

    /**
     * 图书商品V2
     * @param request
     * @return
     */
    private EsSpuNewAggResp<List<SpuNewBookListResp>> spuSearchV2(KeyWordSpuQueryReq request) {
        request.setChannelTypes(Collections.singletonList(commonUtil.getTerminal().getCode()));
        //获取搜索黑名单
        List<String> unSpuIds = spuComponentService.listSearchBlackList(
                Arrays.asList(GoodsBlackListCategoryEnum.GOODS_SESRCH_H5_AT_INDEX.getCode(), GoodsBlackListCategoryEnum.GOODS_SESRCH_AT_INDEX.getCode()));
        request.setUnSpuIds(unSpuIds);

        List<GoodsNameBySpuIdBO> goodsNameBySpuId=new ArrayList<>();
        //获取是否知识顾问用户
        //获取客户信息
        CustomerGetByIdResponse customer = null;
        String userId = commonUtil.getOperatorId();
        if (!StringUtils.isEmpty(userId)) {
            customer = customerQueryProvider.getCustomerById(new CustomerGetByIdRequest(userId)).getContext();
            String isCounselor = customerProvider.isCounselorCache(Integer.valueOf(customer.getFanDengUserNo())).getContext();
            //非知识顾问用户
            if (!Objects.isNull(isCounselor) && "true".equals(isCounselor)) {
                request.setCpsSpecial(1);// 表示知识顾问，显示所有商品
            }
        }
        EsSpuNewAggResp<List<SpuNewBookListResp>> result = new EsSpuNewAggResp<>();
        if(null!=request.getKeyword()&&!request.getKeyword().equals("")) {
            goodsNameBySpuId = goodsSearchKeyProvider.getGoodsNameBySpuId(request.getKeyword());
        }
        if(!CollectionUtils.isEmpty(goodsNameBySpuId)){
            GoodsNameBySpuIdBO goodsNameBySpuIdBO = goodsNameBySpuId.get(0);
            List<String> spuIds=new ArrayList<>();
            spuIds.add(goodsNameBySpuIdBO.getSpuId());
            KeyWordSpuQueryReq req=new KeyWordSpuQueryReq();
            req.setSpuIds(spuIds);
            req.setDelFlag(request.getDelFlag());
            EsSpuNewAggResp<List<EsSpuNewResp>> esSpuNewAggResp = esSpuNewProvider.listKeyWorldEsSpuBySpuId(req).getContext();
            List<SpuNewBookListResp> spuNewBookListResps = spuNewSearchService.listSpuNewSearch(esSpuNewAggResp.getResult().getContent(), customer);
            if(!CollectionUtils.isEmpty(spuNewBookListResps)){
                result.setKeyWordGoods(spuNewBookListResps);
            }
        }
        if(null!=request.getMarketingLabelId()){
            PromoteGoodsParamVO paramVO=new PromoteGoodsParamVO();
            paramVO.setId(request.getMarketingLabelId().toString());
            List<String> marketingGoods = marketingGoods(paramVO);
            request.setSpuIds(marketingGoods);
            EsSpuNewAggResp.PromoteInfo promoteInfo =getPromoteInfo(paramVO);
            EsSpuNewAggResp<List<EsSpuNewResp>> esSpuNewAggResp = esSpuNewProvider.listKeyWorldEsSpu(request).getContext();
            List<SpuNewBookListResp> spuNewBookListResps = spuNewSearchService.listSpuNewSearch(esSpuNewAggResp.getResult().getContent(), customer);
            handCart4FitGoods(spuNewBookListResps,customer);
            result.setPromoteInfo(promoteInfo);
            result.setReq(esSpuNewAggResp.getReq());
            result.setAggsCategorys(esSpuNewAggResp.getAggsCategorys());
            result.setResult(new CommonPageResp<>(esSpuNewAggResp.getResult().getTotal(), spuNewBookListResps));
            return result;
        }
        EsSpuNewAggResp<List<EsSpuNewResp>> esSpuNewAggResp = esSpuNewProvider.listKeyWorldEsSpu(request).getContext();
        List<SpuNewBookListResp> spuNewBookListResps = spuNewSearchService.listSpuNewSearch(esSpuNewAggResp.getResult().getContent(), customer);
        result.setReq(esSpuNewAggResp.getReq());
        result.setAggsCategorys(esSpuNewAggResp.getAggsCategorys());
        result.setResult(new CommonPageResp<>(esSpuNewAggResp.getResult().getTotal(), spuNewBookListResps));
        return result;
    }

    /**
     * 获取凑单活动商品
     * @param paramVO
     * @return
     */
    public List<String> marketingGoods(PromoteGoodsParamVO paramVO) {
        MarketingGetByIdRequest mktParam = new MarketingGetByIdRequest();
        mktParam.setMarketingId(Long.valueOf(paramVO.getId()));
        BaseResponse<MarketingGetByIdForCustomerResponse> mktResp = marketingQueryProvider.getByIdForCustomer(mktParam);
        if (mktResp == null || mktResp.getContext() == null || mktResp.getContext().getMarketingForEndVO() == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR, "指定的营销活动不存在");
        }
        MarketingForEndVO mkt = mktResp.getContext().getMarketingForEndVO();
        //按照h5方式搜索
        EsGoodsInfoQueryRequest queryRequest = new EsGoodsInfoQueryRequest();
        queryRequest.setGoodsInfoIds(mkt.getMarketingScopeList().stream().map(MarketingScopeVO::getScopeId).collect(Collectors.toList()));

        List<String> spus = getPromoteGoodsSpus(queryRequest, paramVO);
//        result.setPromoteInfo(promoteInfo);
        return spus;
    }

    /**
     * 获取凑单活动详情
     * @param paramVO
     * @return
     */
    public EsSpuNewAggResp.PromoteInfo getPromoteInfo(PromoteGoodsParamVO paramVO){
        //查询营销活动
        MarketingGetByIdRequest mktParam = new MarketingGetByIdRequest();
        mktParam.setMarketingId(Long.valueOf(paramVO.getId()));
        BaseResponse<MarketingGetByIdForCustomerResponse> mktResp = marketingQueryProvider.getByIdForCustomer(mktParam);
        if (mktResp == null || mktResp.getContext() == null || mktResp.getContext().getMarketingForEndVO() == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR, "指定的营销活动不存在");
        }

        MarketingForEndVO mkt = mktResp.getContext().getMarketingForEndVO();
        //活动信息
        EsSpuNewAggResp.PromoteInfo promoteInfo = new EsSpuNewAggResp.PromoteInfo();
        promoteInfo.setStartTime(mkt.getBeginTime().format(formatter));
        promoteInfo.setEndTime(mkt.getEndTime().format(formatter));
        //促销文案
        String text = "限时促销：";
        List<EsSpuNewAggResp.PromoteInfo.MarkingDetail> markingDetails=new ArrayList<>();
        if (MarketingSubType.REDUCTION_FULL_AMOUNT.equals(mkt.getSubType())) {
            text += mkt.getFullReductionLevelList().stream().map(item-> "满" + item.getFullAmount() + "减" + item.getReduction()).collect(Collectors.joining(","));
            mkt.getFullReductionLevelList().forEach(item->{
                EsSpuNewAggResp.PromoteInfo.MarkingDetail detail=new EsSpuNewAggResp.PromoteInfo.MarkingDetail();
                detail.setFullAmount(item.getFullAmount());
                detail.setReduction(item.getReduction());
                detail.setReductionName("满" + item.getFullAmount() + "减" + item.getReduction());
                markingDetails.add(detail);
            });
        } else if (MarketingSubType.REDUCTION_FULL_COUNT.equals(mkt.getSubType())) {
            text += mkt.getFullReductionLevelList().stream().map(item-> "满" + item.getFullCount() + "件减" + item.getReduction()).collect(Collectors.joining(","));
            mkt.getFullReductionLevelList().forEach(item->{
                EsSpuNewAggResp.PromoteInfo.MarkingDetail detail=new EsSpuNewAggResp.PromoteInfo.MarkingDetail();
                detail.setFullAmount(BigDecimal.valueOf(item.getFullCount()));
                detail.setReduction(item.getReduction());
                detail.setReductionName("满" + item.getFullAmount() + "件减" + item.getReduction());
                markingDetails.add(detail);
            });
        } else if (MarketingSubType.DISCOUNT_FULL_AMOUNT.equals(mkt.getSubType())) {
            text += mkt.getFullReductionLevelList().stream().map(item-> "满" + item.getFullAmount() + "打" + item.getReduction() + "折").collect(Collectors.joining(","));
            mkt.getFullReductionLevelList().forEach(item->{
                EsSpuNewAggResp.PromoteInfo.MarkingDetail detail=new EsSpuNewAggResp.PromoteInfo.MarkingDetail();
                detail.setFullAmount(item.getFullAmount());
                detail.setReduction(item.getReduction());
                detail.setReductionName("满" + item.getFullAmount() + "打" + item.getReduction()+ "折");
                markingDetails.add(detail);
            });
        } else if (MarketingSubType.DISCOUNT_FULL_COUNT.equals(mkt.getSubType())) {
            text += mkt.getFullReductionLevelList().stream().map(item-> "满" + item.getFullAmount() + "件打" + item.getReduction() + "折").collect(Collectors.joining(","));
            mkt.getFullReductionLevelList().forEach(item->{
                EsSpuNewAggResp.PromoteInfo.MarkingDetail detail=new EsSpuNewAggResp.PromoteInfo.MarkingDetail();
                detail.setFullAmount(item.getFullAmount());
                detail.setReduction(item.getReduction());
                detail.setReductionName("满" + item.getFullAmount() + "件打" + item.getReduction()+ "折");
                markingDetails.add(detail);
            });
        } else {
            text += "其他";
        }
        promoteInfo.setTipText(text);
        promoteInfo.setName(mkt.getMarketingName());
        promoteInfo.setMarkingDetails(markingDetails);
        return promoteInfo;
    }


    /**
     * 获取凑单商品的spuid
     * @param esGoodsInfoQueryRequest
     * @param paramVO
     * @return
     */
    private List<String> getPromoteGoodsSpus(EsGoodsInfoQueryRequest esGoodsInfoQueryRequest, PromoteGoodsParamVO paramVO) {

        esGoodsInfoQueryRequest.setAuditStatus(CheckStatus.CHECKED.toValue());
        esGoodsInfoQueryRequest.setStoreState(StoreState.OPENING.toValue());
        esGoodsInfoQueryRequest.setAddedFlag(AddedFlag.YES.toValue());
        esGoodsInfoQueryRequest.setDelFlag(DeleteFlag.NO.toValue());
        esGoodsInfoQueryRequest.setVendibility(Constants.yes);
        esGoodsInfoQueryRequest.setCateAggFlag(true);
        esGoodsInfoQueryRequest.setSortFlag(paramVO.convertSortType());
        esGoodsInfoQueryRequest.setPageNum(paramVO.getPageNum() - 1);
        esGoodsInfoQueryRequest.setPageSize(paramVO.getPageSize());
        String now = DateUtil.format(LocalDateTime.now(), DateUtil.FMT_TIME_4);
        esGoodsInfoQueryRequest.setContractStartDate(now);
        esGoodsInfoQueryRequest.setContractEndDate(now);

        esGoodsInfoQueryRequest.setGoodsChannelTypeSet(Collections.singletonList(commonUtil.getTerminal().getCode()));
        esGoodsInfoQueryRequest.setLikeGoodsName(paramVO.getKeyword());

        EsGoodsInfoResponse esGoodsInfoResponse = esGoodsInfoElasticQueryProvider.page(esGoodsInfoQueryRequest).getContext();
        List<EsGoodsInfoVO> goodsInfoVOs = esGoodsInfoResponse.getEsGoodsInfoPage().getContent();
        List<String> spuIds = goodsInfoVOs.stream().map(EsGoodsInfoVO::getGoodsId).distinct().collect(Collectors.toList());

        PromoteGoodsResultVO result = new PromoteGoodsResultVO();
        if (org.springframework.util.CollectionUtils.isEmpty(spuIds)) {
            return null;
        }
        return spuIds;
    }

    private void handCart4FitGoods(List<SpuNewBookListResp> fitGoods, CustomerVO customer) {
        //统一查询购物车内容
        BaseResponse<PurchaseListResponse> cartResponse = purchaseQueryProvider.purchaseInfo(
                PurchaseInfoRequest.builder()
                        .customer(customer)
                        .channelType(commonUtil.getTerminal().getCode())
                        .inviteeId(commonUtil.getPurchaseInviteeId()).build());

        PurchaseListResponse cartInfo = cartResponse.getContext();
        if (cartInfo == null || CollectionUtils.isEmpty(cartInfo.getGoodsInfos())) {
            return;
        }
        //处理采购数量
        Map<String, GoodsInfoVO> skuId2sku = cartInfo.getGoodsInfos().stream().collect(Collectors.toMap(GoodsInfoVO::getGoodsInfoId, i -> i));
        for (SpuNewBookListResp fitGood : fitGoods) {
            GoodsInfoVO skuVO = skuId2sku.get(fitGood.getSkuId());
            fitGood.setBuyCount(skuVO == null ? 0 : skuVO.getBuyCount().intValue());
        }
    }

    /**
     * 图书关键词联想
     * @param request
     * @return
     */
    private List<Map<String,Object>> keySearch(KeyWordSpuQueryReq request) {
        request.setChannelTypes(Collections.singletonList(commonUtil.getTerminal().getCode()));
        //获取客户信息
        CustomerGetByIdResponse customer = null;
        String userId = commonUtil.getOperatorId();
        if (!StringUtils.isEmpty(userId)) {
            customer = customerQueryProvider.getCustomerById(new CustomerGetByIdRequest(userId)).getContext();
            String isCounselor = customerProvider.isCounselorCache(Integer.valueOf(customer.getFanDengUserNo())).getContext();
            //非知识顾问用户
            if (!Objects.isNull(isCounselor) && "true".equals(isCounselor)) {
                request.setCpsSpecial(1);// 表示知识顾问，显示所有商品
            }
        }
        EsSpuNewAggResp<List<EsSpuNewResp>> esSpuNewAggResp = esSpuNewProvider.listKeyWorldEsSpu(request).getContext();
        List<SpuNewBookListResp> spuNewBookListResps = spuNewSearchService.listSpuNewSearch(esSpuNewAggResp.getResult().getContent(), customer);
        List<Map<String,Object>> tempList= KsBeanUtil.objectsToMaps(spuNewBookListResps);
        List<Map<String,Object>> resList=new ArrayList<>();

        for(Object obj:tempList){
            Map<String,Object> map= (Map<String, Object>) obj;
            if(resList.size()>10){
                break;
            }
            Map<String,Object> map1=new HashMap<>();
            map1.put("spuId",map.get("spuId"));
            map1.put("skuId",map.get("skuId"));
            map1.put("spuName",map.get("spuName"));
            String type;
            if(map.get("spuCategory").toString().equals(SearchSpuNewCategoryEnum.BOOK.getCode().toString())){
                map1.put("spuCategory","图书");
            }else {
                map1.put("spuCategory","商品");
            }
            resList.add(map1);
        }
        return resList;
    }

    /**
     * 搜索 获取商品/图书
     * @menu 搜索功能
     * @param request
     * @return
     */
    @PostMapping("/keyword/keywordSpuSearch")
    public BaseResponse<EsSpuNewAggResp<List<SpuNewBookListResp>>> keywordSpuSearch(@Validated @RequestBody KeyWordSpuQueryReq request) {
        return BaseResponse.success(this.spuSearch(request));
    }


    /**
     * 搜索 获取商品/图书V2集成凑单
     * @menu 搜索功能
     * @param request
     * @return
     */
    @PostMapping("/keyword/keywordSpuSearchV2")
    public BaseResponse<EsSpuNewAggResp<List<SpuNewBookListResp>>> keywordSpuSearchV2(@Validated @RequestBody KeyWordSpuQueryReq request) {
        return BaseResponse.success(this.spuSearchV2(request));
    }


    /**
     * 搜索 获取前10
     * @menu 搜索功能
     * @param request
     * @return
     */
    @PostMapping("/keyword/v2/keywordSearch")
    public BaseResponse<List<Map<String,Object>>> keywordSearch(@Validated @RequestBody KeyWordSpuQueryReq request) {
        List<Map<String,Object>> list = this.keySearch(request);
        return BaseResponse.success(list);
    }

    /**
     * 凑单搜索 获取商品/图书
     * @menu 搜索功能
     * @param request
     * @return
     */
    @PostMapping("/keyword/supplement/keywordSpuSearch")
    public BaseResponse<EsSpuNewAggResp<List<SpuNewBookListResp>>> supplementKeywordSpuSearch(@Validated @RequestBody KeyWordSpuQueryReq request) {
        EsSpuNewAggResp<List<SpuNewBookListResp>> listEsSpuNewAggResp = this.spuSearch(request);
        //凑单页面 显示价格区间
        for (EsSpuNewAggResp.AggsCategoryResp aggsCategory : listEsSpuNewAggResp.getAggsCategorys()) {
            if (!Objects.equals(aggsCategory.getCategory(), SearchSpuNewAggsCategoryEnum.AGGS_PRICE_RANGE.getCode())) {
                continue;
            }
            for (EsSpuNewAggResp.AggsResp aggsResp : aggsCategory.getAggsList()) {
                aggsResp.setHasShow(true);
            }
        }
        return BaseResponse.success(listEsSpuNewAggResp);

    }
}
