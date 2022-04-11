package com.wanmi.sbc.topic.service;


import com.wanmi.sbc.booklistmodel.BookListModelAndGoodsService;
import com.wanmi.sbc.booklistmodel.response.GoodsCustomResponse;
import com.wanmi.sbc.booklistmodel.response.GoodsExtPropertiesCustomResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.bean.enums.StoreState;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticQueryProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoQueryRequest;
import com.wanmi.sbc.elastic.bean.vo.goods.EsGoodsInfoVO;
import com.wanmi.sbc.elastic.bean.vo.goods.EsGoodsVO;
import com.wanmi.sbc.elastic.bean.vo.goods.GoodsInfoNestVO;
import com.wanmi.sbc.elastic.bean.vo.goods.GoodsLabelNestVO;
import com.wanmi.sbc.goods.api.provider.goods.GoodsQueryProvider;
import com.wanmi.sbc.goods.bean.enums.AddedFlag;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GoodsVO;
import com.wanmi.sbc.marketing.api.provider.coupon.CouponCacheProvider;
import com.wanmi.sbc.marketing.api.request.coupon.CouponCacheCenterPageRequest;
import com.wanmi.sbc.marketing.api.response.coupon.CouponCacheCenterPageResponse;
import com.wanmi.sbc.marketing.bean.enums.CouponSceneType;
import com.wanmi.sbc.marketing.bean.enums.ScopeType;
import com.wanmi.sbc.marketing.bean.vo.CouponVO;
import com.wanmi.sbc.setting.api.provider.topic.TopicConfigProvider;
import com.wanmi.sbc.setting.api.request.topicconfig.TopicQueryRequest;
import com.wanmi.sbc.setting.bean.dto.AtmosphereDTO;
import com.wanmi.sbc.setting.bean.dto.TopicStoreyContentDTO;
import com.wanmi.sbc.setting.bean.enums.TopicStoreyType;
import com.wanmi.sbc.setting.bean.vo.TopicActivityVO;
import com.wanmi.sbc.topic.response.GoodsAndAtmosphereResponse;
import com.wanmi.sbc.topic.response.TopicResponse;
import com.wanmi.sbc.topic.response.TopicStoreyContentReponse;
import com.wanmi.sbc.topic.response.TopicStoreyResponse;
import com.wanmi.sbc.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TopicService {

    @Autowired
    private TopicConfigProvider topicConfigProvider;

    @Autowired
    private EsGoodsInfoElasticQueryProvider esGoodsInfoElasticQueryProvider;

    @Autowired
    private BookListModelAndGoodsService bookListModelAndGoodsService;

    @Autowired
    private CouponCacheProvider couponCacheProvider;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private GoodsQueryProvider goodsQueryProvider;

    public BaseResponse<TopicResponse> detail(TopicQueryRequest request,Boolean allLoad){
        BaseResponse<TopicActivityVO> activityVO =  topicConfigProvider.detail(request);
        if(activityVO == null || activityVO.getContext() ==null){
            return BaseResponse.success(null);
        }
        TopicResponse response = KsBeanUtil.convert(activityVO.getContext(),TopicResponse.class);
        //如果配置有一行两个商品的配置信息，查询商品
        List<String> skuIds = new ArrayList<>();
        //图片+链接解析spuId
        initSpuId(response);
        if(CollectionUtils.isEmpty(activityVO.getContext().getStoreyList())){
            return BaseResponse.success(response);
        }
        //轮播图要加载
        if(!allLoad) {
            int index = response.getStoreyList().size() > 1 ? 2 : 1;
            for (int i= index ;i<response.getStoreyList().size();i++){
                if(!response.getStoreyList().get(i).getStoreyType().equals(TopicStoreyType.HETERSCROLLIMAGE.getId())){
                    response.getStoreyList().get(i).setContents(null);
                }
            }
        }
        //商品属性
        response.getStoreyList().stream().filter(p->p.getStoreyType()!= null && p.getStoreyType().equals(TopicStoreyType.TWOGOODS.getId())).forEach(p->{
            if(CollectionUtils.isNotEmpty(p.getContents())) {
                skuIds.addAll(p.getContents().stream().filter(c -> c.getType() != null && c.getType().equals(1)).map(TopicStoreyContentDTO::getSkuId).collect(Collectors.toList()));
            } });
        if(CollectionUtils.isNotEmpty(skuIds)){
            List<GoodsCustomResponse> list = initGoods(skuIds);
            response.getStoreyList().stream().filter(p->p.getStoreyType().equals(3)).forEach(p->{
                if(CollectionUtils.isEmpty(p.getContents())){
                    return;
                }
                List<TopicStoreyContentReponse> contents = new ArrayList<>(p.getContents().size());
                p.getContents().stream().filter(g -> g.getType().equals(1)).forEach(g -> {
                    if (CollectionUtils.isNotEmpty(list) && list.stream().anyMatch(l -> l.getGoodsInfoId().equals(g.getSkuId()))) {
                        GoodsCustomResponse goodsCustomResponse = list.stream().filter(l -> l.getGoodsInfoId().equals(g.getSkuId())).findFirst().get();
                        g.setGoods(KsBeanUtil.convert(goodsCustomResponse, GoodsAndAtmosphereResponse.class));
                        contents.add(g);
                    }
                });
                contents.addAll(p.getContents().stream().filter(o->o.getType().equals(2)).collect(Collectors.toList()));
                p.setContents(contents);
            });

        }
        initCoupon(response.getStoreyList());
        return BaseResponse.success(response);
    }

    /**
     * 初始化优惠券信息
     */
    private void initCoupon(List<TopicStoreyResponse> storeyList){
        if(!storeyList.stream().anyMatch(p->p.getStoreyType()!= null && p.getStoreyType().equals(TopicStoreyType.COUPON.getId()))){
            return;
        }
        //优惠券
        List<String> activityIds = new ArrayList<>();
        List<String> couponIds = new ArrayList<>();
        storeyList.stream().filter(p->p.getStoreyType()!= null && p.getStoreyType().equals(TopicStoreyType.COUPON.getId())).forEach(p->{
            if(CollectionUtils.isNotEmpty(p.getContents())) {
                activityIds.addAll(p.getContents().stream().map(TopicStoreyContentDTO::getActivityId).collect(Collectors.toList()));
                couponIds.addAll(p.getContents().stream().map(TopicStoreyContentDTO::getCouponId).collect(Collectors.toList()));
            }
        });
        CouponCacheCenterPageRequest couponRequest = new CouponCacheCenterPageRequest();
        couponRequest.setActivityIds(activityIds);
        couponRequest.setCouponInfoIds(couponIds);
        couponRequest.setCouponScene(Arrays.asList(CouponSceneType.TOPIC.getType().toString()));
        couponRequest.setPageNum(0);
        couponRequest.setPageSize(100);
        if(commonUtil.getOperator()!=null && commonUtil.getOperatorId() !=null){
            couponRequest.setCustomerId(commonUtil.getOperatorId());
        }
        BaseResponse<CouponCacheCenterPageResponse> couponResponse =  couponCacheProvider.pageCouponStarted(couponRequest);
        if(couponResponse == null || couponResponse.getContext() == null || couponResponse.getContext().getCouponViews() == null || CollectionUtils.isEmpty(couponResponse.getContext().getCouponViews().getContent())){
            return;
        }
        List<CouponVO> couponVOS = couponResponse.getContext().getCouponViews().getContent();
        List<TopicStoreyContentReponse.CouponInfo> couponInfos = KsBeanUtil.convertList(couponVOS,TopicStoreyContentReponse.CouponInfo.class);
        // 券可用商品的第一个
        String typeAllGoodsId = null;
        Map<String, String> typeStoreCateGoodsId = new HashMap<>();
        for (CouponVO couponVO : couponVOS) {
            if (ScopeType.STORE_CATE.equals(couponVO.getScopeType())) {
                //适用店铺分类
                if(CollectionUtils.isNotEmpty(couponVO.getScopeIds())){
                    if(!typeStoreCateGoodsId.containsKey(couponVO.getScopeIds().get(0))){
                        BaseResponse<String> goodsId = goodsQueryProvider.getGoodsIdByClassify(Integer.parseInt(couponVO.getScopeIds().get(0)));
                        typeStoreCateGoodsId.put(couponVO.getScopeIds().get(0), goodsId.getContext());
                    }
                    for (TopicStoreyContentReponse.CouponInfo couponInfo : couponInfos) {
                        if(couponInfo.getCouponId().equals(couponVO.getCouponId())){
                            couponInfo.setFirstGoodsId(typeStoreCateGoodsId.get(couponVO.getScopeIds().get(0)));
                            break;
                        }
                    }
                }
            }else if (ScopeType.ALL.equals(couponVO.getScopeType()) || ScopeType.BOSS_CATE.equals(couponVO.getScopeType()) || ScopeType.BRAND.equals(couponVO.getScopeType())){
                if(typeAllGoodsId == null){
                    BaseResponse<String> goodsId = goodsQueryProvider.getGoodsId(Collections.emptyList());
                    typeAllGoodsId = goodsId.getContext();
                }
                for (TopicStoreyContentReponse.CouponInfo couponInfo : couponInfos) {
                    if(couponInfo.getCouponId().equals(couponVO.getCouponId())){
                        couponInfo.setFirstGoodsId(typeAllGoodsId);
                        break;
                    }
                }
            }else if (ScopeType.SKU.equals(couponVO.getScopeType())){
                if(CollectionUtils.isNotEmpty(couponVO.getScopeIds())){
                    BaseResponse<String> goodsId = goodsQueryProvider.getGoodsId(couponVO.getScopeIds());
                    for (TopicStoreyContentReponse.CouponInfo couponInfo : couponInfos) {
                        if(couponInfo.getCouponId().equals(couponVO.getCouponId())){
                            couponInfo.setFirstGoodsId(goodsId.getContext());
                            break;
                        }
                    }
                }
            }
        }
        storeyList.stream().filter(p->p.getStoreyType()!= null && p.getStoreyType().equals(TopicStoreyType.COUPON.getId())).forEach(p->{
            if(CollectionUtils.isEmpty(p.getContents())) {
                return;
            }
            p.getContents().forEach(c->{
                Optional<TopicStoreyContentReponse.CouponInfo> optionalCouponVO = couponInfos.stream().filter(coupon->coupon.getActivityId().equals(c.getActivityId()) && coupon.getCouponId().equals(c.getCouponId())).findFirst();
                if(optionalCouponVO.isPresent()){
                    c.setCouponInfo(optionalCouponVO.get());
                }
            });
        });

   }
    private List<GoodsCustomResponse> initGoods(List<String> goodsInfoIds) {
        List<GoodsCustomResponse> goodList = new ArrayList<>();
        //根据商品id列表 获取商品列表信息
        EsGoodsInfoQueryRequest queryRequest = new EsGoodsInfoQueryRequest();
        queryRequest.setPageNum(0);
        queryRequest.setPageSize(goodsInfoIds.size());
        queryRequest.setGoodsInfoIds(goodsInfoIds);
        queryRequest.setQueryGoods(true);
        queryRequest.setAddedFlag(AddedFlag.YES.toValue());
        queryRequest.setDelFlag(DeleteFlag.NO.toValue());
        queryRequest.setAuditStatus(CheckStatus.CHECKED.toValue());
        queryRequest.setStoreState(StoreState.OPENING.toValue());
        queryRequest.setVendibility(Constants.yes);
        queryRequest.setGoodsChannelTypeSet(Collections.singletonList(commonUtil.getTerminal().getCode()));
        //查询商品
        List<EsGoodsVO> esGoodsVOS = esGoodsInfoElasticQueryProvider.pageByGoods(queryRequest).getContext().getEsGoods().getContent();
        List<GoodsCustomResponse> result=  bookListModelAndGoodsService.listGoodsCustom(esGoodsVOS);
        for (EsGoodsVO goodsVo : esGoodsVOS) {
            Optional<GoodsCustomResponse> goodsCustom = result.stream().filter(p->p.getGoodsId().equals(goodsVo.getId())).findFirst();
            if(goodsCustom.isPresent()) {
                goodsVo.getGoodsInfos().forEach(p -> {
                    GoodsCustomResponse goods = new GoodsCustomResponse();
                    goods.setGoodsId(goodsCustom.get().getGoodsId());
                    goods.setGoodsNo(goodsCustom.get().getGoodsNo());
                    goods.setGoodsInfoId(p.getGoodsInfoId());
                    goods.setGoodsInfoNo(p.getGoodsInfoNo());
                    goods.setGoodsName(goodsCustom.get().getGoodsName());
                    goods.setGoodsSubName(goodsCustom.get().getGoodsSubName());
                    goods.setGoodsCoverImg(goodsCustom.get().getGoodsCoverImg());
                    goods.setGoodsUnBackImg(goodsCustom.get().getGoodsUnBackImg());
                    goods.setCpsSpecial(goodsCustom.get().getCpsSpecial());
                    goods.setShowPrice(goodsCustom.get().getShowPrice());
                    goods.setLinePrice(goodsCustom.get().getLinePrice());
                    goods.setMarketingPrice(goodsCustom.get().getMarketingPrice());
                    goods.setCouponLabelList(goodsCustom.get().getCouponLabelList());
                    goods.setGoodsLabelList(goodsCustom.get().getGoodsLabelList());
                    goods.setGoodsScore(goodsCustom.get().getGoodsScore());
                    goods.setStock(goodsCustom.get().getStock());
                    goods.setGoodsExtProperties(goodsCustom.get().getGoodsExtProperties());
                    if(p.getStartTime()!=null && p.getEndTime()!=null && p.getStartTime().compareTo(LocalDateTime.now()) <0 && p.getEndTime().compareTo(LocalDateTime.now()) > 0) {
                        goods.setAtmosType(p.getAtmosType());
                        goods.setImageUrl(p.getImageUrl());
                        goods.setElementOne(p.getElementOne());
                        goods.setElementTwo(p.getElementTwo());
                        goods.setElementThree(p.getElementThree());
                        goods.setElementFour(p.getElementFour());
                    }else{
                        goods.setAtmosType(null);
                        goods.setImageUrl(null);
                        goods.setElementOne(null);
                        goods.setElementTwo(null);
                        goods.setElementThree(null);
                        goods.setElementFour(null);
                    }
                    goodList.add(goods);
                });
            }

        }
        return goodList;

    }

    private void initSpuId(TopicResponse response){
        if(CollectionUtils.isNotEmpty(response.getStoreyList())) {
            response.getStoreyList().stream().filter(p -> CollectionUtils.isNotEmpty(p.getContents())).forEach(storey -> {
                List<TopicStoreyContentReponse> contents = storey.getContents().stream().filter(p -> Objects.equals(p.getType(), 2) && !StringUtils.isEmpty(p.getLinkUrl())).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(contents)) {
                    return;
                }
                contents.stream().forEach(content -> {
                    Map<String, String> map = getUrlParams(content.getLinkUrl());
                    if (map.isEmpty()) {
                        return;
                    }
                    if (map.containsKey("skuId")) {
                        content.setSkuId(map.get("skuId"));
                    }
                    if (map.containsKey("spuId")) {
                        content.setSpuId(map.get("spuId"));
                    }
                });
            });
        }
        if(CollectionUtils.isNotEmpty(response.getHeadImageList())){
            response.getHeadImageList().stream().filter(p->!StringUtils.isEmpty(p.getLinkUrl())).forEach(head->{
                Map<String, String> map = getUrlParams(head.getLinkUrl());
                if (map.isEmpty()) {
                    return;
                }
                if (map.containsKey("skuId")) {
                    head.setSkuId(map.get("skuId"));
                }
                if (map.containsKey("spuId")) {
                    head.setSpuId(map.get("spuId"));
                }
            });
        }

    }

    private Map<String, String> getUrlParams(String url) {
        Map<String, String> map = new HashMap<>();
        if (StringUtils.isEmpty(url)) {
            return map;
        }
        try {
            Integer index = url.indexOf("?");
            if (index == -1) {
                return map;
            }
            String paramsUrl = url.substring(index + 1, url.length());
            String[] params = paramsUrl.split("&");
            for (String param : params) {
                String[] pair = param.split("=");
                map.put(pair[0], pair[1]);
            }
        } catch (Exception e) {
            log.warn("获取url参数失败,url:{}", url, e);
        }
        return map;
    }

}
