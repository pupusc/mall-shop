package com.wanmi.sbc.booklistmodel;

import com.soybean.common.util.StockUtil;
import com.soybean.elastic.api.enums.SearchSpuNewLabelCategoryEnum;
import com.soybean.marketing.api.provider.activity.NormalActivityPointSkuProvider;
import com.soybean.marketing.api.req.SpuNormalActivityReq;
import com.soybean.marketing.api.resp.SkuNormalActivityResp;
import com.wanmi.sbc.booklistmodel.response.BookListModelAndGoodsListResponse;
import com.wanmi.sbc.booklistmodel.response.GoodsCustomResponse;
import com.wanmi.sbc.booklistmodel.response.GoodsExtPropertiesCustomResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.constant.RedisKeyConstant;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.TerminalSource;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.customer.CustomerProvider;
import com.wanmi.sbc.customer.bean.dto.CounselorDto;
import com.wanmi.sbc.customer.bean.dto.CustomerDTO;
import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsCustomQueryProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsCustomQueryProviderRequest;
import com.wanmi.sbc.elastic.bean.vo.goods.EsGoodsVO;
import com.wanmi.sbc.elastic.bean.vo.goods.GoodsInfoNestVO;
import com.wanmi.sbc.elastic.bean.vo.goods.GoodsLabelNestVO;
import com.wanmi.sbc.goods.api.enums.AnchorPushEnum;
import com.wanmi.sbc.goods.api.enums.BusinessTypeEnum;
import com.wanmi.sbc.goods.api.enums.CategoryEnum;
import com.wanmi.sbc.goods.api.enums.DeleteFlagEnum;
import com.wanmi.sbc.goods.api.enums.FilterRuleEnum;
import com.wanmi.sbc.goods.api.enums.StateEnum;
import com.wanmi.sbc.goods.api.provider.booklistmodel.BookListModelProvider;
import com.wanmi.sbc.goods.api.provider.chooserule.ChooseRuleProvider;
import com.wanmi.sbc.goods.api.provider.goods.GoodsQueryProvider;
import com.wanmi.sbc.goods.api.provider.nacos.GoodsNacosConfigProvider;
import com.wanmi.sbc.goods.api.request.booklistgoodspublish.BookListGoodsPublishProviderRequest;
import com.wanmi.sbc.goods.api.request.booklistmodel.BookListModelBySpuIdCollQueryRequest;
import com.wanmi.sbc.goods.api.request.booklistmodel.BookListModelProviderRequest;
import com.wanmi.sbc.goods.api.request.chooserule.ChooseRuleProviderRequest;
import com.wanmi.sbc.goods.api.request.goods.GoodsByConditionRequest;
import com.wanmi.sbc.goods.api.response.booklistgoodspublish.BookListGoodsPublishProviderResponse;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListMixProviderResponse;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListModelGoodsIdProviderResponse;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListModelProviderResponse;
import com.wanmi.sbc.goods.api.response.chooserulegoodslist.BookListGoodsProviderResponse;
import com.wanmi.sbc.goods.api.response.chooserulegoodslist.ChooseRuleProviderResponse;
import com.wanmi.sbc.goods.api.response.goods.GoodsByConditionResponse;
import com.wanmi.sbc.goods.api.response.nacos.GoodsNacosConfigResp;
import com.wanmi.sbc.goods.bean.dto.GoodsInfoDTO;
import com.wanmi.sbc.goods.bean.enums.AddedFlag;
import com.wanmi.sbc.goods.bean.enums.EnterpriseAuditState;
import com.wanmi.sbc.goods.bean.enums.PublishState;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GoodsVO;
import com.wanmi.sbc.marketing.api.provider.plugin.MarketingPluginProvider;
import com.wanmi.sbc.marketing.api.request.plugin.MarketingPluginGoodsListFilterRequest;
import com.wanmi.sbc.marketing.api.response.info.GoodsInfoListByGoodsInfoResponse;
import com.wanmi.sbc.redis.RedisService;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/14 1:48 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
@Slf4j
public class BookListModelAndGoodsService {

    @Autowired
    private BookListModelProvider bookListModelProvider;

    @Autowired
    private EsGoodsCustomQueryProvider esGoodsCustomQueryProvider;


    @Autowired
    private GoodsQueryProvider goodsQueryProvider;

    @Autowired
    private MarketingPluginProvider marketingPluginProvider;

    @Autowired
    private NormalActivityPointSkuProvider normalActivityPointSkuProvider;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private CustomerProvider customerProvider;

    @Autowired
    private ChooseRuleProvider chooseRuleProvider;

    @Autowired
    private RedisService redisService;

//    @Value("${stock.size:5}")
//    private Long stockSize;

    @Autowired
    private GoodsNacosConfigProvider goodsNacosConfigProvider;

    /**
     * 获取是否是知识顾问
     * @return
     */
    public boolean getIsCounselor() {

        CustomerVO customerVO = this.getCustomerVo();
        if (customerVO == null) {
            return false;
        }
        if (StringUtils.isEmpty(customerVO.getFanDengUserNo())) {
            return false;
        } else {
            CounselorDto counselorDto = customerProvider.isCounselor(Integer.valueOf(customerVO.getFanDengUserNo())).getContext();
            if (counselorDto == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取客户信息
     * @return
     */
    public CustomerVO getCustomerVo() {
        String operatorId = commonUtil.getOperatorId();
        if (null==operatorId||StringUtils.isEmpty(operatorId)||"null".equals(operatorId)) {
            return null;
        }
        return commonUtil.getCanNullCustomer();
    }


    /**
     * 批量获取书单详细信息
     * @param bookListModelIdSet
     * @return
     */
    public List<BookListMixProviderResponse> listBookListMixProvider(Collection<Integer> bookListModelIdSet) {
        BaseResponse<List<BookListMixProviderResponse>> listBookListMixResponse = bookListModelProvider.listPublishGoodsByModelIds(bookListModelIdSet);
        if (CollectionUtils.isEmpty(listBookListMixResponse.getContext())) {
            return new ArrayList<>();
        }
        return listBookListMixResponse.getContext();
    }


    /**
     * 根据 书单模版id 获取 商品spuId 书单Map信息
     * @return
     */
//    public Map<String, BookListMixProviderResponse> supId2BookListMixMap(List<BookListMixProviderResponse> bookListMixList) {
//        //根据书单id列表 获取商品列表id信息
//        if (CollectionUtils.isEmpty(bookListMixList)) {
//            return new HashMap<>();
//        }
//
//        //goodsId -> BookListModelProviderResponse
//        Map<String, BookListMixProviderResponse> supId2BookListMixMap = new HashMap<>();
//
//        //获取商品id列表
//        for (BookListMixProviderResponse bookListMixProviderParam : bookListMixList) {
//            if (bookListMixProviderParam.getChooseRuleMode() == null) {
//                continue;
//            }
//            List<BookListGoodsProviderResponse> bookListGoodsList = bookListMixProviderParam.getChooseRuleMode().getBookListGoodsList();
//            if (CollectionUtils.isEmpty(bookListGoodsList)) {
//                continue;
//            }
//            for (BookListGoodsProviderResponse bookListGoodsProviderParam: bookListGoodsList) {
//                //这里方便后续 房源查找模版
//                supId2BookListMixMap.put(bookListGoodsProviderParam.getSpuId(), bookListMixProviderParam);
//            }
//        }
//        return supId2BookListMixMap;
//    }


    /**
     * 打包goodsInfo详细信息
     * @param esGoodsVOList
     * @param customer
     * @return
     */
    public List<GoodsInfoVO> packageGoodsInfoList(List<EsGoodsVO> esGoodsVOList, CustomerVO customer) {
        return this.packageGoodsInfoList(esGoodsVOList, customer, Collections.singletonList(commonUtil.getTerminal().getCode()));
    }

    public List<GoodsInfoVO> packageGoodsInfoListV2(List<EsGoodsVO> esGoodsVOList, CustomerVO customer) {
        return this.packageGoodsInfoList(esGoodsVOList, customer, Collections.singletonList(TerminalSource.H5.getCode()));
    }


    /**
     * 兼容版本
     * @param esGoodsVOList
     * @param customer
     * @param channelTypeSet
     * @return
     */
    public List<GoodsInfoVO> packageGoodsInfoList(List<EsGoodsVO> esGoodsVOList, CustomerVO customer, List<Integer> channelTypeSet) {
        List<GoodsInfoVO> result = new ArrayList<>();
        List<GoodsInfoVO> goodsInfoList = esGoodsVOList.stream().map(EsGoodsVO::getGoodsInfos)
                .flatMap(Collection::stream).map(goods -> {
                    GoodsInfoVO goodsInfoVO = KsBeanUtil.convert(goods, GoodsInfoVO.class);
                    goodsInfoVO.setVendibility(goods.getVendibilityStatus());
                    Integer enterPriseAuditStatus = goods.getEnterPriseAuditStatus();
                    if (Objects.nonNull(enterPriseAuditStatus)) {
                        goodsInfoVO.setEnterPriseAuditState(EnterpriseAuditState.CHECKED.toValue() == enterPriseAuditStatus ?
                                EnterpriseAuditState.CHECKED : null);
                    }
                    return goodsInfoVO;
                }).collect(Collectors.toList());

        MarketingPluginGoodsListFilterRequest filterRequest = new MarketingPluginGoodsListFilterRequest();
        filterRequest.setGoodsInfos(KsBeanUtil.convert(goodsInfoList, GoodsInfoDTO.class));
        if (Objects.nonNull(customer)) {
            filterRequest.setCustomerDTO(KsBeanUtil.convert(customer, CustomerDTO.class));
        }
//        filterRequest.setMoFangFlag(queryRequest.getMoFangFlag());
        GoodsInfoListByGoodsInfoResponse filterResponse = marketingPluginProvider.goodsListFilter(filterRequest).getContext();
        if (Objects.nonNull(filterResponse) && org.apache.commons.collections4.CollectionUtils.isNotEmpty(filterResponse.getGoodsInfoVOList())) {
            result = filterResponse.getGoodsInfoVOList();
        }

        //设置活动信息
        if (!CollectionUtils.isEmpty(result)) {

            //活动信息
            Map<String, SkuNormalActivityResp> skuId2NormalActivityMap = new HashMap<>();
            SpuNormalActivityReq spuNormalActivityReq = new SpuNormalActivityReq();
            spuNormalActivityReq.setSpuIds(result.stream().map(GoodsInfoVO::getGoodsId).collect(Collectors.toList()));
            spuNormalActivityReq.setStatus(StateEnum.RUNNING.getCode());
            spuNormalActivityReq.setPublishState(PublishState.ENABLE.toValue());
            spuNormalActivityReq.setChannelTypes(channelTypeSet);
            spuNormalActivityReq.setCustomerId(customer == null ? "" : customer.getCustomerId());
            List<SkuNormalActivityResp> skuNormalActivityResps = normalActivityPointSkuProvider.listSpuRunningNormalActivity(spuNormalActivityReq).getContext();
            for (SkuNormalActivityResp skuNormalActivityRespParam : skuNormalActivityResps) {
                skuId2NormalActivityMap.put(skuNormalActivityRespParam.getSkuId(), skuNormalActivityRespParam);
            }
            for (GoodsInfoVO goodsInfo : result) {
                SkuNormalActivityResp skuNormalActivityResp = skuId2NormalActivityMap.get(goodsInfo.getGoodsInfoId());
                if (skuNormalActivityResp == null) {
                    continue;
                }
                GoodsInfoVO.NormalActivity activity = new GoodsInfoVO.NormalActivity();
                activity.setNum(skuNormalActivityResp.getNum());
                activity.setActivityShow(String.format("返%d积分", skuNormalActivityResp.getNum()));
                goodsInfo.setActivity(activity);
            }

        }
        return result;
    }

    /**
     * 根据 商品id 书单map 获取商品列表详细信息
     * @param unSpuIdCollection
     * @return
     */
    public MicroServicePage<BookListModelAndGoodsListResponse> listGoodsBySpuIdAndBookListModel(
            Collection<Integer> bookListModelIdCollection, Collection<String> unSpuIdCollection, boolean isCpsSpecial, int pageNum, int pageSize) {
        List<BookListMixProviderResponse> bookListMixList = this.listBookListMixProvider(bookListModelIdCollection);

        MicroServicePage<BookListModelAndGoodsListResponse> microServicePageResult = new MicroServicePage<>();
        microServicePageResult.setTotal(0);
        microServicePageResult.setContent(new ArrayList<>());

        if (CollectionUtils.isEmpty(bookListMixList)) {
            return microServicePageResult;
        }
        Set<String> spuIdSet = new HashSet<>();
        //获取所有商品信息
        for (BookListMixProviderResponse bookListMixParam : bookListMixList) {
            if (bookListMixParam.getBookListModel() == null) {
                continue;
            }

            if (bookListMixParam.getChooseRuleMode() == null) {
                continue;
            }

            if (CollectionUtils.isEmpty(bookListMixParam.getChooseRuleMode().getBookListGoodsList())) {
                continue;
            }
            Set<String> spuIdSetTmp =
                    bookListMixParam.getChooseRuleMode().getBookListGoodsList().stream().map(BookListGoodsProviderResponse::getSpuId).collect(Collectors.toSet());
            spuIdSet.addAll(spuIdSetTmp);
        }

        int maxSize = 100;
        List<BookListModelAndGoodsListResponse> result = new ArrayList<>();
        //根据商品id列表 获取商品列表信息
        EsGoodsCustomQueryProviderRequest esGoodsCustomRequest = new EsGoodsCustomQueryProviderRequest();
        esGoodsCustomRequest.setPageNum(pageNum);
        esGoodsCustomRequest.setPageSize(Math.min(spuIdSet.size(), maxSize)); //这里主要是为啦防止书单里面的数量过分的多的情况，限制最多100个
        esGoodsCustomRequest.setGoodIdList(spuIdSet);
        esGoodsCustomRequest.setGoodsChannelTypeSet(Collections.singletonList(commonUtil.getTerminal().getCode()));
        if (!CollectionUtils.isEmpty(unSpuIdCollection)) {
            esGoodsCustomRequest.setUnGoodIdList(unSpuIdCollection);
        }
        //如果非知识顾问，则需要过滤，是知识顾问就不用过滤
        if (!isCpsSpecial) {
            esGoodsCustomRequest.setCpsSpecial(0); //知识顾问要单独处理
        }

        // goodsId -> BookListModelAndGoodsListResponse
        BaseResponse<MicroServicePage<EsGoodsVO>> esGoodsVOMicroServiceResponse = esGoodsCustomQueryProvider.listEsGoodsNormal(esGoodsCustomRequest);
        MicroServicePage<EsGoodsVO> esGoodsVOMicroServicePage = esGoodsVOMicroServiceResponse.getContext();
        List<EsGoodsVO> content = esGoodsVOMicroServicePage.getContent();
        if (!CollectionUtils.isEmpty(content)) {

            List<GoodsVO> goodsVOList = this.changeEsGoods2GoodsVo(content);
            if (CollectionUtils.isEmpty(goodsVOList)) {
                return microServicePageResult;
            }

            List<GoodsInfoVO> goodsInfoVOList = this.packageGoodsInfoList(content, this.getCustomerVo());
            if (CollectionUtils.isEmpty(goodsInfoVOList)) {
                return microServicePageResult;
            }
            // EsGoods -> Map
            Map<String, GoodsVO> spuId2GoodsVoMap = goodsVOList.stream().collect(Collectors.toMap(GoodsVO::getGoodsId, Function.identity(), (k1, k2) -> k1));
            Map<String, EsGoodsVO> spuId2EsGoodsVoMap = content.stream().collect(Collectors.toMap(EsGoodsVO::getId, Function.identity(), (k1, k2) -> k1));

            for (BookListMixProviderResponse bookListMixParam : bookListMixList) {
                if (bookListMixParam.getBookListModel() == null) {
                    continue;
                }

                if (bookListMixParam.getChooseRuleMode() == null) {
                    continue;
                }

                if (CollectionUtils.isEmpty(bookListMixParam.getChooseRuleMode().getBookListGoodsList())) {
                    continue;
                }

                BookListModelAndGoodsListResponse resultTmp = new BookListModelAndGoodsListResponse();
                resultTmp.setBookListModel(bookListMixParam.getBookListModel());
                List<GoodsCustomResponse> goodsCustomTmpList = new ArrayList<>();
                for (BookListGoodsProviderResponse bookListGoodsTmpParam : bookListMixParam.getChooseRuleMode().getBookListGoodsList()) {
                    GoodsVO goodsVO = spuId2GoodsVoMap.get(bookListGoodsTmpParam.getSpuId());
                    EsGoodsVO esGoodsVO = spuId2EsGoodsVoMap.get(bookListGoodsTmpParam.getSpuId());
                    if (goodsVO == null || esGoodsVO == null) {
                        continue;
                    }
                    if (goodsCustomTmpList.size() >= pageSize) {
                        break;
                    }
                    goodsCustomTmpList.add(this.packageGoodsCustomResponse(goodsVO, esGoodsVO, goodsInfoVOList));
                }
                resultTmp.setGoodsList(goodsCustomTmpList);

                if (!CollectionUtils.isEmpty(goodsCustomTmpList)) {
                    result.add(resultTmp);
                }
            }
        }

        microServicePageResult.setTotal(esGoodsVOMicroServicePage.getTotal() > maxSize ? maxSize : esGoodsVOMicroServicePage.getTotal());
        microServicePageResult.setContent(result);
        return microServicePageResult;
    }

    /**
     * 转化EsGoodsVo to goodsVo
     * @param content
     * @return
     */
    public List<GoodsVO> changeEsGoods2GoodsVo(List<EsGoodsVO> content){
        if (CollectionUtils.isEmpty(content)){
            return new ArrayList<>();
        }

        List<String> goodsIdList = content.stream().map(EsGoodsVO::getId).collect(Collectors.toList());
        GoodsByConditionRequest goodsByConditionRequest = new GoodsByConditionRequest();
        goodsByConditionRequest.setGoodsIds(goodsIdList);
        goodsByConditionRequest.setAddedFlag(AddedFlag.YES.toValue());
        goodsByConditionRequest.setDelFlag(DeleteFlagEnum.NORMAL.getCode());
        GoodsByConditionResponse goodsByConditionResponseBaseResponse = goodsQueryProvider.listByCondition(goodsByConditionRequest).getContext();
        List<GoodsVO> goodsVOList = goodsByConditionResponseBaseResponse.getGoodsVOList();
        if (CollectionUtils.isEmpty(goodsVOList)) {
            return new ArrayList<>();
        }
        return goodsVOList;
    }


    /**
     *  esGoodsVo 转化成 可以返回给前端的对象
     * @param
     * @return
     */
    public GoodsCustomResponse packageGoodsCustomResponse(GoodsVO goodsVO, EsGoodsVO esGoodsVO, List<GoodsInfoVO> goodsInfoVOList) {
        String goodsInfoId = "";
        String goodsInfoNo = "";
        String goodsInfoImg = "";
        List<String> couponLabelNameList = new ArrayList<>();

        Map<String, GoodsInfoVO> spuId2NormalActivityGoodsInfoMap = new HashMap<>();

        GoodsCustomResponse esGoodsCustomResponse = new GoodsCustomResponse();

        BigDecimal currentSalePrice = new BigDecimal("100000");
        BigDecimal currentMarketingPrice = new BigDecimal("100000");
        BigDecimal lineSalePrice = null;
        long stock = 0l;

        if (esGoodsVO != null) {
            if (!CollectionUtils.isEmpty(goodsInfoVOList)) {
                Map<String, List<GoodsInfoVO>> goodsId2GoodsInfoListMap = new HashMap<>();
                for (GoodsInfoVO goodsInfoParam : goodsInfoVOList) {
                    List<GoodsInfoVO> goodsInfoVOListParam = goodsId2GoodsInfoListMap.get(goodsInfoParam.getGoodsId());
                    if (CollectionUtils.isEmpty(goodsInfoVOListParam)) {
                        List<GoodsInfoVO> tmp = new ArrayList<>();
                        tmp.add(goodsInfoParam);
                        goodsId2GoodsInfoListMap.put(goodsInfoParam.getGoodsId(), tmp);
                    } else {
                        goodsInfoVOListParam.add(goodsInfoParam);
                    }
                }

                List<GoodsInfoVO> goodsInfoVoListLast = goodsId2GoodsInfoListMap.get(esGoodsVO.getId());
                if (CollectionUtils.isEmpty(goodsInfoVoListLast)) {
                    goodsInfoVoListLast = new ArrayList<>();
                }


                //填充价格
                for (GoodsInfoVO goodsInfoParam : goodsInfoVoListLast) {
                    BigDecimal tmpSalePrice = null;
                    BigDecimal tmpMarketingPrice = null;
                    Long tempStock = 0L;

                    //获取库存
                    String stockRedis = redisService.getString(RedisKeyConstant.GOODS_INFO_STOCK_PREFIX + goodsInfoParam.getGoodsInfoId());
                    tempStock = StringUtils.isEmpty(stockRedis) ? 0L : Long.parseLong(stockRedis);
                    //商品<5个不销售
                    if(tempStock.compareTo(StockUtil.THRESHOLD_STOCK) <=0){
                        tempStock = 0L;
                    }

                    if (tempStock > stock) {
                        stock = tempStock;
                    }

                    //会员价
                    if (goodsInfoParam.getSalePrice() != null && currentSalePrice.compareTo(goodsInfoParam.getSalePrice()) > 0) {
                        tmpSalePrice = goodsInfoParam.getSalePrice();
                    }

                    if (goodsInfoParam.getMarketPrice() != null && currentMarketingPrice.compareTo(goodsInfoParam.getMarketPrice()) > 0) {
                        tmpMarketingPrice = goodsInfoParam.getMarketPrice();
                    }


                    if (tmpSalePrice != null || tmpMarketingPrice != null) {
                        currentSalePrice = tmpSalePrice;
                        currentMarketingPrice = tmpMarketingPrice;
                        goodsInfoId = goodsInfoParam.getGoodsInfoId();
                        goodsInfoNo = goodsInfoParam.getGoodsInfoNo();
                        goodsInfoImg = goodsInfoParam.getGoodsInfoImg();
                        int maxLabelSize = 2; //只是显示2个标签

                        //折扣
                        if (!CollectionUtils.isEmpty(goodsInfoParam.getMarketingLabels())) {
                            for (int i = 0; i < maxLabelSize && i < goodsInfoParam.getMarketingLabels().size(); i++) {
                                couponLabelNameList.add(goodsInfoParam.getMarketingLabels().get(i).getMarketingDesc());
                            }
                        }

                        //满减
                        if (!CollectionUtils.isEmpty(goodsInfoParam.getCouponLabels())) {
                            for (int i = 0; i < (maxLabelSize - couponLabelNameList.size()) && i < goodsInfoParam.getCouponLabels().size(); i++) {
                                couponLabelNameList.add(goodsInfoParam.getCouponLabels().get(i).getCouponDesc());
                            }
                        }
                    }

                    if (goodsInfoParam.getActivity() != null) {
                        GoodsInfoVO spu2NormalActivityGoodsInfoResp = spuId2NormalActivityGoodsInfoMap.get(goodsInfoParam.getGoodsId());
                        if (spu2NormalActivityGoodsInfoResp == null) {
                            spuId2NormalActivityGoodsInfoMap.put(goodsInfoParam.getGoodsId(), goodsInfoParam);
                        } else {
                            if (spu2NormalActivityGoodsInfoResp.getActivity().getNum() < goodsInfoParam.getActivity().getNum()) {
                                spuId2NormalActivityGoodsInfoMap.put(goodsInfoParam.getGoodsId(), goodsInfoParam);
                            }
                        }
                    }
                }
            }

            List<GoodsLabelNestVO> goodsLabelList = esGoodsVO.getGoodsLabelList();
            if (!CollectionUtils.isEmpty(goodsLabelList)) {
                esGoodsCustomResponse.setGoodsLabelList(goodsLabelList.stream().filter(gl ->
                                Objects.equals(gl.getDelFlag(), DeleteFlag.NO) && Objects.equals(gl.getLabelVisible(), true))
                        .map(GoodsLabelNestVO::getLabelName).collect(Collectors.toList()));
            } else {
                esGoodsCustomResponse.setGoodsLabelList(new ArrayList<>());
            }
            if (esGoodsVO.getGoodsExtProps() != null) {
                esGoodsCustomResponse.setGoodsScore(esGoodsVO.getGoodsExtProps().getScore() == null ? 100 + "" : esGoodsVO.getGoodsExtProps().getScore()+"");

                if (!StringUtils.isEmpty(esGoodsVO.getGoodsExtProps().getAuthor())
                        || !StringUtils.isEmpty(esGoodsVO.getGoodsExtProps().getPublisher())
                        || esGoodsVO.getGoodsExtProps().getPrice() != null) {
                    GoodsExtPropertiesCustomResponse extProperties = new GoodsExtPropertiesCustomResponse();
                    extProperties.setAuthor(StringUtils.isEmpty(esGoodsVO.getGoodsExtProps().getAuthor()) ? "" : esGoodsVO.getGoodsExtProps().getAuthor());
                    extProperties.setPublisher(StringUtils.isEmpty(esGoodsVO.getGoodsExtProps().getPublisher()) ? "" : esGoodsVO.getGoodsExtProps().getPublisher());
                    extProperties.setPrice(esGoodsVO.getGoodsExtProps().getPrice() == null ? new BigDecimal("1000") : esGoodsVO.getGoodsExtProps().getPrice());
                    esGoodsCustomResponse.setGoodsExtProperties(extProperties);
                    lineSalePrice = extProperties.getPrice();
                }
            }
            //氛围
            if(!CollectionUtils.isEmpty(esGoodsVO.getGoodsInfos())){
                GoodsInfoNestVO goodsInfoVO = esGoodsVO.getGoodsInfos().get(0);
                if(goodsInfoVO.getStartTime()!=null && goodsInfoVO.getEndTime() !=null &&  goodsInfoVO.getStartTime().compareTo(LocalDateTime.now()) < 0 && goodsInfoVO.getEndTime().compareTo(LocalDateTime.now()) >0){
                    esGoodsCustomResponse.setImageUrl(goodsInfoVO.getImageUrl());
                    esGoodsCustomResponse.setAtmosType(goodsInfoVO.getAtmosType());
                    esGoodsCustomResponse.setElementFour(goodsInfoVO.getElementFour());
                    esGoodsCustomResponse.setElementThree(goodsInfoVO.getElementThree());
                    esGoodsCustomResponse.setElementTwo(goodsInfoVO.getElementTwo());
                    esGoodsCustomResponse.setElementOne(goodsInfoVO.getElementOne());
                }else{
                    esGoodsCustomResponse.setImageUrl(null);
                    esGoodsCustomResponse.setAtmosType(null);
                    esGoodsCustomResponse.setElementFour(null);
                    esGoodsCustomResponse.setElementThree(null);
                    esGoodsCustomResponse.setElementTwo(null);
                    esGoodsCustomResponse.setElementOne(null);
                }
            }
        }



        //商品展示价格

        esGoodsCustomResponse.setGoodsId(goodsVO.getGoodsId());
        esGoodsCustomResponse.setGoodsNo(goodsVO.getGoodsNo());
        //获取最小价格的 goodsInfo
        esGoodsCustomResponse.setGoodsInfoId(goodsInfoId);
        esGoodsCustomResponse.setGoodsInfoNo(goodsInfoNo);
        esGoodsCustomResponse.setGoodsName(goodsVO.getGoodsName());
        esGoodsCustomResponse.setGoodsSubName(goodsVO.getGoodsSubtitle());
        esGoodsCustomResponse.setGoodsCoverImg(goodsInfoImg);
        esGoodsCustomResponse.setGoodsUnBackImg(goodsVO.getGoodsUnBackImg());
        esGoodsCustomResponse.setShowPrice(currentSalePrice == null ? currentMarketingPrice : currentSalePrice);
        esGoodsCustomResponse.setLinePrice(lineSalePrice == null ? currentMarketingPrice : lineSalePrice);
        esGoodsCustomResponse.setCpsSpecial(goodsVO.getCpsSpecial());
        esGoodsCustomResponse.setCouponLabelList(CollectionUtils.isEmpty(couponLabelNameList) ? new ArrayList<>() : couponLabelNameList);
        esGoodsCustomResponse.setStock(stock);

        //获取 49免运费模版信息
        GoodsNacosConfigResp context = goodsNacosConfigProvider.getNacosConfig().getContext();
        log.info("BookListModelAndGoodsService packageGoodsCustomResponse spuId {} freightTmpId:{}",
                goodsVO.getGoodsId(), goodsVO.getFreightTempId());
        if (context != null && goodsVO.getFreightTempId() != null &&
                Objects.equals(context.getFreeDelivery49(), goodsVO.getFreightTempId().toString())) {
            SearchSpuNewLabelCategoryEnum freeDelivery49Enum = SearchSpuNewLabelCategoryEnum.FREE_DELIVERY_49;
            GoodsCustomResponse.Label freightLabel = new GoodsCustomResponse.Label();
            freightLabel.setLabelName(freeDelivery49Enum.getMessage());
            freightLabel.setLabelCategory(freeDelivery49Enum.getCode());

            esGoodsCustomResponse.setLabels(Collections.singletonList(freightLabel));
        }
        //主播推荐标签
        List<AnchorPushEnum> anchorPushEnumList = new ArrayList<>();
        if (!StringUtils.isEmpty(goodsVO.getAnchorPushs())) {
            for (String anchorPushParam : goodsVO.getAnchorPushs().split(" ")) {
                AnchorPushEnum byCode = AnchorPushEnum.getByCode(anchorPushParam);
                if (byCode == null) {
                    continue;
                }
                anchorPushEnumList.add(byCode);
            }
        }
        //活动
        GoodsInfoVO normalActivityGoodsInfo = spuId2NormalActivityGoodsInfoMap.get(goodsVO.getGoodsId());
        if (normalActivityGoodsInfo != null && normalActivityGoodsInfo.getActivity() != null) {
            GoodsCustomResponse.NormalActivity goodsCustomerNormal = new GoodsCustomResponse.NormalActivity();
            goodsCustomerNormal.setNum(normalActivityGoodsInfo.getActivity().getNum());
            goodsCustomerNormal.setActivityShow(normalActivityGoodsInfo.getActivity().getActivityShow());
            esGoodsCustomResponse.setActivities(Arrays.asList(goodsCustomerNormal));
//            esGoodsCustomResponse.setGoodsInfoId(normalActivityGoodsInfo.getGoodsInfoId());
        }
        //排序获取
        if (!CollectionUtils.isEmpty(anchorPushEnumList)) {
            anchorPushEnumList.sort(new Comparator<AnchorPushEnum>() {
                @Override
                public int compare(AnchorPushEnum o1, AnchorPushEnum o2) {
                    return o1.getOrder() - o2.getOrder();
                }
            });
            esGoodsCustomResponse.setAnchorPush(anchorPushEnumList.get(0).getMessage());
        }
        return esGoodsCustomResponse;
    }

    /**
     * 获取 esGoodsVo
     * @param esGoodsCustomRequest
     * @return
     */
    public List<EsGoodsVO> listRandomEsGoodsVo(EsGoodsCustomQueryProviderRequest esGoodsCustomRequest, int randomPageSize) {
        List<EsGoodsVO> resultEsGoodsList = new ArrayList<>();
        BaseResponse<MicroServicePage<EsGoodsVO>> esGoodsVOMicroServiceResponse = esGoodsCustomQueryProvider.listEsGoodsNormal(esGoodsCustomRequest);
        MicroServicePage<EsGoodsVO> esGoodsVOMicroServicePage = esGoodsVOMicroServiceResponse.getContext();
        List<EsGoodsVO> content = esGoodsVOMicroServicePage.getContent();
        if (CollectionUtils.isEmpty(content)) {
            return resultEsGoodsList;
        }
        //获取随机书籍
        Collection<Integer> randomIndex = RandomUtil.getRandom(content.size(), randomPageSize);

        for (Integer index : randomIndex) {
            resultEsGoodsList.add(content.get(index));
        }
        return resultEsGoodsList;
    }


    /**
     * 获取 esGoodsVo
     * @param esGoodsCustomRequest
     * @return
     */
    public MicroServicePage<EsGoodsVO> listEsGoodsVo(EsGoodsCustomQueryProviderRequest esGoodsCustomRequest) {
        BaseResponse<MicroServicePage<EsGoodsVO>> esGoodsVOMicroServiceResponse = esGoodsCustomQueryProvider.listEsGoodsNormal(esGoodsCustomRequest);
        return esGoodsVOMicroServiceResponse.getContext();
    }


    /**
     * 获取根据EsGoodsVo 转化成 GoodsCustomResponse
     * @return
     */
    public List<GoodsCustomResponse> listGoodsCustom(List<EsGoodsVO> resultEsGoodsList) {
        List<GoodsCustomResponse> result = new ArrayList<>();
        if (CollectionUtils.isEmpty(resultEsGoodsList)) {
            return result;
        }
        List<GoodsVO> goodsVOList = this.changeEsGoods2GoodsVo(resultEsGoodsList);
        if (CollectionUtils.isEmpty(goodsVOList)) {
            return result;
        }
        List<GoodsInfoVO> goodsInfoVOList = this.packageGoodsInfoList(resultEsGoodsList, this.getCustomerVo()); //添加客户信息
        if (CollectionUtils.isEmpty(goodsInfoVOList)) {
            return result;
        }
        Map<String, GoodsVO> supId2GoodsVoMap = goodsVOList.stream().collect(Collectors.toMap(GoodsVO::getGoodsId, Function.identity(), (k1, k2) -> k1));
        for (EsGoodsVO esGoodsVO : resultEsGoodsList) {
            GoodsVO goodsVO = supId2GoodsVoMap.get(esGoodsVO.getId());
            if (goodsVO == null){
                continue;
            }
            result.add(this.packageGoodsCustomResponse(supId2GoodsVoMap.get(esGoodsVO.getId()), esGoodsVO, goodsInfoVOList));
        }
        return result;
    }

    /**
     * 获取根据EsGoodsVo 转化成 GoodsCustomResponse
     * @return
     */
    public List<GoodsCustomResponse> listGoodsCustomV2(List<EsGoodsVO> resultEsGoodsList) {
        List<GoodsCustomResponse> result = new ArrayList<>();
        if (CollectionUtils.isEmpty(resultEsGoodsList)) {
            return result;
        }
        List<GoodsVO> goodsVOList = this.changeEsGoods2GoodsVo(resultEsGoodsList);
        if (CollectionUtils.isEmpty(goodsVOList)) {
            return result;
        }
        List<GoodsInfoVO> goodsInfoVOList = this.packageGoodsInfoListV2(resultEsGoodsList, new CustomerVO()); //添加客户信息
        if (CollectionUtils.isEmpty(goodsInfoVOList)) {
            return result;
        }
        Map<String, GoodsVO> supId2GoodsVoMap = goodsVOList.stream().collect(Collectors.toMap(GoodsVO::getGoodsId, Function.identity(), (k1, k2) -> k1));
        for (EsGoodsVO esGoodsVO : resultEsGoodsList) {
            GoodsVO goodsVO = supId2GoodsVoMap.get(esGoodsVO.getId());
            if (goodsVO == null){
                continue;
            }
            result.add(this.packageGoodsCustomResponse(supId2GoodsVoMap.get(esGoodsVO.getId()), esGoodsVO, goodsInfoVOList));
        }
        return result;
    }

    /**
     * 根据书单获取商品列表
     * @return
     */
    public BaseResponse<MicroServicePage<GoodsCustomResponse>> listGoodsByBookListModelId(Integer bookListModelId, int pageNum, int pageSize, Integer maxSize) {

        MicroServicePage<GoodsCustomResponse> result = new MicroServicePage<>();
        result.setTotal(0L);
        result.setContent(new ArrayList<>());
        if (bookListModelId == null) {
            return BaseResponse.success(result);
        }

        //获取书单信息
        BookListModelProviderRequest bookListModelProviderRequest = new BookListModelProviderRequest();
        bookListModelProviderRequest.setId(bookListModelId);
        BaseResponse<BookListModelProviderResponse> bookListModelProviderResponseBaseResponse = bookListModelProvider.findSimpleById(bookListModelProviderRequest);
        if (bookListModelProviderResponseBaseResponse.getContext() == null) {
            throw new IllegalArgumentException("书单id不存在");
        }
        //获取控件信息
        ChooseRuleProviderRequest chooseRuleProviderRequest = new ChooseRuleProviderRequest();
        chooseRuleProviderRequest.setBookListModelId(bookListModelId);
        chooseRuleProviderRequest.setCategoryId(CategoryEnum.BOOK_LIST_MODEL.getCode());
        BaseResponse<ChooseRuleProviderResponse> chooseRuleNoGoodsByConditionResponse = chooseRuleProvider.findChooseRuleNoGoodsByCondition(chooseRuleProviderRequest);
        ChooseRuleProviderResponse chooseRuleProviderResponse = chooseRuleNoGoodsByConditionResponse.getContext();
        if (chooseRuleProviderResponse == null) {
            throw new IllegalArgumentException("书单控件不存在");
        }
        BookListGoodsPublishProviderRequest request = new BookListGoodsPublishProviderRequest();
        request.setBookListIdColl(Collections.singletonList(bookListModelId));
        request.setCategoryId(CategoryEnum.BOOK_LIST_MODEL.getCode());
        request.setOperator("duan");
        BaseResponse<List<BookListGoodsPublishProviderResponse>> bookListGoodsPublishProviderResponses = bookListModelProvider.listBookListGoodsPublish(request);
        List<BookListGoodsPublishProviderResponse> goodsContext = bookListGoodsPublishProviderResponses.getContext();
        if (!CollectionUtils.isEmpty(goodsContext)) {

            //根据书单模版获取商品列表
            Set<String> spuIdSet = goodsContext.stream().map(BookListGoodsPublishProviderResponse::getSpuId).collect(Collectors.toSet());
            EsGoodsCustomQueryProviderRequest esGoodsCustomRequest = new EsGoodsCustomQueryProviderRequest();
            esGoodsCustomRequest.setGoodsChannelTypeSet(Collections.singletonList(commonUtil.getTerminal().getCode()));
            esGoodsCustomRequest.setPageNum(0);
            int esPageSize = spuIdSet.size();
            if (maxSize != null) {
                esPageSize = maxSize;
            }
            esGoodsCustomRequest.setPageSize(esPageSize); //TODO 一次性全部查询出来
            esGoodsCustomRequest.setGoodIdList(spuIdSet);
            if (!this.getIsCounselor()) {
                esGoodsCustomRequest.setCpsSpecial(0); //设置cps
            }
            BaseResponse<MicroServicePage<EsGoodsVO>> esGoodsVOMicroServiceResponse = esGoodsCustomQueryProvider.listEsGoodsNormal(esGoodsCustomRequest);
            MicroServicePage<EsGoodsVO> esGoodsVOMicroServicePage = esGoodsVOMicroServiceResponse.getContext();
            List<EsGoodsVO> content = esGoodsVOMicroServicePage.getContent();
            if (CollectionUtils.isEmpty(content)) {
                return BaseResponse.success(result);
            }

            //esGoodsVo to map
            Map<String, EsGoodsVO> esGoodsId2EsGoodsVoMap = content.stream().collect(Collectors.toMap(EsGoodsVO::getId, Function.identity(), (k1, k2) -> k1));

            List<EsGoodsVO> resultEsGoodsVoList = new ArrayList<>();
            List<EsGoodsVO> resultEsGoodsVoUnStockList = new ArrayList<>();

            int unShowSum = 0;

            for (BookListGoodsPublishProviderResponse bookListGoodsPublishParam : goodsContext) {
                EsGoodsVO esGoodsVOLocal = esGoodsId2EsGoodsVoMap.get(bookListGoodsPublishParam.getSpuId());
                //无效的对象信息
                if (esGoodsVOLocal == null) {
                    unShowSum++;
                    continue;
                }
                //无库存不展示
                if (FilterRuleEnum.getByCode(chooseRuleProviderResponse.getFilterRule()) == FilterRuleEnum.OUT_OF_STOCK_UN_SHOW && esGoodsVOLocal.getStock() <= 0) {
                    unShowSum++;
                    continue;
                }
                //无库存沉底
                if (FilterRuleEnum.getByCode(chooseRuleProviderResponse.getFilterRule()) == FilterRuleEnum.OUT_OF_STOCK_BOTTOM && esGoodsVOLocal.getStock() <= 0) {
                    resultEsGoodsVoUnStockList.add(esGoodsVOLocal);
                    continue;
                }
                resultEsGoodsVoList.add(esGoodsVOLocal);
            }

            resultEsGoodsVoList.addAll(resultEsGoodsVoUnStockList);


            long total = goodsContext.size() - unShowSum;
            total = total <= 0 ? 0 : total;

            int from = pageNum * pageSize;
            int to = (pageNum + 1) * pageSize;

            result.setTotal(total);
            if (from > total) {
                return BaseResponse.success(result);
            } else if (to > total) {
                to = Integer.parseInt(total+"");
            }

            List<EsGoodsVO> esGoodsVOList = resultEsGoodsVoList.subList(from, to);

            List<GoodsVO> goodsVOList = this.changeEsGoods2GoodsVo(esGoodsVOList);
            if (CollectionUtils.isEmpty(goodsVOList)) {
                return BaseResponse.success(result);
            }
            Map<String, GoodsVO> spuId2GoodsVoMap = goodsVOList.stream().collect(Collectors.toMap(GoodsVO::getGoodsId, Function.identity(), (k1, k2) -> k1));

            List<GoodsInfoVO> goodsInfoVOList = this.packageGoodsInfoList(esGoodsVOList, this.getCustomerVo());
            if (CollectionUtils.isEmpty(goodsInfoVOList)) {
                return BaseResponse.success(result);
            }
            result.setContent(esGoodsVOList.stream()
                    .filter(ex -> spuId2GoodsVoMap.get(ex.getId()) != null).map(ex ->
                            this.packageGoodsCustomResponse(spuId2GoodsVoMap.get(ex.getId()), ex, goodsInfoVOList)).collect(Collectors.toList()));
            return BaseResponse.success(result);
        }
        return BaseResponse.success(result);
    }


    /**
     * 根据 商品id 获取书单信息 goodsId-> BookListModelGoodsIdProviderResponse
     * @param goodsIdColl
     * @return
     */
    public Map<String, BookListModelGoodsIdProviderResponse> mapBookLitModelByGoodsIdColl(Collection<String> goodsIdColl) {
        BookListModelBySpuIdCollQueryRequest bookListModelBySpuIdCollQueryRequest = new BookListModelBySpuIdCollQueryRequest();
        bookListModelBySpuIdCollQueryRequest.setSpuIdCollection(goodsIdColl);
        bookListModelBySpuIdCollQueryRequest.setBusinessTypeList(Arrays.asList(BusinessTypeEnum.RANKING_LIST.getCode(), BusinessTypeEnum.BOOK_LIST.getCode(), BusinessTypeEnum.BOOK_RECOMMEND.getCode()));
        BaseResponse<List<BookListModelGoodsIdProviderResponse>> listBookListModelNoPageBySpuIdCollResponse =
                bookListModelProvider.listBookListModelNoPageBySpuIdColl(bookListModelBySpuIdCollQueryRequest);
        List<BookListModelGoodsIdProviderResponse> listBookListModelNoPageBySpuIdColl = listBookListModelNoPageBySpuIdCollResponse.getContext();
        //list转化成map
        return listBookListModelNoPageBySpuIdColl.stream().collect(Collectors.toMap(BookListModelGoodsIdProviderResponse::getSpuId, Function.identity(), (k1, k2) -> k1));
    }
}
