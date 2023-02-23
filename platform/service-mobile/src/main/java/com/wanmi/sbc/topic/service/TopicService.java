package com.wanmi.sbc.topic.service;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.soybean.marketing.api.provider.activity.NormalActivityPointSkuProvider;
import com.soybean.marketing.api.resp.NormalActivitySkuResp;
import com.wanmi.sbc.booklistmodel.BookListModelAndGoodsService;
import com.wanmi.sbc.booklistmodel.response.GoodsCustomResponse;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.configure.SpringUtil;
import com.wanmi.sbc.customer.CustomerBaseController;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.request.customer.CustomerGetByIdRequest;
import com.wanmi.sbc.customer.api.response.customer.CustomerGetByIdResponse;
import com.wanmi.sbc.customer.bean.dto.CustomerDTO;
import com.wanmi.sbc.customer.bean.enums.StoreState;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticQueryProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoQueryRequest;
import com.wanmi.sbc.elastic.bean.vo.goods.EsGoodsVO;
import com.wanmi.sbc.goods.api.provider.SuspensionV2.SuspensionProvider;
import com.wanmi.sbc.goods.api.provider.goods.GoodsQueryProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.provider.pointsgoods.PointsGoodsQueryProvider;
import com.wanmi.sbc.goods.api.request.SuspensionV2.SuspensionByTypeRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoViewByIdsRequest;
import com.wanmi.sbc.goods.api.response.index.NormalModuleSkuResp;
import com.wanmi.sbc.goods.bean.dto.GoodsInfoDTO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.marketing.api.provider.plugin.MarketingPluginProvider;
import com.wanmi.sbc.marketing.api.request.plugin.MarketingPluginGoodsListFilterRequest;
import com.wanmi.sbc.setting.api.request.RankPageRequest;
import com.wanmi.sbc.setting.api.request.RankRequest;
import com.wanmi.sbc.setting.api.request.RankRequestListResponse;
import com.wanmi.sbc.setting.api.request.RankStoreyRequest;
import com.wanmi.sbc.setting.api.request.topicconfig.*;
import com.wanmi.sbc.setting.api.response.RankPageResponse;
import com.wanmi.sbc.setting.api.response.TopicStoreySearchContentRequest;
import com.wanmi.sbc.setting.bean.dto.TopicStoreyColumnDTO;
import com.wanmi.sbc.setting.bean.dto.TopicStoreyColumnGoodsDTO;
import com.wanmi.sbc.setting.api.request.topicconfig.MixedComponentQueryRequest;
import com.wanmi.sbc.setting.api.response.mixedcomponentV2.TopicStoreyMixedComponentResponse;
import com.wanmi.sbc.topic.response.NewBookPointResponse;
import com.wanmi.sbc.goods.bean.enums.AddedFlag;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import com.wanmi.sbc.home.service.HomePageService;
import com.wanmi.sbc.marketing.api.provider.coupon.CouponCacheProvider;
import com.wanmi.sbc.marketing.api.request.coupon.CouponCacheCenterPageRequest;
import com.wanmi.sbc.marketing.api.response.coupon.CouponCacheCenterPageResponse;
import com.wanmi.sbc.marketing.bean.enums.CouponSceneType;
import com.wanmi.sbc.marketing.bean.enums.ScopeType;
import com.wanmi.sbc.marketing.bean.vo.CouponVO;
import com.wanmi.sbc.setting.api.provider.topic.TopicConfigProvider;
import com.wanmi.sbc.setting.bean.dto.TopicStoreyContentDTO;
import com.wanmi.sbc.setting.bean.enums.TopicStoreyType;
import com.wanmi.sbc.setting.bean.enums.TopicStoreyTypeV2;
import com.wanmi.sbc.setting.bean.vo.TopicActivityVO;
import com.wanmi.sbc.topic.response.*;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.windows.request.ThreeGoodBookRequest;
import io.jsonwebtoken.Claims;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import springfox.documentation.spring.web.json.Json;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
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

    @Autowired
    private HomePageService homePageService;

    @Autowired
    private CustomerBaseController customerBaseController;

    @Autowired
    private CustomerQueryProvider customerQueryProvider;

    @Autowired
    private NormalActivityPointSkuProvider normalActivityPointSkuProvider;

    @Autowired
    private PointsGoodsQueryProvider pointsGoodsQueryProvider;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    @Autowired
    private MarketingPluginProvider marketingPluginProvider;

    @Autowired
    private SuspensionProvider suspensionProvider;


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


    public TopicCustomerPointsResponse getPoints(CustomerGetByIdResponse customer){
        String key="showUserIntegral.config";
        String pointsText=SpringUtil.getBean(key);
        Gson gson=new Gson();
        Map map=new HashMap();
        map=gson.fromJson(pointsText,Map.class);
        String customerId;
        try{
            customerId = commonUtil.getOperatorId();
            if(StringUtil.isNotBlank(customerId)) {
                TopicCustomerPointsResponse pointsResponse=new TopicCustomerPointsResponse();
                if(null!=map.get("text")){
                    pointsResponse.setPoints_text(map.get("text").toString());
                }
                pointsResponse.setPoints_available(customer.getPointsAvailable());
                pointsResponse.setCustomer_id(customer.getCustomerId());
                pointsResponse.setCustomer_name(StringUtil.isNotBlank(customer.getCustomerDetail().getCustomerName())?customer.getCustomerDetail().getCustomerName():customer.getCustomerDetail().getCustomerId());
                return pointsResponse;
            }
            return null;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }


    }

    public BaseResponse<TopicResponse> detailV2(TopicQueryRequest request,Boolean allLoad){

//        HttpServletRequest httpRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//        CustomerGetByIdResponse customer =new CustomerGetByIdResponse();
//        Map customerMap = ( Map ) httpRequest.getAttribute("claims");
//        if(null!=customerMap && null!=customerMap.get("customerId")) {
//            customer = customerQueryProvider.getCustomerById(new CustomerGetByIdRequest(customerMap.get("customerId").toString())).getContext();
//
//        }
        String c="{\"checkState\":\"CHECKED\",\"createTime\":\"2023-02-03T15:07:27\",\"customerAccount\":\"15618961858\",\"customerDetail\":{\"contactName\":\"书友_izw9\",\"contactPhone\":\"15618961858\",\"createTime\":\"2023-02-03T15:07:27\",\"customerDetailId\":\"2c9a00d184efa38001861619fbd60235\",\"customerId\":\"2c9a00d184efa38001861619fbd60234\",\"customerName\":\"书友_izw9\",\"customerStatus\":\"ENABLE\",\"delFlag\":\"NO\",\"employeeId\":\"2c9a00027f1f3e36017f202dfce40002\",\"isDistributor\":\"NO\",\"updatePerson\":\"2c90e863786d2a4c01786dd80bc0000a\",\"updateTime\":\"2023-02-11T11:18:23\"},\"customerId\":\"2c9a00d184efa38001861619fbd60234\",\"customerLevelId\":3,\"customerPassword\":\"a8568f6a11ca32de1429db6450278bfd\",\"customerSaltVal\":\"64f88c8c7b53457f55671acc856bf60b7ffffe79ba037b8753c005d1265444ad\",\"customerType\":\"PLATFORM\",\"delFlag\":\"NO\",\"enterpriseCheckState\":\"INIT\",\"fanDengUserNo\":\"600395394\",\"growthValue\":0,\"loginErrorCount\":0,\"loginIp\":\"192.168.56.108\",\"loginTime\":\"2023-02-17T10:37:58\",\"payErrorTime\":0,\"pointsAvailable\":0,\"pointsUsed\":0,\"safeLevel\":20,\"storeCustomerRelaListByAll\":[],\"updatePerson\":\"2c90e863786d2a4c01786dd80bc0000a\",\"updateTime\":\"2023-02-11T11:18:23\"}\n";
        CustomerGetByIdResponse customer= JSON.parseObject(c, CustomerGetByIdResponse.class);
        BaseResponse<TopicResponse> detail = this.detail(request, allLoad);
        if(null==detail||null==detail.getContext()){
            return BaseResponse.success(null);
        }
        TopicResponse response=detail.getContext();
        if(null==response.getStoreyList()||response.getStoreyList().size()==0){
            return BaseResponse.success(null);
        }
        List<TopicStoreyResponse> storeyList = response.getStoreyList();
        for(TopicStoreyResponse topicResponse:storeyList){
            Integer storeyType = topicResponse.getStoreyType();
            if(storeyType == TopicStoreyTypeV2.ROLLINGMESSAGE.getId()){//滚动消息
                topicResponse.setNotes(homePageService.notice());
            } else if(storeyType == TopicStoreyTypeV2.VOUCHER.getId()) {//抵扣券
                initCouponV2(storeyList);
            } else if(storeyType == TopicStoreyTypeV2.MIXED.getId()) { //混合组件
                //topicResponse.setTopicStoreyMixedComponentResponse(getMixedComponentContent(topicResponse.getId()));
            }else if(storeyType==TopicStoreyTypeV2.POINTS.getId()){//用户积分
                topicResponse.setPoints(this.getPoints(customer));
            }else if(storeyType==TopicStoreyTypeV2.NEWBOOK.getId()){//新书速递
                topicResponse.setNewBookPointResponseList(newBookPoint(new BaseQueryRequest(),customer));
            }else if(storeyType==TopicStoreyTypeV2.RANKLIST.getId()){//首页榜单
                List<RankRequest> rank = rank(topicResponse);
                topicResponse.setRankList(KsBeanUtil.convertList(rank,RankResponse.class));
            }else if(storeyType==TopicStoreyTypeV2.RANKDETAIL.getId()){//榜单更多
                RankPageRequest rankPage = rankPage2(topicResponse);
                topicResponse.setRankPageRequest(rankPage);
            } else if(storeyType==TopicStoreyTypeV2.THREEGOODBOOK.getId()){//三本好书
                topicResponse.setThreeGoodBookResponses(this.threeGoodBook(new ThreeGoodBookRequest()));
            }else if(storeyType==TopicStoreyTypeV2.Books.getId()){//图书组件
                TopicStoreyContentRequest topicStoreyContentRequest=new TopicStoreyContentRequest();
                topicStoreyContentRequest.setStoreyType(TopicStoreyTypeV2.Books.getId());
                topicResponse.setBooksResponses(this.bookOrGoods(topicStoreyContentRequest,customer));
            }else if(storeyType==TopicStoreyTypeV2.Goods.getId()){//商品组件
                TopicStoreyContentRequest topicStoreyContentRequest=new TopicStoreyContentRequest();
                topicStoreyContentRequest.setStoreyType(TopicStoreyTypeV2.Goods.getId());
                topicResponse.setGoodsResponses(this.bookOrGoods(topicStoreyContentRequest,customer));
            }else if(storeyType==TopicStoreyTypeV2.KeyWord.getId()){//关键字组件
                SuspensionByTypeRequest suspensionByTypeRequest=new SuspensionByTypeRequest();
                suspensionByTypeRequest.setType(2L);
                topicResponse.setSuspensionDTOList(suspensionProvider.getByType(suspensionByTypeRequest).getContext().getSuspensionDTOList());
            }
        }
        return BaseResponse.success(response);
    }

    public List<RankRequest> rank(TopicStoreyResponse topicResponse){
        RankStoreyRequest rankStoreyRequest = new RankStoreyRequest();
        rankStoreyRequest.setTopicStoreyId(topicResponse.getId());
        rankStoreyRequest.setIsRankDetail(false);
        RankRequestListResponse response = topicConfigProvider.rank(rankStoreyRequest);
        List<GoodsCustomResponse> goodsCustomResponses = initGoods(response.getIdList());
        goodsCustomResponses.forEach(g->{
            String label = g.getGoodsLabelList().get(0);
            response.getRankRequestList().forEach(r->{
                if(null!=r.getRankList()&&r.getRankList().size()>0){
                    r.getRankList().forEach(t->{
                        Map map= (Map) t;
                        if(map.get("spuId").equals(g.getGoodsId())){
                            map.put("label",label);
                            map.put("goodsInfoId",g.getGoodsInfoId());
                            map.put("subName",g.getGoodsSubName());
                            map.put("showPrice",g.getShowPrice());
                            map.put("linePrice",g.getLinePrice());
                            map.put("discount",g.getLinePrice().divide(g.getShowPrice(), RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(10)));
                            map.put("stock",g.getStock());
                            map.put("author",g.getGoodsExtProperties().getAuthor());
                            map.put("publisher",g.getGoodsExtProperties().getPublisher());
                        }
                    });
                }
            });
        });
        return response.getRankRequestList();
    }

    /**
     * 获取广告
     */
    public void getADV(){

    }

    /**
     * 商品组件及图书组件
     */
    public List<GoodsOrBookResponse> bookOrGoods(TopicStoreyContentRequest topicStoreyContentRequest,CustomerGetByIdResponse customer){

        List<GoodsOrBookResponse> goodsOrBookResponse=new ArrayList<>();
        //获得主题id
        topicStoreyContentRequest.setStoreyId(topicConfigProvider.getStoreyIdByType(topicStoreyContentRequest.getStoreyType()).get(0).getId());
        //获得主题下商品skuList
        List<TopicStoreyContentDTO> contentByStoreyId = topicConfigProvider.getContentByStoreyId(topicStoreyContentRequest);
        if(null==contentByStoreyId || contentByStoreyId.size()==0){
            return null;
        }
        List<TopicStoreyContentDTO> collectTemp = contentByStoreyId.stream().filter(t -> {
            LocalDateTime now = LocalDateTime.now();
            if (now.isBefore(t.getEndTime()) && now.isAfter(t.getStartTime())) {
                return true;
            } else {
                return false;
            }
        }).collect(Collectors.toList());

        if(null==collectTemp || collectTemp.size()==0){
            return null;
        }
        List<String> skuList = collectTemp.stream().map(t -> t.getSkuId()).collect(Collectors.toList());

        //获取商品信息
        GoodsInfoViewByIdsRequest goodsInfoByIdRequest = new GoodsInfoViewByIdsRequest();
        goodsInfoByIdRequest.setDeleteFlag(DeleteFlag.NO);
        goodsInfoByIdRequest.setGoodsInfoIds(skuList);
        goodsInfoByIdRequest.setIsHavSpecText(1);
        List<GoodsInfoVO> goodsInfos = goodsInfoQueryProvider.listViewByIds(goodsInfoByIdRequest).getContext().getGoodsInfos();

//        //获取会员价
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//        CustomerGetByIdResponse customer =new CustomerGetByIdResponse();
//        Map customerMap = ( Map ) request.getAttribute("claims");
//        if(null!=customerMap && null!=customerMap.get("customerId")) {
//            customer = customerQueryProvider.getCustomerById(new CustomerGetByIdRequest(customerMap.get("customerId").toString())).getContext();
//
//        }
        //获取会员
        MarketingPluginGoodsListFilterRequest filterRequest = new MarketingPluginGoodsListFilterRequest();
        filterRequest.setGoodsInfos(KsBeanUtil.convert(goodsInfos, GoodsInfoDTO.class));
        filterRequest.setCustomerDTO(KsBeanUtil.convert(customer, CustomerDTO.class));
        List<GoodsInfoVO> goodsInfoVOList = marketingPluginProvider.goodsListFilter(filterRequest).getContext().getGoodsInfoVOList();

        Map<String, GoodsInfoVO> salePriceMap = goodsInfoVOList.stream().collect(Collectors.toMap(GoodsInfoVO::getGoodsInfoId, Function.identity()));

        collectTemp.stream().forEach(g->{
            GoodsOrBookResponse goodsOrBookResponseTemp=new GoodsOrBookResponse();
            BeanUtils.copyProperties(g,goodsOrBookResponseTemp);
            goodsOrBookResponseTemp.setMarketPrice(salePriceMap.get(goodsOrBookResponseTemp.getSkuId()).getMarketPrice());
            goodsOrBookResponseTemp.setSalePrice(salePriceMap.get(goodsOrBookResponseTemp.getSkuId()).getSalePrice());
            goodsOrBookResponseTemp.getMarketingLabels().addAll(salePriceMap.get(goodsOrBookResponseTemp.getSkuId()).getMarketingLabels());
            goodsOrBookResponse.add(goodsOrBookResponseTemp);
        });


        return goodsOrBookResponse;
    }

    public BaseResponse<RankPageRequest> rankPage(RankStoreyRequest request){
        RankPageResponse pageResponse = topicConfigProvider.rankPage(request);
        List<String> idList = pageResponse.getIdList();
        if(CollectionUtils.isEmpty(idList)){
            return BaseResponse.success(null);
        }
        List<RankRequest> contentList = pageResponse.getPageRequest().getContentList();
        Iterator<RankRequest> iterator=contentList.iterator();
        //删除空榜单
//        while (iterator.hasNext()){
//            RankRequest rankRequest = iterator.next();
//            List<Map> mapList = (List<Map>) rankRequest.getRankList();
//            if(CollectionUtils.isNotEmpty(mapList)){
//                Iterator<Map> it1=mapList.iterator();
//                while (it1.hasNext()){
//                    Map tMap= (Map) it1.next();
//                    List<Map> rankList = (List<Map>) tMap.get("rankList");
//                    if(CollectionUtils.isEmpty(rankList)){
//                        it1.remove();
//                    }
//                }
//            }
//            if(null==rankRequest||CollectionUtils.isEmpty(mapList)){
//                iterator.remove();
//            }
//        }
        List<GoodsCustomResponse> goodsCustomResponses = initGoods(idList);
        goodsCustomResponses.forEach(g-> {
            String label = g.getGoodsLabelList().get(0);
            pageResponse.getPageRequest().getContentList().forEach(r->{
                r.getRankList().forEach(t->{
                    Map tMap= (Map) t;
                    List<Map> list=(List<Map>) tMap.get("rankList");
                    list.forEach(m->{
                            if(m.get("spuId").equals(g.getGoodsId())) {
                                m.put("label", label);
                                m.put("subName", g.getGoodsSubName());
                                m.put("goodsInfoId",g.getGoodsInfoId());
                                m.put("showPrice",g.getShowPrice());
                                m.put("linePrice",g.getLinePrice());
                                m.put("discount",g.getLinePrice().divide(g.getShowPrice(), RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(10)));
                                m.put("stock",g.getStock());
                                m.put("author",g.getGoodsExtProperties().getAuthor());
                                m.put("publisher",g.getGoodsExtProperties().getPublisher());
                            }

                    });
                });
                });
            });
        return BaseResponse.success(pageResponse.getPageRequest());
    }

    public RankPageRequest rankPage2(TopicStoreyResponse storeyResponse){
        RankStoreyRequest request=new RankStoreyRequest();
        request.setTopicStoreyId(storeyResponse.getId());
        RankPageResponse pageResponse = topicConfigProvider.rankPage2(request);
        List<String> idList = pageResponse.getIdList();
        if(CollectionUtils.isEmpty(idList)){
            return null;
        }
        List<RankRequest> contentList = pageResponse.getPageRequest().getContentList();
        List<GoodsCustomResponse> goodsCustomResponses = initGoods(idList);
        goodsCustomResponses.forEach(g-> {
            String label = g.getGoodsLabelList().get(0);
            pageResponse.getPageRequest().getContentList().forEach(r->{
                r.getRankList().forEach(t->{
                    Map tMap= (Map) t;
                    List<Map> list=(List<Map>) tMap.get("rankList");
                    list.forEach(m->{
                        if(m.get("spuId").equals(g.getGoodsId())) {
                            m.put("label", label);
                            m.put("goodsInfoId",g.getGoodsInfoId());
                            m.put("subName", g.getGoodsSubName());
                            m.put("showPrice",g.getShowPrice());
                            m.put("linePrice",g.getLinePrice());
                            m.put("discount",g.getLinePrice().divide(g.getShowPrice(), RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(10)));
                            m.put("stock",g.getStock());
                            m.put("author",g.getGoodsExtProperties().getAuthor());
                            m.put("publisher",g.getGoodsExtProperties().getPublisher());
                        }

                    });
                });
            });
        });
        return pageResponse.getPageRequest();
    }

    /**
     * 三本好书,首页加载
     */
     public List<ThreeGoodBookResponse> threeGoodBook(ThreeGoodBookRequest threeGoodBookRequest){

         List<ThreeGoodBookResponse> threeGoodBookResponses=new ArrayList<>();
         if(null == threeGoodBookRequest.getId()) {
             //没有指定栏目id,添加所有栏目
             TopicStoreyColumnQueryRequest request = new TopicStoreyColumnQueryRequest();
             request.setState(1);
             request.setPublishState(0);
             request.setTopicStoreyId(topicConfigProvider.getStoreyIdByType(TopicStoreyTypeV2.THREEGOODBOOK.getId()).get(0).getId());
             topicConfigProvider.listStoryColumn(request).getContext().getContent().stream().forEach(t -> {
                 ThreeGoodBookResponse threeGoodBookResponse = new ThreeGoodBookResponse();
                 BeanUtils.copyProperties(t, threeGoodBookResponse);
                 threeGoodBookResponses.add(threeGoodBookResponse);
             });
         }else {
             //指定了特定栏目id
             ThreeGoodBookResponse threeGoodBookResponse = new ThreeGoodBookResponse();
             threeGoodBookResponse.setId(threeGoodBookRequest.getId());
             threeGoodBookResponses.add(threeGoodBookResponse);
         }

         if(null != threeGoodBookResponses && threeGoodBookResponses.size()!=0) {
             TopicStoreyColumnGoodsQueryRequest topicStoreyColumnGoodsQueryRequest = new TopicStoreyColumnGoodsQueryRequest();
             topicStoreyColumnGoodsQueryRequest.setPublishState(0);
             topicStoreyColumnGoodsQueryRequest.setTopicStoreySearchId(threeGoodBookResponses.get(0).getId());
             //设定指定的分页
             topicStoreyColumnGoodsQueryRequest.setPageNum(threeGoodBookRequest.getPageNum());
             topicStoreyColumnGoodsQueryRequest.setPageSize(threeGoodBookRequest.getPageSize());
             List<TopicStoreyColumnGoodsDTO> content = topicConfigProvider.listStoryColumnGoods(topicStoreyColumnGoodsQueryRequest).getContext().getContent();

             if(null != content && content.size()!=0){
                 List<ThreeGoodBookGoods> goodBookGoods=new ArrayList<>();
                 content.stream().forEach(t-> {
                     ThreeGoodBookGoods goodBookGoodsTemp=new ThreeGoodBookGoods();
                     BeanUtils.copyProperties(t,goodBookGoodsTemp);
                     goodBookGoods.add(goodBookGoodsTemp);
                 });
                 threeGoodBookResponses.get(0).setGoodBookGoods(goodBookGoods);
             }
         }
         return threeGoodBookResponses;
     }



    /**
     * 商品信息及赠送积分信息
     */
    public List<NewBookPointResponse> newBookPoint(BaseQueryRequest baseQueryRequest,CustomerGetByIdResponse customer) {


        List<NewBookPointResponse> newBookPointResponseList= new ArrayList<>();


        List<NormalModuleSkuResp> context = pointsGoodsQueryProvider.getReturnPointGoods(baseQueryRequest).getContext();

        List<NormalActivitySkuResp> ponitByActivity = normalActivityPointSkuProvider.getPonitByActivity();

        Map<String, NormalActivitySkuResp> goodsPointMap = ponitByActivity.stream()
                .filter(normalActivitySkuResp -> normalActivitySkuResp.getNum() != 0)
                .collect(Collectors.toMap(NormalActivitySkuResp::getSkuId, Function.identity()));

        //获取商品积分
        List<String> skuIdList=new ArrayList<>();
        context.stream().forEach(normalModuleSkuResp -> {
            NewBookPointResponse newBookPointResponse=new NewBookPointResponse();
            BeanUtils.copyProperties(normalModuleSkuResp,newBookPointResponse);
            if(null != goodsPointMap.get(normalModuleSkuResp.getSkuId()) && null!= goodsPointMap.get(normalModuleSkuResp.getSkuId()).getNum()){
                newBookPointResponse.setNum(goodsPointMap.get(normalModuleSkuResp.getSkuId()).getNum());
            }
            skuIdList.add(newBookPointResponse.getSkuId());
            newBookPointResponseList.add(newBookPointResponse);
        });

        //获取商品信息
        GoodsInfoViewByIdsRequest goodsInfoByIdRequest = new GoodsInfoViewByIdsRequest();
        goodsInfoByIdRequest.setDeleteFlag(DeleteFlag.NO);
        goodsInfoByIdRequest.setGoodsInfoIds(skuIdList);
        goodsInfoByIdRequest.setIsHavSpecText(1);
        List<GoodsInfoVO> goodsInfos = goodsInfoQueryProvider.listViewByIds(goodsInfoByIdRequest).getContext().getGoodsInfos();

        Map<String, GoodsInfoVO> goodsPriceMap = goodsInfos
                .stream().collect(Collectors.toMap(GoodsInfoVO::getGoodsInfoId, Function.identity()));


        for(int i=0; i<newBookPointResponseList.size();i++ ){
            newBookPointResponseList.get(i).setMarketPrice(goodsPriceMap.get(newBookPointResponseList.get(i).getSkuId()).getMarketPrice());
            newBookPointResponseList.get(i).setGoodsInfoName(goodsPriceMap.get(newBookPointResponseList.get(i).getSkuId()).getGoodsInfoName());
            newBookPointResponseList.get(i).setGoodsInfoImg(goodsPriceMap.get(newBookPointResponseList.get(i).getSkuId()).getGoodsInfoImg());
        }


//        //获取会员价
//        CustomerGetByIdResponse customer=new CustomerGetByIdResponse();
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//        Map customerMap = ( Map ) request.getAttribute("claims");
//        if(null!=customerMap && null!=customerMap.get("customerId")) {
//            //获取会员
//             customer = customerQueryProvider.getCustomerById(new CustomerGetByIdRequest(customerMap.get("customerId").toString())).getContext();
//        }
            MarketingPluginGoodsListFilterRequest filterRequest = new MarketingPluginGoodsListFilterRequest();
            filterRequest.setGoodsInfos(KsBeanUtil.convert(goodsInfos, GoodsInfoDTO.class));
            filterRequest.setCustomerDTO(KsBeanUtil.convert(customer, CustomerDTO.class));
            List<GoodsInfoVO> goodsInfoVOList = marketingPluginProvider.goodsListFilter(filterRequest).getContext().getGoodsInfoVOList();
            Map<String, GoodsInfoVO> goodsVipPriceMap = goodsInfoVOList
                    .stream().collect(Collectors.toMap(GoodsInfoVO::getGoodsInfoId, Function.identity()));

            for(int i=0; i<newBookPointResponseList.size();i++ ){
                newBookPointResponseList.get(i).setSalePrice(goodsVipPriceMap.get(newBookPointResponseList.get(i).getSkuId()).getSalePrice());
                newBookPointResponseList.get(i).setGoodsInfoImg(goodsVipPriceMap.get(newBookPointResponseList.get(i).getSkuId()).getGoodsInfoImg());
            }

        return newBookPointResponseList;
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
        storeyList.stream().filter(p->p.getStoreyType()!= null && p.getStoreyType().equals(TopicStoreyType.COUPON.getId())).forEach(p->{
            if(CollectionUtils.isEmpty(p.getContents())) {
                return;
            }
            String typeAllGoodsId = null;
            Map<String, String> typeStoreCateGoodsId = new HashMap<>();
            for (TopicStoreyContentReponse c : p.getContents()) {
                Optional<TopicStoreyContentReponse.CouponInfo> optionalCouponVO = couponInfos.stream().filter(coupon->coupon.getActivityId().equals(c.getActivityId()) && coupon.getCouponId().equals(c.getCouponId())).findFirst();
                if(optionalCouponVO.isPresent()){
                    c.setCouponInfo(optionalCouponVO.get());
                    for (CouponVO couponVO : couponVOS) {
                        if(couponVO.getActivityId().equals(c.getActivityId()) && couponVO.getCouponId().equals(c.getCouponId())){
                            if (ScopeType.STORE_CATE.equals(couponVO.getScopeType())) {
                                //适用店铺分类
                                if(CollectionUtils.isNotEmpty(couponVO.getScopeIds())){
                                    if(!typeStoreCateGoodsId.containsKey(couponVO.getScopeIds().get(0))){
                                        BaseResponse<String> goodsId = goodsQueryProvider.getGoodsIdByClassify(Integer.parseInt(couponVO.getScopeIds().get(0)));
                                        typeStoreCateGoodsId.put(couponVO.getScopeIds().get(0), goodsId.getContext());
                                    }
                                    c.setSpuId(typeStoreCateGoodsId.get(couponVO.getScopeIds().get(0)));
                                }
                            }else if (ScopeType.ALL.equals(couponVO.getScopeType()) || ScopeType.BOSS_CATE.equals(couponVO.getScopeType()) || ScopeType.BRAND.equals(couponVO.getScopeType())){
                                if(typeAllGoodsId == null){
                                    BaseResponse<String> goodsId = goodsQueryProvider.getGoodsId(Collections.emptyList());
                                    typeAllGoodsId = goodsId.getContext();
                                }
                                c.setSpuId(typeAllGoodsId);
                            }else if (ScopeType.SKU.equals(couponVO.getScopeType())){
                                if(CollectionUtils.isNotEmpty(couponVO.getScopeIds())){
                                    BaseResponse<String> goodsId = goodsQueryProvider.getGoodsId(couponVO.getScopeIds());
                                    c.setSpuId(goodsId.getContext());
                                }
                            }
                        }
                    }
                }
            }
        });
   }

    /**
     * @Description 初始化优惠券信息
     * @Author zh
     * @Date  2023/2/6 10:28
     */
    private void initCouponV2(List<TopicStoreyResponse> storeyList){
        //优惠券
        List<String> activityIds = new ArrayList<>();
        List<String> couponIds = new ArrayList<>();
        storeyList.stream().filter(p->p.getStoreyType()!= null && p.getStoreyType().equals(TopicStoreyTypeV2.VOUCHER.getId())).forEach(p->{
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
        storeyList.stream().filter(p->p.getStoreyType()!= null && p.getStoreyType().equals(TopicStoreyType.COUPON.getId())).forEach(p->{
            if(CollectionUtils.isEmpty(p.getContents())) {
                return;
            }
            String typeAllGoodsId = null;
            Map<String, String> typeStoreCateGoodsId = new HashMap<>();
            for (TopicStoreyContentReponse c : p.getContents()) {
                Optional<TopicStoreyContentReponse.CouponInfo> optionalCouponVO = couponInfos.stream().filter(coupon->coupon.getActivityId().equals(c.getActivityId()) && coupon.getCouponId().equals(c.getCouponId())).findFirst();
                if(optionalCouponVO.isPresent()){
                    c.setCouponInfo(optionalCouponVO.get());
                    for (CouponVO couponVO : couponVOS) {
                        if(couponVO.getActivityId().equals(c.getActivityId()) && couponVO.getCouponId().equals(c.getCouponId())){
                            if (ScopeType.STORE_CATE.equals(couponVO.getScopeType())) {
                                //适用店铺分类
                                if(CollectionUtils.isNotEmpty(couponVO.getScopeIds())){
                                    if(!typeStoreCateGoodsId.containsKey(couponVO.getScopeIds().get(0))){
                                        BaseResponse<String> goodsId = goodsQueryProvider.getGoodsIdByClassify(Integer.parseInt(couponVO.getScopeIds().get(0)));
                                        typeStoreCateGoodsId.put(couponVO.getScopeIds().get(0), goodsId.getContext());
                                    }
                                    c.setSpuId(typeStoreCateGoodsId.get(couponVO.getScopeIds().get(0)));
                                }
                            }else if (ScopeType.ALL.equals(couponVO.getScopeType()) || ScopeType.BOSS_CATE.equals(couponVO.getScopeType()) || ScopeType.BRAND.equals(couponVO.getScopeType())){
                                if(typeAllGoodsId == null){
                                    BaseResponse<String> goodsId = goodsQueryProvider.getGoodsId(Collections.emptyList());
                                    typeAllGoodsId = goodsId.getContext();
                                }
                                c.setSpuId(typeAllGoodsId);
                            }else if (ScopeType.SKU.equals(couponVO.getScopeType())){
                                if(CollectionUtils.isNotEmpty(couponVO.getScopeIds())){
                                    BaseResponse<String> goodsId = goodsQueryProvider.getGoodsId(couponVO.getScopeIds());
                                    c.setSpuId(goodsId.getContext());
                                }
                            }
                        }
                    }
                }
            }
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
                    goods.setLabels(goodsCustom.get().getLabels());
                    goods.setActivities(goodsCustom.get().getActivities());
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
                    goods.setActivities(goodsCustom.get().getActivities());
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

//    private TopicStoreyMixedComponentResponse getMixedComponentContent(Integer id) {
//        MixedComponentQueryRequest mixedComponentQueryRequest = new MixedComponentQueryRequest();
//        mixedComponentQueryRequest.setTopicStoreyId(id);
//        TopicStoreyMixedComponentResponse mixedComponent = topicConfigProvider.mixedComponentContent(mixedComponentQueryRequest).getContext();
//        return mixedComponent;
//    }

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
