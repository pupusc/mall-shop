package com.soybean.mall.goods.service;

import com.soybean.common.util.StockUtil;
import com.soybean.elastic.api.resp.EsSpuNewResp;
import com.soybean.mall.common.CommonUtil;
import com.soybean.mall.common.RedisService;
import com.soybean.mall.goods.dto.SpuRecomBookListDTO;
import com.soybean.mall.goods.response.SpuNewBookListResp;
import com.soybean.marketing.api.provider.activity.NormalActivityPointSkuProvider;
import com.soybean.marketing.api.req.SpuNormalActivityReq;
import com.soybean.marketing.api.resp.SkuNormalActivityResp;
import com.wanmi.sbc.common.constant.RedisKeyConstant;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.provider.paidcardcustomerrel.PaidCardCustomerRelQueryProvider;
import com.wanmi.sbc.customer.api.request.customer.CustomerGetByIdRequest;
import com.wanmi.sbc.customer.api.request.paidcardcustomerrel.MaxDiscountPaidCardRequest;
import com.wanmi.sbc.customer.api.response.customer.CustomerGetByIdResponse;
import com.wanmi.sbc.customer.bean.dto.CustomerDTO;
import com.wanmi.sbc.customer.bean.vo.PaidCardVO;
import com.wanmi.sbc.goods.api.enums.StateEnum;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoListByConditionRequest;
import com.wanmi.sbc.goods.bean.dto.GoodsInfoDTO;
import com.wanmi.sbc.goods.bean.dto.MarketingLabelNewDTO;
import com.wanmi.sbc.goods.bean.enums.AddedFlag;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import com.wanmi.sbc.goods.bean.enums.GoodsPriceType;
import com.wanmi.sbc.goods.bean.enums.PublishState;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.marketing.api.provider.plugin.MarketingPluginProvider;
import com.wanmi.sbc.marketing.api.request.plugin.MarketingPluginGoodsListFilterRequest;
import com.wanmi.sbc.setting.api.provider.AtmosphereProvider;
import com.wanmi.sbc.setting.api.request.AtmosphereQueryRequest;
import com.wanmi.sbc.setting.bean.dto.AtmosphereDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Description: sp
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/9 1:00 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
public class SpuNewSearchService {

    @Autowired
    private SpuNewBookListService spuNewBookListService;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    @Autowired
    private MarketingPluginProvider marketingPluginProvider;

    @Autowired
    private CustomerQueryProvider customerQueryProvider;

    @Autowired
    private AtmosphereProvider atmosphereProvider;

    @Autowired
    private NormalActivityPointSkuProvider normalActivityPointSkuProvider;

    @Autowired
    private PaidCardCustomerRelQueryProvider paidCardCustomerRelQueryProvider;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private RedisService redisService;


    /**
     * 搜索商品书单信息
     * @param esSpuNewRespList
     * @return
     */
    public List<SpuNewBookListResp> listSpuNewSearch(List<EsSpuNewResp> esSpuNewRespList, CustomerGetByIdResponse customer){
        return this.packageSpuNewBookListResp(esSpuNewRespList, customer, false, new ArrayList<>());
    }

    /**
     * 搜索商品书单信息并以及关联的skus
     */
    public List<SpuNewBookListResp> listSpuNewSearch(List<EsSpuNewResp> esSpuNewRespList, CustomerGetByIdResponse customer, boolean fetchSkus){
        return this.packageSpuNewBookListResp(esSpuNewRespList, customer, fetchSkus, new ArrayList<>());
    }

    /**
     * 搜索商品书单信息
     * @param esSpuNewRespList
     * @return
     */
    public List<SpuNewBookListResp> listSpuNewSearch(List<EsSpuNewResp> esSpuNewRespList, List<String> showSkuIds){
        CustomerGetByIdResponse customer = null;
        String userId = commonUtil.getOperatorId();
        if (!StringUtils.isEmpty(userId)) {
            customer = customerQueryProvider.getCustomerById(new CustomerGetByIdRequest(userId)).getContext();
        }
        return this.packageSpuNewBookListResp(esSpuNewRespList, customer, false, showSkuIds);
    }

    /**
     * 封装商品信息
     * @param esSpuNewRespList
     * @return
     */
    private List<SpuNewBookListResp> packageSpuNewBookListResp(List<EsSpuNewResp> esSpuNewRespList, CustomerGetByIdResponse customer, boolean fetchSkus, List<String> showSkuIds){
        if (CollectionUtils.isEmpty(esSpuNewRespList)) {
            return new ArrayList<>();
        }
        //获取商品对应的书单信息
        List<String> spuIdList = esSpuNewRespList.stream().map(EsSpuNewResp::getSpuId).collect(Collectors.toList());
        Map<String, SpuRecomBookListDTO> spuId2SpuRecomBookListDTOMap = spuNewBookListService.getSpuId2EsBookListModelResp(spuIdList);


        //获取goodsInfo信息
//        BigDecimal salePrice = new BigDecimal("9999");
        Map<String, GoodsInfoVO> spuId2HasStockGoodsInfoVoMap = new HashMap<>();
        Map<String, GoodsInfoVO> spuId2UnHasStockGoodsInfoVoMap = new HashMap<>();
        //指定showSkuId信息
        Map<String, GoodsInfoVO> skuId2DirectGoodsInfoVoMap = new HashMap<>();

        boolean hasCustomerVip = false;

        GoodsInfoListByConditionRequest request = new GoodsInfoListByConditionRequest();
        request.setGoodsIds(spuIdList);
        request.setAuditStatus(CheckStatus.CHECKED);
        request.setDelFlag(DeleteFlag.NO.toValue());
        request.setAddedFlag(AddedFlag.YES.toValue());
        request.setShowSpecFlag(true);
        List<GoodsInfoVO> goodsInfos = goodsInfoQueryProvider.listByCondition(request).getContext().getGoodsInfos();
        if (!CollectionUtils.isEmpty(goodsInfos)) {
            MarketingPluginGoodsListFilterRequest filterRequest = new MarketingPluginGoodsListFilterRequest();
            List<GoodsInfoDTO> goodsInfoDTOS = new ArrayList<>();
            for (GoodsInfoVO goodsInfo : goodsInfos) {
                GoodsInfoDTO goodsInfoDTO = KsBeanUtil.convert(goodsInfo, GoodsInfoDTO.class);
                goodsInfoDTO.setPriceType(GoodsPriceType.MARKET.toValue()); //此处强制设置为市场价来计算折扣
                goodsInfoDTOS.add(goodsInfoDTO);
            }
            filterRequest.setGoodsInfos(goodsInfoDTOS);
            if (Objects.nonNull(customer)) {
                filterRequest.setCustomerDTO(KsBeanUtil.convert(customer, CustomerDTO.class));
                //获取vip信息
                MaxDiscountPaidCardRequest maxDiscountPaidCardRequest = new MaxDiscountPaidCardRequest();
                maxDiscountPaidCardRequest.setCustomerId(customer.getCustomerId());
                List<PaidCardVO> paidCardVOList = paidCardCustomerRelQueryProvider.getMaxDiscountPaidCard(maxDiscountPaidCardRequest).getContext();
                hasCustomerVip = !CollectionUtils.isEmpty(paidCardVOList);
            }
            goodsInfos = marketingPluginProvider.goodsListFilter(filterRequest).getContext().getGoodsInfoVOList();

            for (GoodsInfoVO goodsInfoParam : goodsInfos) {
                MarketingLabelNewDTO marketingLabel=goodsInfoQueryProvider.getMarketingLabelsBySKu(goodsInfoParam.getGoodsInfoId()).getContext();
                if(null!=marketingLabel){
                    if(null!=marketingLabel.getSale_num()){
                        goodsInfoParam.setSaleNum(marketingLabel.getSale_num());
                    }
                }else {
                    if (goodsInfoParam.getSalePrice() == null) {
                        goodsInfoParam.setSalePrice(goodsInfoParam.getMarketPrice());
                    }
                }
                 if (goodsInfoParam.getStock() <= StockUtil.THRESHOLD_STOCK) {
                     goodsInfoParam.setStock(0L); //设置库存为0
                 }

                 //如果制定skuId则使用对应的skuid
                if (!CollectionUtils.isEmpty(showSkuIds) && showSkuIds.contains(goodsInfoParam.getGoodsInfoId())) {
                    skuId2DirectGoodsInfoVoMap.put(goodsInfoParam.getGoodsId(), goodsInfoParam);
                    continue;
                }

                 //存在库存 规格：如果有库存则取库存里面价格最低的商品，如果没有商品则获取 库存最低里面价格最低的商品 skuInfo 设置skuid
                 if (goodsInfoParam.getStock() > 0) {
                     GoodsInfoVO goodsInfoHasStock = spuId2HasStockGoodsInfoVoMap.get(goodsInfoParam.getGoodsId());
                     //如果map中没有，则添加到
                     if (goodsInfoHasStock == null) {
                         spuId2HasStockGoodsInfoVoMap.put(goodsInfoParam.getGoodsId(), goodsInfoParam);
                         spuId2UnHasStockGoodsInfoVoMap.remove(goodsInfoParam.getGoodsId()); //如果无库存中存在，则移除掉数据
                     } else {
                        if (goodsInfoParam.getSalePrice().compareTo(goodsInfoHasStock.getSalePrice()) < 0) {
                            spuId2HasStockGoodsInfoVoMap.put(goodsInfoParam.getGoodsId(), goodsInfoParam);
                            spuId2UnHasStockGoodsInfoVoMap.remove(goodsInfoParam.getGoodsId()); //如果无库存中存在，则移除掉数据
                        }
                     }
                 } else {
                    //如果有库存，则继续
                     GoodsInfoVO goodsInfoHasStock = spuId2HasStockGoodsInfoVoMap.get(goodsInfoParam.getGoodsId());
                     if (goodsInfoHasStock != null) {
                         continue;
                     }

                     GoodsInfoVO goodsInfoUnHasStock = spuId2UnHasStockGoodsInfoVoMap.get(goodsInfoParam.getGoodsId());
                     if (goodsInfoUnHasStock == null) {
                         spuId2UnHasStockGoodsInfoVoMap.put(goodsInfoParam.getGoodsId(), goodsInfoParam);
                     } else {
                         if (goodsInfoParam.getSalePrice().compareTo(goodsInfoUnHasStock.getSalePrice()) < 0) {
                             spuId2UnHasStockGoodsInfoVoMap.put(goodsInfoParam.getGoodsId(), goodsInfoParam);
                         }
                     }
                 }
            }
        }


        //获取氛围图信息 atmosphere
        Map<String, AtmosphereDTO> skuId2AtomsphereMap = new HashMap<>();
        if (!spuId2HasStockGoodsInfoVoMap.isEmpty() || !spuId2UnHasStockGoodsInfoVoMap.isEmpty()) {
            List<String> skuIdList = new ArrayList<>();
            skuIdList.addAll(spuId2HasStockGoodsInfoVoMap.values().stream().map(GoodsInfoVO::getGoodsInfoId).collect(Collectors.toList()));
            skuIdList.addAll(spuId2UnHasStockGoodsInfoVoMap.values().stream().map(GoodsInfoVO::getGoodsInfoId).collect(Collectors.toList()));
            AtmosphereQueryRequest atmosphereQueryRequest = new AtmosphereQueryRequest();
            atmosphereQueryRequest.setSkuId(skuIdList);
            List<AtmosphereDTO> context = atmosphereProvider.listAtmosphere(atmosphereQueryRequest).getContext();
            LocalDateTime now = LocalDateTime.now();
            for (AtmosphereDTO atmosphereDTO : context) {
                if (atmosphereDTO.getStartTime() == null || atmosphereDTO.getEndTime() == null) {
                    continue;
                }
                if (atmosphereDTO.getStartTime().compareTo(now) < 0 && atmosphereDTO.getEndTime().compareTo(now) >0) {
                    skuId2AtomsphereMap.put(atmosphereDTO.getSkuId(), atmosphereDTO);
                }
            }
        }


        //normalActivity 活动信息
        Map<String, SkuNormalActivityResp> spuId2NormalActivityMap = new HashMap<>();
        SpuNormalActivityReq spuNormalActivityReq = new SpuNormalActivityReq();
        spuNormalActivityReq.setSpuIds(spuIdList);
        spuNormalActivityReq.setStatus(StateEnum.RUNNING.getCode());
        spuNormalActivityReq.setPublishState(PublishState.ENABLE.toValue());
        spuNormalActivityReq.setChannelTypes(Collections.singletonList(commonUtil.getTerminal().getCode()));
        spuNormalActivityReq.setCustomerId(commonUtil.getOperatorId());
        List<SkuNormalActivityResp> skuNormalActivityResps = normalActivityPointSkuProvider.listSpuRunningNormalActivity(spuNormalActivityReq).getContext();
        for (SkuNormalActivityResp skuNormalActivityRespParam : skuNormalActivityResps) {
            SkuNormalActivityResp skuNormalActivityResp = spuId2NormalActivityMap.get(skuNormalActivityRespParam.getSpuId());
            if (skuNormalActivityResp == null) {
                spuId2NormalActivityMap.put(skuNormalActivityRespParam.getSpuId(), skuNormalActivityRespParam);
            } else {
                if (skuNormalActivityResp.getNum() < skuNormalActivityRespParam.getNum()) {
                    spuId2NormalActivityMap.put(skuNormalActivityRespParam.getSpuId(), skuNormalActivityRespParam);
                }
            }
        }


        List<SpuNewBookListResp> result = new ArrayList<>();

        for (EsSpuNewResp esSpuNewRespParam : esSpuNewRespList) {
            GoodsInfoVO goodsInfoVOShow = skuId2DirectGoodsInfoVoMap.get(esSpuNewRespParam.getSpuId());
            GoodsInfoVO goodsInfoVO = null;
            if (goodsInfoVOShow == null) {
                GoodsInfoVO goodsInfoVOTmp = spuId2HasStockGoodsInfoVoMap.get(esSpuNewRespParam.getSpuId());
                goodsInfoVO = goodsInfoVOTmp != null ? goodsInfoVOTmp
                        : spuId2UnHasStockGoodsInfoVoMap.get(esSpuNewRespParam.getSpuId()) == null
                        ? null : spuId2UnHasStockGoodsInfoVoMap.get(esSpuNewRespParam.getSpuId());
            } else {
                goodsInfoVO = goodsInfoVOShow;
            }

            if (goodsInfoVO == null) {
                continue;
            }

            SpuNewBookListResp spuNewBookListResp = new SpuNewBookListResp();
            spuNewBookListResp.setSpuId(esSpuNewRespParam.getSpuId());
            spuNewBookListResp.setSkuId(goodsInfoVO.getGoodsInfoId());
            spuNewBookListResp.setSpuName(esSpuNewRespParam.getSpuName());
            spuNewBookListResp.setSpuSubName(esSpuNewRespParam.getSpuSubName());
            spuNewBookListResp.setSpuCategory(esSpuNewRespParam.getSpuCategory());

            if (!CollectionUtils.isEmpty(goodsInfoVO.getCouponLabels())) {
                List<SpuNewBookListResp.CouponLabel> cpnLabels = goodsInfoVO.getCouponLabels().stream().map(i -> {
                    SpuNewBookListResp.CouponLabel cpnLabel = new SpuNewBookListResp.CouponLabel();
                    BeanUtils.copyProperties(i, cpnLabel);
                    return cpnLabel;
                }).collect(Collectors.toList());
                spuNewBookListResp.setCouponLabels(cpnLabels);
            }
            if (!CollectionUtils.isEmpty(goodsInfoVO.getMarketingLabels())) {
                List<SpuNewBookListResp.MarketingLabel> mktLabels = goodsInfoVO.getMarketingLabels().stream().map(i -> {
                    SpuNewBookListResp.MarketingLabel mktLable = new SpuNewBookListResp.MarketingLabel();
                    BeanUtils.copyProperties(i, mktLable);
                    return mktLable;
                }).collect(Collectors.toList());
                spuNewBookListResp.setMarketingLabels(mktLabels);
            }

            if (esSpuNewRespParam.getBook() != null) {
                EsSpuNewResp.Book book = esSpuNewRespParam.getBook();
                SpuNewBookListResp.Book resultBook = new SpuNewBookListResp.Book();
                resultBook.setAuthorNames(book.getAuthorNames());
                resultBook.setScore(book.getScore());
                resultBook.setPublisher(book.getPublisher());
                resultBook.setFixPrice(book.getFixPrice());

                List<SpuNewBookListResp.Book.BookTag> resultTag = new ArrayList<>();
                if (!CollectionUtils.isEmpty(book.getTags())) {
                    for (EsSpuNewResp.Book.SubBookLabel tagParam : book.getTags()) {
                        SpuNewBookListResp.Book.BookTag resultBookTag = new SpuNewBookListResp.Book.BookTag();
                        resultBookTag.setTageId(tagParam.getTagId());
                        resultBookTag.setTagName(tagParam.getTagName());
                        resultTag.add(resultBookTag);
                    }
                }
                resultBook.setTags(resultTag);
                spuNewBookListResp.setBook(resultBook);
            }

            //主播推荐标签
            List<EsSpuNewResp.SubAnchorRecom> anchorRecoms = esSpuNewRespParam.getAnchorRecoms();
            if (!CollectionUtils.isEmpty(anchorRecoms)) {
                //排序获取
                anchorRecoms.sort((o1, o2) -> o1.getRecomId() - o2.getRecomId());
                spuNewBookListResp.setAnchorRecomName(anchorRecoms.get(0).getRecomName());
            }

            //获取书单排行榜信息
            SpuRecomBookListDTO spuRecomBookListDTO = spuId2SpuRecomBookListDTOMap.get(esSpuNewRespParam.getSpuId());
            SpuNewBookListResp.BookList bookList = spuRecomBookListDTO != null ?
                    new SpuNewBookListResp.BookList(spuRecomBookListDTO.getBookListNameShow(),spuRecomBookListDTO.getBookListName(), spuRecomBookListDTO.getSpu() == null ? 1
                            : spuRecomBookListDTO.getSpu().getSortNum(), spuRecomBookListDTO.getBookListBusinessType()) : null;
            spuNewBookListResp.setBookList(bookList);

            //获取图书氛围信息
            AtmosphereDTO atmosphereDTO = skuId2AtomsphereMap.get(goodsInfoVO.getGoodsInfoId());
            if (atmosphereDTO != null) {
                SpuNewBookListResp.Atmosphere atmosphere = new SpuNewBookListResp.Atmosphere();
                atmosphere.setImageUrl(atmosphereDTO.getImageUrl());
                atmosphere.setAtmosType(atmosphereDTO.getAtmosType());
                atmosphere.setElementOne(atmosphereDTO.getElementOne());
                atmosphere.setElementTwo(atmosphereDTO.getElementTwo());
                atmosphere.setElementThree(atmosphereDTO.getElementThree());
                atmosphere.setElementFour(atmosphereDTO.getElementFour());
                spuNewBookListResp.setAtmosphere(atmosphere);
            }

            //获取活动信息
            SkuNormalActivityResp skuNormalActivityResp = spuId2NormalActivityMap.get(esSpuNewRespParam.getSpuId());
            if (skuNormalActivityResp != null) {
                List<SpuNewBookListResp.NormalActivity> activities = new ArrayList<>();
                SpuNewBookListResp.NormalActivity activity = new SpuNewBookListResp.NormalActivity();
                activity.setNum(skuNormalActivityResp.getNum());
                activity.setActivityShow(String.format("返%d积分", skuNormalActivityResp.getNum()));
                activities.add(activity);
                spuNewBookListResp.setActivities(activities);
//                spuNewBookListResp.setSkuId(skuNormalActivityResp.getSkuId());
            }

            //标签信息
            List<EsSpuNewResp.SubLabel> labelResps = esSpuNewRespParam.getLabels();
            if (!CollectionUtils.isEmpty(labelResps)) {
                List<SpuNewBookListResp.Label> labels = new ArrayList<>();
                for (EsSpuNewResp.SubLabel labelResp : labelResps) {
                    SpuNewBookListResp.Label label = new SpuNewBookListResp.Label();
                    label.setLabelCategory(labelResp.getCategory());
                    label.setLabelName(labelResp.getLabelName());
                    labels.add(label);
                }
                spuNewBookListResp.setLabels(labels);
            }
            if(null!=esSpuNewRespParam.getSpuLabels()&&esSpuNewRespParam.getSpuLabels().size()>0) {
                List<SpuNewBookListResp.SubSpuLabelNew> spuLabels=new ArrayList<>();
                esSpuNewRespParam.getSpuLabels().forEach(e->{
                    SpuNewBookListResp.SubSpuLabelNew labelNew=new SpuNewBookListResp.SubSpuLabelNew();
                    labelNew.setId(e.getId());
                    labelNew.setType(e.getType());
                    labelNew.setName(e.getName());
                    labelNew.setShowName(e.getShowName());
                    labelNew.setOrderType(e.getOrderType());
                    spuLabels.add(labelNew);
                });
                spuNewBookListResp.setSpuLabels(spuLabels);
            }
            if(null!=esSpuNewRespParam.getMarketingLabel()&&esSpuNewRespParam.getMarketingLabel().size()>0) {
                spuNewBookListResp.setMarketingLabel(KsBeanUtil.convertList(esSpuNewRespParam.getMarketingLabel(), SpuNewBookListResp.SubSkuMarketingLabelNew.class));
            }
            spuNewBookListResp.setStock(goodsInfoVO.getStock());
            spuNewBookListResp.setSalesPrice(goodsInfoVO.getSalePrice());
            spuNewBookListResp.setSaleNum(goodsInfoVO.getSaleNum());
            spuNewBookListResp.setMarketPrice(goodsInfoVO.getFixPrice()!=null&&!(goodsInfoVO.getFixPrice().compareTo(BigDecimal.ZERO)==0)?goodsInfoVO.getFixPrice():(spuNewBookListResp.getBook()!=null?(null!=spuNewBookListResp.getBook().getFixPrice()?BigDecimal.valueOf(spuNewBookListResp.getBook().getFixPrice()) :goodsInfoVO.getMarketPrice()):goodsInfoVO.getMarketPrice()));
            spuNewBookListResp.setHasVip(hasCustomerVip ? 1 : 0);
            spuNewBookListResp.setSpecMore(!StringUtils.isEmpty(goodsInfoVO.getSpecText()));
            spuNewBookListResp.setPic(esSpuNewRespParam.getPic());
            spuNewBookListResp.setUnBackgroundPic(esSpuNewRespParam.getUnBackgroundPic());
            result.add(spuNewBookListResp);
        }

        if (fetchSkus && !CollectionUtils.isEmpty(goodsInfos)) {
            Map<String, List<GoodsInfoVO>> spuId2skus = goodsInfos.stream().collect(Collectors.groupingBy(GoodsInfoVO::getGoodsId));
            result.forEach(spu -> spu.setSkus(spuId2skus.getOrDefault(spu.getSpuId(), new ArrayList<>())));
        }
        return result;
    }

}
