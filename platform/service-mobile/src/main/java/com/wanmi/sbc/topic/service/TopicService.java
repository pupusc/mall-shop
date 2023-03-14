package com.wanmi.sbc.topic.service;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.gson.Gson;
import com.soybean.elastic.api.provider.spu.EsSpuNewProvider;
import com.soybean.elastic.api.req.EsKeyWordSpuNewQueryProviderReq;
import com.soybean.elastic.api.resp.EsSpuNewResp;
import com.soybean.marketing.api.provider.activity.NormalActivityPointSkuProvider;
import com.wanmi.sbc.booklistmodel.BookListModelAndGoodsService;
import com.wanmi.sbc.booklistmodel.response.GoodsCustomResponse;
import com.wanmi.sbc.bookmeta.bo.MetaBookRcmmdFigureBO;
import com.wanmi.sbc.bookmeta.provider.MetaLabelProvider;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.configure.SpringUtil;
import com.wanmi.sbc.customer.CustomerBaseController;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.request.customer.CustomerGetByIdRequest;
import com.wanmi.sbc.customer.api.response.customer.CustomerGetByIdResponse;
import com.wanmi.sbc.customer.api.response.customer.CustomerPointsAvailableByCustomerIdResponse;
import com.wanmi.sbc.customer.bean.dto.CustomerDTO;
import com.wanmi.sbc.customer.bean.enums.StoreState;
import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticQueryProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoQueryRequest;
import com.wanmi.sbc.elastic.bean.vo.goods.EsGoodsVO;
import com.wanmi.sbc.goods.api.provider.SuspensionV2.SuspensionProvider;
import com.wanmi.sbc.goods.api.provider.booklistmodel.BookListModelProvider;
import com.wanmi.sbc.goods.api.provider.goods.GoodsQueryProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.provider.pointsgoods.PointsGoodsQueryProvider;
import com.wanmi.sbc.goods.api.request.SuspensionV2.SuspensionByTypeRequest;
import com.wanmi.sbc.goods.api.request.info.DistributionGoodsChangeRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoViewByIdsRequest;
import com.wanmi.sbc.goods.api.response.goods.GoodsInfosRedisResponse;
import com.wanmi.sbc.goods.api.response.goods.NewBookPointRedisResponse;
import com.wanmi.sbc.goods.bean.dto.GoodsInfoDTO;
import com.wanmi.sbc.goods.bean.dto.MarketingLabelNewDTO;
import com.wanmi.sbc.goods.bean.dto.SuspensionDTO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goodsPool.PoolFactory;
import com.wanmi.sbc.goodsPool.service.PoolService;
import com.wanmi.sbc.index.RefreshConfig;
import com.wanmi.sbc.index.V2tabConfigResponse;
import com.wanmi.sbc.marketing.api.provider.plugin.MarketingPluginProvider;
import com.wanmi.sbc.marketing.api.request.plugin.MarketingPluginGoodsListFilterRequest;
import com.wanmi.sbc.marketing.api.response.info.GoodsInfoListByGoodsInfoResponse;
import com.wanmi.sbc.order.api.provider.stockAppointment.StockAppointmentProvider;
import com.wanmi.sbc.order.api.request.stockAppointment.AppointmentRequest;
import com.wanmi.sbc.order.api.request.stockAppointment.StockAppointmentRequest;
import com.wanmi.sbc.order.request.AppointmentStockRequest;
import com.wanmi.sbc.redis.RedisListService;
import com.wanmi.sbc.redis.RedisService;
import com.wanmi.sbc.setting.api.request.*;
import com.wanmi.sbc.setting.api.request.topicconfig.*;
import com.wanmi.sbc.setting.bean.dto.*;
import com.wanmi.sbc.setting.bean.enums.MixedComponentLevel;
import com.wanmi.sbc.task.MixedComponentContentJobHandler;
import com.wanmi.sbc.task.NewBookPointJobHandler;
import com.wanmi.sbc.task.NewRankJobHandler;
import com.wanmi.sbc.task.RankPageJobHandler;
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
import com.wanmi.sbc.setting.bean.enums.TopicStoreyType;
import com.wanmi.sbc.setting.bean.enums.TopicStoreyTypeV2;
import com.wanmi.sbc.setting.bean.vo.TopicActivityVO;
import com.wanmi.sbc.topic.response.*;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.DitaUtil;
import com.wanmi.sbc.util.RedisKeyUtil;
import com.wanmi.sbc.util.ThreadLocalUtil;
import com.wanmi.sbc.windows.request.ThreeGoodBookRequest;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TopicService {

    private static String GOODS_PRICE = "GOODS_PRICE";

    @Autowired
    private TopicConfigProvider topicConfigProvider;

    @Autowired
    private EsGoodsInfoElasticQueryProvider esGoodsInfoElasticQueryProvider;

    @Autowired
    private EsSpuNewProvider esSpuNewProvider;

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

    @Autowired
    private MetaLabelProvider metaLabelProvider;

    @Autowired
    private BookListModelProvider bookListModelProvider;

    @Autowired
    private StockAppointmentProvider stockAppointmentProvider;

    @Autowired
    private RedisService redisService;

    @Autowired
    private RedisListService redisListService;

    @Autowired
    private PoolFactory poolFactory;

    @Autowired
    private RefreshConfig refreshConfig;

    @Autowired
    private TopicConfigProvider columnRepository;

    public BaseResponse<TopicResponse> detail(TopicQueryRequest request,Boolean allLoad){
        BaseResponse<TopicActivityVO> activityVO =  topicConfigProvider.detail(request);
        if(activityVO == null || activityVO.getContext() ==null){
            return BaseResponse.success(null);
        }
        TopicResponse response = KsBeanUtil.convert(activityVO.getContext(),TopicResponse.class);
        if(!allLoad){
            response.setStoreyList(response.getStoreyList().subList(0,4));
        }
        //如果配置有一行两个商品的配置信息，查询商品
        List<String> skuIds = new ArrayList<>();
        //图片+链接解析spuId
        initSpuId(response);
        if(CollectionUtils.isEmpty(activityVO.getContext().getStoreyList())){
            return BaseResponse.success(response);
        }
        //轮播图要加载
//        for (int i= 0 ;i<response.getStoreyList().size();i++){
//            if(!response.getStoreyList().get(i).getStoreyType().equals(TopicStoreyType.HETERSCROLLIMAGE.getId())){
//                response.getStoreyList().get(i).setContents(null);
//            }
//        }
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
                CustomerPointsAvailableByCustomerIdResponse context = customerBaseController.getPointsAvailable().getContext();
                pointsResponse.setPoints_available(context.getPointsAvailable());
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
    @Autowired
    private NewRankJobHandler rankJobHandler;

    @Autowired
    private NewBookPointJobHandler newBookPointJobHandler;

    @Autowired
    private RankPageJobHandler rankPageJobHandler;

    @Autowired
    private MixedComponentContentJobHandler mixedComponentContentJobHandler;

    public void refresRedis() throws Exception {

        List<V2tabConfigResponse> list = JSONArray.parseArray(refreshConfig.getV2tabConfig(), V2tabConfigResponse.class);
        if(list != null && list.size() > 0){
            V2tabConfigResponse response = list.get(0);
            String topicKey = response.getParamsId();

            TopicQueryRequest request = new TopicQueryRequest();
            request.setTopicKey(topicKey);

            BaseResponse<TopicActivityVO> activityVO =  topicConfigProvider.detail(request);
            List<TopicStoreyDTO> tpList = activityVO.getContext().getStoreyList();

            for(int i=0;i<tpList.size();i++){
                TopicStoreyDTO storeyDTO = tpList.get(i);

                int topic_store_id = storeyDTO.getId();
                String name = storeyDTO.getName();
                int storeyType = storeyDTO.getStoreyType();

                if(storeyType == TopicStoreyTypeV2.NEWBOOK.getId()){                //13.新书速递
                    //writeRedis(topic_store_id);
                    newBookPointJobHandler.execute(null);
                }else if(storeyType == TopicStoreyTypeV2.RANKLIST.getId()){         //11.榜单组件
                    //writeRedis(topic_store_id);
                    rankJobHandler.execute("H5");
                }else if(storeyType==TopicStoreyTypeV2.RANKDETAIL.getId()){         //21.榜单更多
                    rankPageJobHandler.execute(topicKey);
                }else if(storeyType==TopicStoreyTypeV2.MIXED.getId()){              //20.混合组件
                    mixedComponentContentJobHandler.execute(null);
                }else if(storeyType==TopicStoreyTypeV2.THREEGOODBOOK.getId()){      //14.三本好书
                    threeBookSaveRedis(topic_store_id);
                }else if(storeyType==TopicStoreyTypeV2.Goods.getId()){              //19.商品组件
                   goodsOrBookSaveRedis(topic_store_id);
                }else if(storeyType==TopicStoreyTypeV2.Books.getId()){              //18.图书组件
                    goodsOrBookSaveRedis(topic_store_id);
                }

            }

        }


    }

    /**
     * 新版首页入口
     * @param request
     * @param allLoad
     * @return
     */
    public BaseResponse<TopicResponse> detailV2(TopicQueryRequest request,Boolean allLoad){
        CustomerGetByIdResponse customer = getCustomer();
        //调用老版首页入口加载老版组件
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

            System.out.println("storeyType:" + storeyType + "~ begin:" + DitaUtil.getCurrentAllDate());

            if(storeyType == TopicStoreyTypeV2.ROLLINGMESSAGE.getId()){//滚动消息
                topicResponse.setNotes(homePageService.notice());
            } else if(storeyType == TopicStoreyTypeV2.VOUCHER.getId()) {//抵扣券
                initCouponV2(storeyList);
            } else if(storeyType == TopicStoreyTypeV2.MIXED.getId()) { //混合组件   价格
                topicResponse.setMixedComponentContent(getMixedComponentContent(request.getTabId(), request.getKeyWord(), customer, request.getPageNum(), request.getPageSize()));
            }else if(storeyType==TopicStoreyTypeV2.POINTS.getId()){//用户积分
                topicResponse.setPoints(this.getPoints(customer));
            }else if(storeyType==TopicStoreyTypeV2.NEWBOOK.getId()){//新书速递  价格
                topicResponse.setNewBookPointResponseList(newBookPoint(new BaseQueryRequest(),customer));
            }else if(storeyType==TopicStoreyTypeV2.RANKLIST.getId()){//首页榜单
                List<RankRequest> rank = rank();
                topicResponse.setRankList(KsBeanUtil.convertList(rank,RankResponse.class));
            }else if(storeyType==TopicStoreyTypeV2.RANKDETAIL.getId()){//榜单更多
                RankPageRequest rankPage = rankPage(topicResponse);
                topicResponse.setRankPageRequest(rankPage);
            } else if(storeyType==TopicStoreyTypeV2.THREEGOODBOOK.getId()){//三本好书
                TopicStoreyContentRequest topicStoreyContentRequest=new TopicStoreyContentRequest();
                topicStoreyContentRequest.setStoreyId(topicResponse.getId());
                topicResponse.setThreeGoodBookResponses(getThreeBookSaveByRedis(topicStoreyContentRequest));
            }else if(storeyType==TopicStoreyTypeV2.Books.getId()){//图书组件 价格
                TopicStoreyContentRequest topicStoreyContentRequest=new TopicStoreyContentRequest();
                topicStoreyContentRequest.setStoreyId(topicResponse.getId());
                topicResponse.setBooksResponses(getGoodsOrBookSaveByRedis(topicStoreyContentRequest,customer));
            }else if(storeyType==TopicStoreyTypeV2.Goods.getId()){//商品组件 价格
                TopicStoreyContentRequest topicStoreyContentRequest=new TopicStoreyContentRequest();
                topicStoreyContentRequest.setStoreyId(topicResponse.getId());
                topicResponse.setGoodsResponses(getGoodsOrBookSaveByRedis(topicStoreyContentRequest,customer));
            }else if(storeyType==TopicStoreyTypeV2.KeyWord.getId()){//关键字组件
                //SuspensionByTypeRequest suspensionByTypeRequest=new SuspensionByTypeRequest();
                //suspensionByTypeRequest.setType(2L);
                //topicResponse.setSuspensionDTOList(suspensionProvider.getByType(suspensionByTypeRequest).getContext().getSuspensionDTOList());
                List<SuspensionDTO> list = this.getSearchKey();
                topicResponse.setSuspensionDTOList(list);
            }

            System.out.println("storeyType:" + storeyType + "~  end:" + DitaUtil.getCurrentAllDate());
        }
        //清理本地缓存
        ThreadLocalUtil.remove(GOODS_PRICE);
        ThreadLocalUtil.remove();
        return BaseResponse.success(response);
    }

    public CustomerGetByIdResponse getCustomer(){
        HttpServletRequest httpRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        CustomerGetByIdResponse customer =new CustomerGetByIdResponse();
        Map customerMap = ( Map ) httpRequest.getAttribute("claims");
        if(null!=customerMap && null!=customerMap.get("customerId")) {
            customer = customerQueryProvider.getCustomerById(new CustomerGetByIdRequest(customerMap.get("customerId").toString())).getContext();
        }
        return customer;
    }

    private List<SuspensionDTO> getSearchKey() {
        LocalDateTime now = LocalDateTime.now();
        List<SuspensionDTO> list =new ArrayList<>();
        JSONArray.parseArray(refreshConfig.getV2searchKey(), SuspensionDTO.class).forEach(s->{
            if(null!=s.getStartTime()&&null!=s.getEndTime()){
                if((now.isAfter(s.getStartTime())||now.isEqual(s.getStartTime()))&&(now.isBefore(s.getEndTime())||now.isEqual(s.getEndTime()))){
                    list.add(s);
                }
            }else {
                list.add(s);
            }
        });
        return list;
    }

    /**
     * 首页榜单
     * @param
     * @return
     */
    public List<RankRequest> rank(){
        try {
            RankRedisListResponse response = JSON.parseObject(redisService.getString(RedisKeyUtil.HOME_RANK), RankRedisListResponse.class);
//        List<Integer> idList = response.getRankIds();
//        GoodsIdsByRankListIdsRequest idsRequest=new GoodsIdsByRankListIdsRequest();
//        idsRequest.setIds(idList);
//        List<String> goods=new ArrayList<>();
//        List<RankGoodsPublishResponse> baseResponse = bookListModelProvider.listBookListGoodsPublishByIds(idsRequest).getContext();
//        if(CollectionUtils.isEmpty(baseResponse)){
//            return null;
//        }
//        //初始化榜单商品树形结构
//        idList.forEach(id->{
//            List<String> goodIds=new ArrayList<>();
//            List<Map> maps=new ArrayList<>();
//            baseResponse.stream().filter(item->item.getBookListId().equals(id)&&goodIds.size()<3).forEach(item->{
//                goodIds.add(item.getSkuId());
//                Map map=new HashMap();
//                map.put("spuId",item.getSpuId());
//                maps.add(map);
//            });
//            goods.addAll(goodIds);
//            response.getRankRequestList().stream().filter(r->r.getId().equals(id)).forEach(r->r.setRankList(maps));
//        });
//        //初始化榜单商品
//        List<GoodsCustomResponse> goodsCustomResponses = initGoods(goods);
//        goodsCustomResponses.forEach(g->{
////            String label = g.getGoodsLabelList().get(0);
//            response.getRankRequestList().forEach(r->{
//                if(null!=r.getRankList()&&r.getRankList().size()>0){
//                    r.getRankList().forEach(t->{
//                        Map map= (Map) t;
//                        if(map.get("spuId").equals(g.getGoodsId())){
//                            map.put("label","");
//                            map.put("goodsInfoId",g.getGoodsInfoId());
//                            map.put("subName",g.getGoodsSubName());
//                            map.put("showPrice",g.getShowPrice());
//                            map.put("linePrice",g.getLinePrice());
//                            map.put("discount",g.getLinePrice().divide(g.getShowPrice(), RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(10)));
//                            map.put("stock",g.getStock());
//                            map.put("author",g.getGoodsExtProperties().getAuthor());
//                            map.put("publisher",g.getGoodsExtProperties().getPublisher());
//                        }
//                    });
//                }
//            });
//        });
            return response.getRankRequestList();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 商品组件及图书组件
     */
    @Deprecated
    public List<GoodsOrBookResponse> bookOrGoods(TopicStoreyContentRequest topicStoreyContentRequest,CustomerGetByIdResponse customer){

        try {
            List<GoodsOrBookResponse> goodsOrBookResponse = new ArrayList<>();
            //获得主题id
            topicStoreyContentRequest.setStoreyId(topicConfigProvider.getStoreyIdByType(topicStoreyContentRequest.getStoreyType()).get(0).getId());
            //获得主题下商品skuList
            List<TopicStoreyContentDTO> collectTemp = topicConfigProvider.getContentByStoreyId(topicStoreyContentRequest);
//        if(null==contentByStoreyId || contentByStoreyId.size()==0){
//            return null;
//        }
//        List<TopicStoreyContentDTO> collectTemp = contentByStoreyId.stream().filter(t -> {
//            LocalDateTime now = LocalDateTime.now();
//            if (now.isBefore(t.getEndTime()) && now.isAfter(t.getStartTime())) {
//                return true;
//            } else {
//                return false;
//            }
//        }).collect(Collectors.toList());

            if (null == collectTemp || collectTemp.size() == 0) {
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

            collectTemp.stream().forEach(g -> {
                if (null != salePriceMap.get(g.getSkuId())) {
                    GoodsOrBookResponse goodsOrBookResponseTemp = new GoodsOrBookResponse();
                    BeanUtils.copyProperties(g, goodsOrBookResponseTemp);
                    goodsOrBookResponseTemp.setMarketPrice(salePriceMap.get(goodsOrBookResponseTemp.getSkuId()).getMarketPrice());
                    goodsOrBookResponseTemp.setSalePrice(salePriceMap.get(goodsOrBookResponseTemp.getSkuId()).getSalePrice());
                    goodsOrBookResponseTemp.getMarketingLabels().addAll(salePriceMap.get(goodsOrBookResponseTemp.getSkuId()).getMarketingLabels());

                    com.wanmi.sbc.goods.bean.dto.TagsDto tagsDto = goodsInfoQueryProvider.getTabsBySpu(goodsOrBookResponseTemp.getSpuId()).getContext();
                    if(null!=tagsDto.getTags() &&tagsDto.getTags().size()!=0 ) {
                        goodsOrBookResponseTemp.setTagsDto(tagsDto);
                    }
                    goodsOrBookResponse.add(goodsOrBookResponseTemp);
                }
            });
            return goodsOrBookResponse;
        }catch (Exception e){
            return null;
        }
    }

//    public BaseResponse<RankPageRequest> rankPage(RankStoreyRequest request){
//        RankPageResponse pageResponse = topicConfigProvider.rankPage(request);
//        List<String> idList = pageResponse.getIdList();
//        if(CollectionUtils.isEmpty(idList)){
//            return BaseResponse.success(null);
//        }
//        List<RankRequest> contentList = pageResponse.getPageRequest().getContentList();
//        Iterator<RankRequest> iterator=contentList.iterator();
//        List<GoodsCustomResponse> goodsCustomResponses = initGoods(idList);
//        goodsCustomResponses.forEach(g-> {
//            String label = g.getGoodsLabelList().get(0);
//            pageResponse.getPageRequest().getContentList().forEach(r->{
//                r.getRankList().forEach(t->{
//                    Map tMap= (Map) t;
//                    List<Map> list=(List<Map>) tMap.get("rankList");
//                    list.forEach(m->{
//                            if(m.get("spuId").equals(g.getGoodsId())) {
//                                m.put("label", label);
//                                m.put("subName", g.getGoodsSubName());
//                                m.put("goodsInfoId",g.getGoodsInfoId());
//                                m.put("showPrice",g.getShowPrice());
//                                m.put("linePrice",g.getLinePrice());
//                                m.put("discount",g.getLinePrice().divide(g.getShowPrice(), RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(10)));
//                                m.put("stock",g.getStock());
//                                m.put("author",g.getGoodsExtProperties().getAuthor());
//                                m.put("publisher",g.getGoodsExtProperties().getPublisher());
//                            }
//
//                    });
//                });
//                });
//            });
//        return BaseResponse.success(pageResponse.getPageRequest());
//    }

    /**
     * 榜单聚合页
     *
     * @param request
     * @return
     */
    public RankPageRequest rankPageByBookList(RankStoreyRequest request){
        try {
            List<RankRequest> rankRequestList = new ArrayList<>();
            List<JSONObject> objectList = redisListService.findAll(RedisKeyUtil.RANK_PAGE + request.getTopicStoreyId() + ":table");
            if (!CollectionUtils.isEmpty(objectList)) {
                for (JSONObject goodStr : objectList) {
                    rankRequestList.add(JSONObject.toJavaObject(goodStr, RankRequest.class));
                }
            }
            List<GoodsInfoVO> goodsInfoVOList = new ArrayList<>();
            List<JSONObject> infoObjectList = redisListService.findAll(RedisKeyUtil.RANK_PAGE+request.getTopicStoreyId()+":goodsInfoList");
            if (!CollectionUtils.isEmpty(infoObjectList)) {
                for (JSONObject goodStr : infoObjectList) {
                    goodsInfoVOList.add(JSONObject.toJavaObject(goodStr, GoodsInfoVO.class));
                }
            }
            if (null == request.getTopicStoreySearchId() && null == request.getRankId()) {
                request.setTopicStoreySearchId(rankRequestList.get(0).getId());
                Collection rankList = rankRequestList.get(0).getRankList();
                RankRequest convert = KsBeanUtil.convert(rankList.stream().findFirst(), RankRequest.class);
                request.setRankId(convert.getId());
            } else if (null != request.getTopicStoreySearchId() && null == request.getRankId()) {
                Optional<RankRequest> first = rankRequestList.stream().filter(r -> r.getId().equals(request.getTopicStoreySearchId())).findFirst();
                if (first.isPresent()) {
                    Collection rankList = first.get().getRankList();
                    RankRequest convert = KsBeanUtil.convert(rankList.stream().findFirst(), RankRequest.class);
                    request.setRankId(convert.getId());
                }
            }
            String key = RedisKeyUtil.RANK_PAGE + request.getTopicStoreyId() + ":listGoods:" + request.getRankId();
            //瀑布流分页
            Integer total = redisListService.getSize(key);
            Integer pageSize = request.getPageSize();
            long totalPages = (long) Math.ceil(total / pageSize);
            Integer pageNum = request.getPageNum();
            Integer start = (pageNum) * pageSize;
            Integer end = start + pageSize - 1;
            if (start >= total) {
                return null;
            }
            if (end > total) {
                end = total - 1;
            }
            List<Map> goodsCustomResponses = new ArrayList<>();
            List<JSONObject> goodsInfoList = redisListService.findByRange(key, start, end);
            if (!CollectionUtils.isEmpty(goodsInfoList)) {
                for (JSONObject goodStr : goodsInfoList) {
                    goodsCustomResponses.add(JSONObject.toJavaObject(goodStr, Map.class));
                }
            }
            RankPageRequest pageRequest = new RankPageRequest();
            //初始化榜单树形结构，获取商品详情
            rankRequestList.forEach(r -> {
                r.getRankList().forEach(t -> {
                    Map tMap = (Map) t;
                    if (tMap.get("id").equals(request.getRankId())) {
                        pageRequest.setTopicStoreySearchId(r.getId());
                        List<Map> rankList = (List<Map>) tMap.get("rankList");
                        rankList.addAll(goodsCustomResponses);
                    }
                });
            });
            pageRequest.setContentList(rankRequestList);
            pageRequest.setPageNum(pageNum);
            pageRequest.setTotalPages(totalPages);
            pageRequest.setPageSize(pageSize);
            pageRequest.setTotal(Long.valueOf(total));
            return pageRequest;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 榜单更多
     *
     * @param storeyResponse
     * @return
     */
    public RankPageRequest rankPage(TopicStoreyResponse storeyResponse){
        RankStoreyRequest request=new RankStoreyRequest();
        request.setTopicStoreyId(storeyResponse.getId());
        request.setPageNum(0);
        request.setPageSize(10);
        //首页跳转榜单聚合页
        RankPageRequest rankPageRequest = rankPageByBookList(request);
        return rankPageRequest;
    }

    /**
     * 三本好书,首页加载
     */
    @Deprecated
     public List<ThreeGoodBookResponse> threeGoodBook(ThreeGoodBookRequest threeGoodBookRequest){

         try {
             List<ThreeGoodBookResponse> threeGoodBookResponses = new ArrayList<>();
             if (null == threeGoodBookRequest.getId()) {
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
             } else {
                 //指定了特定栏目id
                 ThreeGoodBookResponse threeGoodBookResponse = new ThreeGoodBookResponse();
                 threeGoodBookResponse.setId(threeGoodBookRequest.getId());
                 threeGoodBookResponses.add(threeGoodBookResponse);
             }

             if (null != threeGoodBookResponses && threeGoodBookResponses.size() != 0) {
                 TopicStoreyColumnGoodsQueryRequest topicStoreyColumnGoodsQueryRequest = new TopicStoreyColumnGoodsQueryRequest();
                 topicStoreyColumnGoodsQueryRequest.setPublishState(0);
                 topicStoreyColumnGoodsQueryRequest.setTopicStoreySearchId(threeGoodBookResponses.get(0).getId());
                 //设定指定的分页
                 topicStoreyColumnGoodsQueryRequest.setPageNum(threeGoodBookRequest.getPageNum());
                 topicStoreyColumnGoodsQueryRequest.setPageSize(threeGoodBookRequest.getPageSize());
                 List<TopicStoreyColumnGoodsDTO> content = topicConfigProvider.listStoryColumnGoods(topicStoreyColumnGoodsQueryRequest).getContext().getContent();

                 if (null != content && content.size() != 0) {
                     List<ThreeGoodBookGoods> goodBookGoods = new ArrayList<>();
                     content.stream().forEach(t -> {
                         ThreeGoodBookGoods goodBookGoodsTemp = new ThreeGoodBookGoods();
                         BeanUtils.copyProperties(t, goodBookGoodsTemp);
                         goodBookGoods.add(goodBookGoodsTemp);
                     });
                     threeGoodBookResponses.get(0).setGoodBookGoods(goodBookGoods);
                 }
             }
             return threeGoodBookResponses;
         }catch (Exception e){
             return null;
         }
     }



    /**
     * 商品信息及赠送积分信息
     */
    public List<NewBookPointResponse> newBookPoint(BaseQueryRequest baseQueryRequest,CustomerGetByIdResponse customer) {

        try {
            String string = redisService.getString(RedisKeyUtil.NEW_BOOK_POINT);
            GoodsInfosRedisResponse response = JSON.parseObject(string, GoodsInfosRedisResponse.class);
            List<NewBookPointRedisResponse> pointResponseList = response.getNewBookPointResponseList();
            List<NewBookPointResponse> newBookPointResponseList = KsBeanUtil.convertList(pointResponseList, NewBookPointResponse.class);
            List<GoodsInfoVO> goodsInfos = response.getGoodsInfoVOList();

            Map<String, GoodsInfoVO> goodsPriceMap = goodsInfos
                    .stream().collect(Collectors.toMap(GoodsInfoVO::getGoodsInfoId, Function.identity()));


            for (int i = 0; i < newBookPointResponseList.size(); i++) {
                if(null==goodsPriceMap.get(newBookPointResponseList.get(i).getSkuId())){
                    continue;
                }
                newBookPointResponseList.get(i).setMarketPrice(goodsPriceMap.get(newBookPointResponseList.get(i).getSkuId()).getMarketPrice());
                newBookPointResponseList.get(i).setGoodsInfoName(goodsPriceMap.get(newBookPointResponseList.get(i).getSkuId()).getGoodsInfoName());
                newBookPointResponseList.get(i).setGoodsInfoImg(goodsPriceMap.get(newBookPointResponseList.get(i).getSkuId()).getGoodsInfoImg());

                String labelJson = redisService.getString("ELASTIC_SAVE:GOODS_MARKING_SKU_ID" + ":" + newBookPointResponseList.get(i).getSkuId());
                if(null!=labelJson) {
                    Map labelMap = JSONObject.parseObject(labelJson, Map.class);
                    newBookPointResponseList.get(i).setLabelMap(labelMap);

                    if(null!=labelMap.get("fix_price")) {
                        newBookPointResponseList.get(i).setFixPrice(new BigDecimal(labelMap.get("fix_price").toString()));
                    }
                }

            }


//        //获取会员价
//        CustomerGetByIdResponse customer=new CustomerGetByIdResponse();
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//        Map customerMap = ( Map ) request.getAttribute("claims");
//        if(null!=customerMap && null!=customerMap.get("customerId")) {
//            //获取会员
//             customer = customerQueryProvider.getCustomerById(new CustomerGetByIdRequest(customerMap.get("customerId").toString())).getContext();
//        }
            /*MarketingPluginGoodsListFilterRequest filterRequest = new MarketingPluginGoodsListFilterRequest();
            filterRequest.setGoodsInfos(KsBeanUtil.convert(goodsInfos, GoodsInfoDTO.class));
            filterRequest.setCustomerDTO(KsBeanUtil.convert(customer, CustomerDTO.class));
            List<GoodsInfoVO> goodsInfoVOList = marketingPluginProvider.goodsListFilter(filterRequest).getContext().getGoodsInfoVOList();*/

            List<GoodsInfoVO> goodsInfoVOList = this.initGoodsPrice(customer).getGoodsInfoVOList();

            Map<String, GoodsInfoVO> goodsVipPriceMap = goodsInfoVOList
                    .stream().collect(Collectors.toMap(GoodsInfoVO::getGoodsInfoId, Function.identity()));

            for (int i = 0; i < newBookPointResponseList.size(); i++) {
                if(null==goodsVipPriceMap.get(newBookPointResponseList.get(i).getSkuId())){
                    continue;
                }
                newBookPointResponseList.get(i).setSalePrice(goodsVipPriceMap.get(newBookPointResponseList.get(i).getSkuId()).getSalePrice());
                newBookPointResponseList.get(i).setGoodsInfoImg(goodsVipPriceMap.get(newBookPointResponseList.get(i).getSkuId()).getGoodsInfoImg());
            }

            return newBookPointResponseList;
        }catch (Exception e){
            return null;
        }
    }

    /**
     * 三本好书存redis
     */
    public BaseResponse threeBookSaveRedis(Integer topicStoreyId){
        ThreeGoodBookRequest threeGoodBookRequest=new ThreeGoodBookRequest();
        List<ThreeGoodBookResponse> threeGoodBookResponses = new ArrayList<>();
        try {
            if (null == threeGoodBookRequest.getId()) {
                //没有指定栏目id,添加所有栏目
                TopicStoreyColumnQueryRequest request=new TopicStoreyColumnQueryRequest();
                request.setState(1);
                request.setPublishState(0);
                //  request.setTopicStoreyId(topicConfigProvider.getStoreyIdByType(TopicStoreyTypeV2.THREEGOODBOOK.getId()).get(0).getId());
                request.setTopicStoreyId(topicStoreyId);
                topicConfigProvider.listStoryColumn(request).getContext().getContent().stream().forEach(t -> {
                    ThreeGoodBookResponse threeGoodBookResponse = new ThreeGoodBookResponse();
                    BeanUtils.copyProperties(t, threeGoodBookResponse);
                    threeGoodBookResponses.add(threeGoodBookResponse);
                });
            }

            if (null != threeGoodBookResponses && threeGoodBookResponses.size() != 0) {
                TopicStoreyColumnGoodsQueryRequest topicStoreyColumnGoodsQueryRequest = new TopicStoreyColumnGoodsQueryRequest();
                topicStoreyColumnGoodsQueryRequest.setPublishState(0);
                for (ThreeGoodBookResponse threeGoodBookResponse : threeGoodBookResponses) {
                    topicStoreyColumnGoodsQueryRequest.setTopicStoreySearchId(threeGoodBookResponse.getId());
                    //设定指定的分页
                    topicStoreyColumnGoodsQueryRequest.setPageNum(threeGoodBookRequest.getPageNum());
                    topicStoreyColumnGoodsQueryRequest.setPageSize(threeGoodBookRequest.getPageSize());
                    MicroServicePage<TopicStoreyColumnGoodsDTO> contextPage = topicConfigProvider.listStoryColumnGoods(topicStoreyColumnGoodsQueryRequest).getContext();
                    List<TopicStoreyColumnGoodsDTO> content = contextPage.getContent();
                    if (null != content && content.size() != 0) {
                        List<ThreeGoodBookGoods> goodBookGoods = new ArrayList<>();
                        content.stream().forEach(t -> {
                            ThreeGoodBookGoods goodBookGoodsTemp = new ThreeGoodBookGoods();
                            BeanUtils.copyProperties(t, goodBookGoodsTemp);
                            goodBookGoods.add(goodBookGoodsTemp);
                        });
                        threeGoodBookResponse.setGoodBookGoods(goodBookGoods);
                    }
                }
            }
        }catch (Exception e){
            return BaseResponse.error("失败");
        }
        String json = JSON.toJSONString(threeGoodBookResponses, SerializerFeature.WriteMapNullValue);
        String old_json = redisService.getString("ELASTIC_SAVE:HOMEPAGE" + ":" + topicStoreyId.toString());
        if(!json.equals(old_json)){
            redisService.setString("ELASTIC_SAVE:HOMEPAGE" + ":" + topicStoreyId.toString(), json );
        }
        return BaseResponse.SUCCESSFUL();
    }
    /**
     * 三本好书取redis
     */
    public List getThreeBookSaveByRedis(TopicStoreyContentRequest topicStoreyContentRequest ){
        String old_json = redisService.getString("ELASTIC_SAVE:HOMEPAGE" + ":" + topicStoreyContentRequest.getStoreyId().toString());
        List list=JSONObject.parseObject(old_json,List.class);
        return list;
    }

    /**
     * 商品组件或图书组件存redis
     */
    public BaseResponse goodsOrBookSaveRedis( Integer topicStoreyId){
        List<GoodsOrBookResponse> goodsOrBookResponse = new ArrayList<>();
        try {
            TopicStoreyContentRequest topicStoreyContentRequest=new TopicStoreyContentRequest();
            //获得主题id
            topicStoreyContentRequest.setStoreyId(topicStoreyId);
            //获得主题下商品skuList
            List<TopicStoreyContentDTO> collectTemp = topicConfigProvider.getContentByStoreyId(topicStoreyContentRequest);

            if (null == collectTemp || collectTemp.size() == 0) {
                return BaseResponse.error("没有可存储的数据");
            }
            List<String> skuList = collectTemp.stream().map(t -> t.getSkuId()).collect(Collectors.toList());

            //获取商品信息
            GoodsInfoViewByIdsRequest goodsInfoByIdRequest = new GoodsInfoViewByIdsRequest();
            goodsInfoByIdRequest.setDeleteFlag(DeleteFlag.NO);
            goodsInfoByIdRequest.setGoodsInfoIds(skuList);
            goodsInfoByIdRequest.setIsHavSpecText(1);

            collectTemp.stream().forEach(g -> {
                GoodsOrBookResponse goodsOrBookResponseTemp = new GoodsOrBookResponse();
                BeanUtils.copyProperties(g, goodsOrBookResponseTemp);
//                com.wanmi.sbc.goods.bean.dto.TagsDto tagsDto = goodsInfoQueryProvider.getTabsBySpu(goodsOrBookResponseTemp.getSpuId()).getContext();
//                if(null!=tagsDto.getTags() &&tagsDto.getTags().size()!=0 ) {
//                    goodsOrBookResponseTemp.setTagsDto(tagsDto);
//                }
                String old_json = redisService.getString("ELASTIC_SAVE:GOODS_MARKING_SKU_ID" + ":" + goodsOrBookResponseTemp.getSkuId());
                if(null!=old_json) {
                    Map labelMap = JSONObject.parseObject(old_json, Map.class);
                    goodsOrBookResponseTemp.setLabelMap(labelMap);
                }
                goodsOrBookResponse.add(goodsOrBookResponseTemp);
            });
            String json = JSON.toJSONString(goodsOrBookResponse, SerializerFeature.WriteMapNullValue);
            String old_json = redisService.getString("ELASTIC_SAVE:HOMEPAGE" + ":" + topicStoreyContentRequest.getStoreyId().toString());
            if(!json.equals(old_json)){
                redisService.setString("ELASTIC_SAVE:HOMEPAGE" + ":" + topicStoreyContentRequest.getStoreyId().toString(), json );
            }
        }catch (Exception e){
            return BaseResponse.error("失败");
        }
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 商品组件或图书组件取redis
     */
    public List<Map> getGoodsOrBookSaveByRedis(TopicStoreyContentRequest topicStoreyContentRequest ,CustomerGetByIdResponse customer){
        String old_json = redisService.getString("ELASTIC_SAVE:HOMEPAGE" + ":" + topicStoreyContentRequest.getStoreyId().toString());
        List goodsOrBookResponseList=JSONObject.parseObject(old_json,List.class);
        //List<String> skuIdList=new ArrayList<>();
        //返回值
        List<Map> goodsOrBookMapList=new ArrayList<>();
        //循环每个商品取出skuId
        for(int i=0;i<goodsOrBookResponseList.size();i++){
            Map goodsOrBookMap= (Map)goodsOrBookResponseList.get(i);
            //skuIdList.add(goodsOrBookMap.get("skuId").toString());
            goodsOrBookMapList.add(goodsOrBookMap);
        }

        /*GoodsInfoViewByIdsRequest goodsInfoViewByIdsRequest = new GoodsInfoViewByIdsRequest();
        goodsInfoViewByIdsRequest.setGoodsInfoIds(skuIdList);
        List<GoodsInfoVO> goodsInfos = goodsInfoQueryProvider.listSimpleView(goodsInfoViewByIdsRequest).getContext().getGoodsInfos();

        MarketingPluginGoodsListFilterRequest filterRequest = new MarketingPluginGoodsListFilterRequest();
        filterRequest.setGoodsInfos(KsBeanUtil.convert(goodsInfos, GoodsInfoDTO.class));
        filterRequest.setCustomerDTO(KsBeanUtil.convert(customer, CustomerDTO.class));
        GoodsInfoListByGoodsInfoResponse priceContext = marketingPluginProvider.goodsListFilter(filterRequest).getContext();
         */
        GoodsInfoListByGoodsInfoResponse priceContext = this.initGoodsPrice(customer);

        if(null== priceContext){
            return goodsOrBookMapList;
        }
        List<GoodsInfoVO> goodsInfoVOList = priceContext.getGoodsInfoVOList();
        Map<String, GoodsInfoVO> goodsPriceMap = goodsInfoVOList
                .stream().collect(Collectors.toMap(GoodsInfoVO::getGoodsInfoId, Function.identity()));

        //循环每个商品
        for(int j=0;j<goodsOrBookMapList.size();j++){
            if(null != goodsPriceMap && null != goodsPriceMap.get(goodsOrBookMapList.get(j).get("skuId").toString())){
                // recomentBookVo.setSalePrice(goodsPriceMap.get(recomentBookVo.getGoodsInfoId()).getSalePrice());
                if(null!=goodsPriceMap.get(goodsOrBookMapList.get(j).get("skuId").toString()).getSalePrice()) {
                    goodsOrBookMapList.get(j).put("salePrice", goodsPriceMap.get(goodsOrBookMapList.get(j).get("skuId").toString()).getSalePrice());
                }
                if(null!=goodsPriceMap.get(goodsOrBookMapList.get(j).get("skuId").toString()).getMarketPrice()) {
                    goodsOrBookMapList.get(j).put("marketPrice", goodsPriceMap.get(goodsOrBookMapList.get(j).get("skuId").toString()).getMarketPrice());
                }
                goodsOrBookMapList.get(j).put("fixPrice",null);
                String label = redisService.getString("ELASTIC_SAVE:GOODS_MARKING_SKU_ID" + ":" + goodsOrBookMapList.get(j).get("skuId").toString());
                if(null!=old_json) {
                    Map labelMap = JSONObject.parseObject(label, Map.class);
                    if(labelMap != null){
                        goodsOrBookMapList.get(j).put("fixPrice",labelMap.get("fix_price"));
                    }

                }
            }
        }
        return goodsOrBookMapList;
    }

    public void initPrice(List<String> spuIds){

    }

    /**
     * 初始化商品价格通用方法
     */
    private GoodsInfoListByGoodsInfoResponse initGoodsPrice(CustomerGetByIdResponse customer){
        Object priceCache = ThreadLocalUtil.get(GOODS_PRICE);
        if(!Objects.isNull(priceCache)){
            return (GoodsInfoListByGoodsInfoResponse)priceCache;
        }
        //获取所有需要查询加的商品数据
//        Map map = redisService.getObj("COLLECT_HOME:SKU", Map.class);
        String string = redisService.getString("COLLECT_HOME:SKU");
//        String old_json = redisService.getString("COLLECT_HOME:SKU");
        Map<String,Object> map =JSONObject.parseObject(string,Map.class);
        //获取商品信息
        List<String> skuIdList= (List<String>) map.get("skuIds");
        GoodsInfoViewByIdsRequest goodsInfoViewByIdsRequest = new GoodsInfoViewByIdsRequest();
        goodsInfoViewByIdsRequest.setGoodsInfoIds(skuIdList);
        List<GoodsInfoVO> goodsInfos = goodsInfoQueryProvider.listSimpleView(goodsInfoViewByIdsRequest).getContext().getGoodsInfos();

        MarketingPluginGoodsListFilterRequest filterRequest = new MarketingPluginGoodsListFilterRequest();
        filterRequest.setGoodsInfos(KsBeanUtil.convert(goodsInfos, GoodsInfoDTO.class));
        filterRequest.setCustomerDTO(KsBeanUtil.convert(customer, CustomerDTO.class));
        GoodsInfoListByGoodsInfoResponse priceContext = marketingPluginProvider.goodsListFilter(filterRequest).getContext();
        ThreadLocalUtil.set(GOODS_PRICE,priceContext);
        return priceContext;

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

    /**
     * 初始化商品详情
     * @param goodsInfoIds
     * @return
     */
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
                    MarketingLabelNewDTO context = goodsInfoQueryProvider.getMarketingLabelsBySKu(goodsCustom.get().getGoodsInfoId()).getContext();
                    if(null!=context){
                        goods.setMarketingLabels(context);
                    }
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

    public List<MixedComponentDto> getMixedComponentContent(Integer tabId, String keyWord, CustomerGetByIdResponse customer, Integer pageNum, Integer pageSize) {
        try {
            //详情
//            String mixed = redisService.getString(RedisKeyUtil.MIXED_COMPONENT+ "details");
//            List<MixedComponentTabDto> mixedComponentTab = new ArrayList<>();
//            if (!StringUtils.isEmpty(mixed)) {
//                mixedComponentTab = JSON.parseArray(mixed, MixedComponentTabDto.class);
//            }
//
//            List<MixedComponentDto> mixedComponentDtos = new ArrayList<>();
            // tab
            String tabJSON = redisService.getString(RedisKeyUtil.MIXED_COMPONENT_TAB);
            List<MixedComponentDto> mixedComponentDtos = new ArrayList<>();
            if (!StringUtils.isEmpty(tabJSON)) {
                mixedComponentDtos = JSON.parseArray(tabJSON, MixedComponentDto.class);
            }
//            mixedComponentDtos = mixedComponentTab.stream().filter(c -> MixedComponentLevel.ONE.toValue().equals(c.getLevel())).map(c -> {
//                return new MixedComponentDto(c);
//            }).collect(Collectors.toList());
            if (tabId == null || "".equals(tabId)) {
                tabId = mixedComponentDtos != null ? mixedComponentDtos.get(0).getId() : null;
            }
            Integer finalTabId = tabId;
            // 获取关键字
            List<KeyWordDto> keywords = new ArrayList<>();
            String keywordJSON = redisService.getString(RedisKeyUtil.MIXED_COMPONENT + finalTabId + ":keywords");
            if (!StringUtils.isEmpty(keywordJSON)) {
                keywords = JSON.parseArray(keywordJSON, KeyWordDto.class);
            }
//            mixedComponentTab.stream().filter(c -> MixedComponentLevel.TWO.toValue().equals(c.getLevel()) && finalTabId.equals(c.getPId()))
//                    .map(c -> {return c.getKeywords();}).collect(Collectors.toList())
//                    .forEach(c -> {c.forEach(s -> {keywords.add(new KeyWordDto(s.getId(), s.getName()));});});
            if (keyWord == null || "".equals(keyWord)) {
                keyWord = mixedComponentDtos.size() != 0 && keywords.size() != 0 ? keywords.get(0).getName() : null;
            }
            //瀑布流
            String finalKeyWord = keyWord;
            String keyWordId = keywords.stream().filter(t -> finalKeyWord.equals(t.getName())).findFirst().get().getId();
            MicroServicePage<GoodsPoolDto> goodsPoolPage = new MicroServicePage<>();
            List<JSONObject> byRange = redisListService.findByRange(RedisKeyUtil.MIXED_COMPONENT + tabId + ":" + keyWordId, pageNum * pageSize, pageNum * pageSize + 9);
            List<GoodsPoolDto> goodsPoolDtos = byRange.stream().map(s -> {return JSON.toJavaObject(s, GoodsPoolDto.class);}).collect(Collectors.toList());
            //初始化会员价
            if (customer == null) {
                HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
                customer =new CustomerGetByIdResponse();
                Map customerMap = ( Map ) request.getAttribute("claims");
                if(null!=customerMap && null!=customerMap.get("customerId")) {
                    customer = customerQueryProvider.getCustomerById(new CustomerGetByIdRequest(customerMap.get("customerId").toString())).getContext();
                }
            }
            initVipPrice(goodsPoolDtos, customer);
            goodsPoolPage.setContent(goodsPoolDtos);
            goodsPoolPage.setTotal(redisListService.getSize(RedisKeyUtil.MIXED_COMPONENT + tabId + ":" + keyWordId));
            if(keywords.size() != 0) {keywords.forEach(s -> {if(finalKeyWord.equals(s.getName())) {s.setGoodsPoolPage(goodsPoolPage);}});}
            if(mixedComponentDtos.size() != 0) {
                List<KeyWordDto> finalKeywords = keywords;
                mixedComponentDtos.forEach(s -> {if(finalTabId.equals(s.getId())) {s.setKeywords(finalKeywords);}});}
            return mixedComponentDtos;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
    }

    //获取会员价
    public void initVipPrice(List<GoodsPoolDto> goodsPoolDtos, CustomerGetByIdResponse customer) {
        try {
            List<String> skuIdList = new ArrayList<>();
            goodsPoolDtos.stream().map(GoodsPoolDto::getGoods).collect(Collectors.toList()).forEach(s -> {
                        if (s != null && s.size() != 0) {s.forEach(c -> {skuIdList.add(c.getSkuId());});}});
            //获取商品信息
           /* GoodsInfoViewByIdsRequest goodsInfoByIdRequest = new GoodsInfoViewByIdsRequest();
            goodsInfoByIdRequest.setDeleteFlag(DeleteFlag.NO);
            goodsInfoByIdRequest.setGoodsInfoIds(skuIdList);
            goodsInfoByIdRequest.setIsHavSpecText(1);
            List<GoodsInfoVO> goodsInfos = goodsInfoQueryProvider.listViewByIds(goodsInfoByIdRequest).getContext().getGoodsInfos();

            MarketingPluginGoodsListFilterRequest filterRequest = new MarketingPluginGoodsListFilterRequest();
            filterRequest.setGoodsInfos(KsBeanUtil.convert(goodsInfos, GoodsInfoDTO.class));
            filterRequest.setCustomerDTO(KsBeanUtil.convert(customer, CustomerDTO.class));
            List<GoodsInfoVO> goodsInfoVOList = marketingPluginProvider.goodsListFilter(filterRequest).getContext().getGoodsInfoVOList();
            */
            List<GoodsInfoVO> goodsInfoVOList = this.initGoodsPrice(customer).getGoodsInfoVOList();
            Map<String, GoodsInfoVO> goodsVipPriceMap = goodsInfoVOList
                    .stream().collect(Collectors.toMap(GoodsInfoVO::getGoodsInfoId, Function.identity()));
            for (GoodsPoolDto goodsPoolDto : goodsPoolDtos) {
                List<GoodsDto> goodsDtos = goodsPoolDto.getGoods();
                if(goodsDtos.size() != 0) {
                    for (int i = 0; i < goodsDtos.size(); i++) {
                        if(null==goodsVipPriceMap.get(goodsDtos.get(i).getSkuId())){
                            continue;
                        }
                        goodsDtos.get(i).setPaidCardPrice(goodsVipPriceMap.get(goodsDtos.get(i).getSkuId()).getSalePrice());
                        goodsDtos.get(i).setDiscount(goodsDtos.get(i).getRetailPrice().compareTo(BigDecimal.ZERO) != 0 ? String.valueOf((goodsDtos.get(i).getPaidCardPrice().divide(goodsDtos.get(i).getRetailPrice())).multiply(new BigDecimal(100))) : null);
                    }
                }
                goodsPoolDto.setGoods(goodsDtos);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
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

    /**
     * 预约商品
     * @param request
     * @return
     */
    @Transactional
    public BaseResponse addAppointment(AppointmentStockRequest request) {
        List<StockAppointmentRequest> list=new ArrayList<>();
        Operator operator = commonUtil.getOperator();
        request.setAccount(operator.getAccount());
        request.setCustomer(operator.getUserId());
        AppointmentRequest appointmentRequest=new AppointmentRequest();
        list.add(KsBeanUtil.convert(request,StockAppointmentRequest.class));
        appointmentRequest.setAppointmentList(list);
        return stockAppointmentProvider.add(appointmentRequest);
    }

    /**
     * 取消预约
     * @param request
     * @return
     */
    @Transactional
    public BaseResponse deleteAppointment(AppointmentStockRequest request) {
        return stockAppointmentProvider.deleteById(request.getId());
    }

    /**
     * 获取用户所有预约
     * @return
     */
    public BaseResponse<AppointmentRequest> findAppointment() {
        AppointmentStockRequest request=new AppointmentStockRequest();
        List<StockAppointmentRequest> list=new ArrayList<>();
        AppointmentRequest appointmentRequest=new AppointmentRequest();
        Operator operator = commonUtil.getOperator();
        request.setAccount(operator.getAccount());
        request.setCustomer(operator.getUserId());
        list.add(KsBeanUtil.convert(request,StockAppointmentRequest.class));
        appointmentRequest.setAppointmentList(list);
        return stockAppointmentProvider.findCustomerAppointment(appointmentRequest);
    }

    public void saveMixedComponentContent() {
        //栏目信息
        Integer topicStoreyId = 194;
        TopicStoreyColumnQueryRequest request = new TopicStoreyColumnQueryRequest();
        request.setTopicStoreyId(topicStoreyId);
        request.setPublishState(0);
        request.setState(1);
        request.setParentId(0);
        List<TopicStoreyColumnDTO> list = columnRepository.listStoryColumnAll(request);
        //获取tab
        List<TopicStoreyColumnDTO> tabLists = new ArrayList<>();
        for(TopicStoreyColumnDTO dto:list){
            TopicStoreyColumnQueryRequest param = new TopicStoreyColumnQueryRequest();
            param.setTopicStoreyId(topicStoreyId);
            param.setParentId(dto.getId());
            List<TopicStoreyColumnDTO> tabList = columnRepository.listStoryColumnAll(param);
            if(!tabList.isEmpty()){
                for(TopicStoreyColumnDTO tab:tabList){
                    tabLists.add(tab);
                }
                dto.setChilidList(tabList);
            }
        }
        //存放关键词
        redisService.setString(RedisKeyUtil.MIXED_COMPONENT_TAB+"&test", JSON.toJSONString(list));
        //循环关键词取出对应的商品池
        List<TopicStoreyColumnDTO> pools = new ArrayList<>();
        for(TopicStoreyColumnDTO tab:tabLists){
            TopicStoreyColumnQueryRequest param = new TopicStoreyColumnQueryRequest();
            param.setTopicStoreyId(topicStoreyId);
            param.setParentId(tab.getId());
            List<TopicStoreyColumnDTO> poolList = columnRepository.listStoryColumnAll(param);
            if(!poolList.isEmpty()){
                for(TopicStoreyColumnDTO pool:poolList){
                    TopicStoreyColumnGoodsQueryRequest params = new TopicStoreyColumnGoodsQueryRequest();
                    params.setTopicStoreySearchId(pool.getId());
                    params.setTopicStoreyId(topicStoreyId);
                    List<TopicStoreyColumnGoodsDTO> goodsPools = columnRepository.listStoryColumnGoodsId(params);
                    //List<TopicStoreyColumnDTO> goodsPools = columnRepository.listStoryColumnAll(param);
                    //存放商品
                    redisListService.putAll(RedisKeyUtil.MIXED_COMPONENT+ tab.getId() + ":" + pool.getId()+"&test", goodsPools);
                    pools.add(pool);
                }
                tab.setChilidList(poolList);
            }
            //存放商品池
            redisListService.putAll(RedisKeyUtil.MIXED_COMPONENT + tab.getId() + ":keywords"+"&test", poolList);
        }
    }

    public BaseResponse getGoodsDetialById1(String spuId, String skuId) {
        CustomerGetByIdResponse customer = getCustomer();
        Map map=new HashMap<>();
        String old_json=new String();
        old_json = getGoodsDetialById(spuId, skuId, "ELASTIC_SAVE:BOOKS_DETAIL_SPU_ID");
        map=JSONObject.parseObject(old_json,Map.class);

        List<String> skuIdList=new ArrayList<>();
        List<Map> maplist=new ArrayList<>();
        List<MetaBookRcmmdFigureBO> metaBookRcmmdFigureBOS=new ArrayList<>();
        //循环每个推荐人取出skuId,并放入skuIdList
//        List<String> skuIdByGoodsDetailTableOne = goodsService.getSkuIdByGoodsDetailTableOne(map, skuIdList);
//        List<String> skuIdByGoodsDetailOtherBook = goodsService.getSkuIdByGoodsDetailOtherBook(map, skuIdList);
        skuIdList = getSkuIdByGoodsDetail(old_json);
        List<String> collect = skuIdList.stream().distinct().collect(Collectors.toList());

        if(null==skuIdList ||skuIdList.size()==0 ){
            //没有商品信息需要回填
            return BaseResponse.success(map);
        }

        GoodsInfoViewByIdsRequest goodsInfoViewByIdsRequest = new GoodsInfoViewByIdsRequest();
        goodsInfoViewByIdsRequest.setGoodsInfoIds(collect);
        List<GoodsInfoVO> goodsInfos = goodsInfoQueryProvider.listSimpleView(goodsInfoViewByIdsRequest).getContext().getGoodsInfos();
//        //用户信息
//        String c = "{\"checkState\":\"CHECKED\",\"createTime\":\"2023-02-03T15:07:27\",\"customerAccount\":\"15618961858\",\"customerDetail\":{\"contactName\":\"书友_izw9\",\"contactPhone\":\"15618961858\",\"createTime\":\"2023-02-03T15:07:27\",\"customerDetailId\":\"2c9a00d184efa38001861619fbd60235\",\"customerId\":\"2c9a00d184efa38001861619fbd60234\",\"customerName\":\"书友_izw9\",\"customerStatus\":\"ENABLE\",\"delFlag\":\"NO\",\"employeeId\":\"2c9a00027f1f3e36017f202dfce40002\",\"isDistributor\":\"NO\",\"updatePerson\":\"2c90e863786d2a4c01786dd80bc0000a\",\"updateTime\":\"2023-02-11T11:18:23\"},\"customerId\":\"2c9a00d184efa38001861619fbd60234\",\"customerLevelId\":3,\"customerPassword\":\"a8568f6a11ca32de1429db6450278bfd\",\"customerSaltVal\":\"64f88c8c7b53457f55671acc856bf60b7ffffe79ba037b8753c005d1265444ad\",\"customerType\":\"PLATFORM\",\"delFlag\":\"NO\",\"enterpriseCheckState\":\"INIT\",\"fanDengUserNo\":\"600395394\",\"growthValue\":0,\"loginErrorCount\":0,\"loginIp\":\"192.168.56.108\",\"loginTime\":\"2023-02-17T10:37:58\",\"payErrorTime\":0,\"pointsAvailable\":0,\"pointsUsed\":0,\"safeLevel\":20,\"storeCustomerRelaListByAll\":[],\"updatePerson\":\"2c90e863786d2a4c01786dd80bc0000a\",\"updateTime\":\"2023-02-11T11:18:23\"}\n";
//        CustomerGetByIdResponse customer = JSON.parseObject(c, CustomerGetByIdResponse.class);
        //价格信息
        MarketingPluginGoodsListFilterRequest filterRequest = new MarketingPluginGoodsListFilterRequest();
        filterRequest.setGoodsInfos(KsBeanUtil.convert(goodsInfos, GoodsInfoDTO.class));
        filterRequest.setCustomerDTO(KsBeanUtil.convert(customer, CustomerDTO.class));
        List<GoodsInfoVO> goodsInfoVOList = marketingPluginProvider.goodsListFilter(filterRequest).getContext().getGoodsInfoVOList();
        Map<String, GoodsInfoVO> goodsPriceMap = goodsInfoVOList
                .stream().collect(Collectors.toMap(GoodsInfoVO::getGoodsInfoId, Function.identity()));

        if(null==goodsPriceMap ||goodsPriceMap.size()==0 ){
            //没有商品信息
            return BaseResponse.success(map);
        }

//        if(null!=skuIdByGoodsDetailTableOne && skuIdByGoodsDetailTableOne.size()!=0){
//            //推荐人有商品需要回填信息
//            List detailList=goodsService.fillGoodsDetailTableOne(map,goodsPriceMap);
//            map.put("bookDetail",detailList);
//        }
//        if(null!=skuIdByGoodsDetailOtherBook && skuIdByGoodsDetailOtherBook.size()!=0){
//            //其他书籍有商品需要回填信息
//            List otherBookList = goodsService.fillGoodsDetailOtherBook(map, goodsPriceMap);
//            map.put("otherBook",otherBookList);
//
//        }

        //回填商品的价格信息
        map= fillGoodsDetail(old_json,goodsPriceMap);

        //榜单
        Map rankMap = getGoodsDetailRankById(spuId, skuId, "ELASTIC_SAVE:GOODS_TAGS_SPU_ID");
        map.put("rank",rankMap);
        return BaseResponse.success(map);
    }

    /**
     * 通过spu或者sku取redis获取商详榜单信息
     * @param spuId、skuId
     * @return
     */
    public Map getGoodsDetailRankById(String spuId, String skuId,String redisTagsConstant) {

        String old_json=null;
        //优先用spuId去取
        if(null!=spuId && spuId.isEmpty()==false){
            old_json = redisService.getString(redisTagsConstant + ":" + spuId);
        } else{
//            //spuId为空则通过skuId获取spuId
//            if(null == skuId || skuId.isEmpty()){
//                return null;
//            }else {
//                Map<String, String> goodsInfoMap = goodsInfoService.goodsInfoBySkuId(skuId);
//                spuId=goodsInfoMap.get("spuId");
//                old_json = redisService.getString(redisTagsConstant + ":" + spuId);
//            }
            return null;
        }

        if(null==old_json || old_json.isEmpty()){
            return null;//不去数据库再找了
        }else {


            Map map=JSONObject.parseObject(old_json,Map.class);

            return map;
        }
    }

    /**
     * 通过spu或者sku取redis获取商详信息
     * @param spuId、skuId
     * @return
     */
    public String getGoodsDetialById(String spuId, String skuId,String redisTagsConstant) {
        String old_json=null;
        //优先用spuId去取
        if(null!=spuId && spuId.isEmpty()==false){
            old_json = redisService.getString(redisTagsConstant + ":" + spuId);
        } else{
//            //spuId为空则通过skuId获取spuId
//            if(null == skuId || skuId.isEmpty()){
//                return null;
//            }else {
//                Map<String, String> goodsInfoMap = goodsInfoService.goodsInfoBySkuId(skuId);
//                spuId=goodsInfoMap.get("spuId");
//                old_json = redisService.getString(redisTagsConstant + ":" + spuId);
//            }
            return null;
        }

        if(null==old_json || old_json.isEmpty()){
            return null;//不去数据库再找了
        }else {

            return old_json;
            //     return old_json;
        }
    }
    /**
     * 收集商详table2相关信息中的skuId信息
     * @param  old_json,skuIdList
     * @return
     */
    public List<String> getSkuIdByGoodsDetail(String old_json) {

        if(null==old_json){
            return null;
        }
        return findJsonGetKey(old_json,"sku_id");

    }

    //递归查询,查询所有为key的value
    public  List<String> findJsonGetKey(String fullResponseJson, String key) {

        List<String> list = new ArrayList<>();

        findValueObjectGetKey(JSONObject.parseObject(fullResponseJson), key, list);

        return list;
    }

    /**
     * 从json中查找对象
     *
     * @param fullResponse json对象
     * @param key          json key
     */
    private  void findValueObjectGetKey(JSONObject fullResponse, String key,List list) {

        if (fullResponse == null) {
            return;
        }
        fullResponse.keySet().forEach(keyStr -> {
            Object keyvalue = fullResponse.get((String) keyStr);
            if (keyvalue instanceof JSONArray) {
                for (int i = 0; i < ((JSONArray) keyvalue).size(); i++) {
                    Object obj = ((JSONArray) keyvalue).get(i);
                    if (obj instanceof JSONObject) {
                        findValueObjectGetKey(((JSONObject) obj), key,list);
                    }
                }
            } else if (keyvalue instanceof JSONObject) {
                findValueObjectGetKey((JSONObject) keyvalue,key, list);
            } else {
                if (key.equals(keyStr)) {
                    list.add(keyvalue);
                }
            }
        });
    }

    public Map fillGoodsDetail(String old_json, Map<String, GoodsInfoVO> goodsPriceMap) {
        Map mapTemp=new HashMap<>();
//        String s = map.toString();
        JSONObject jsonObject = JSONObject.parseObject(old_json);
        goodsPriceMap.entrySet().stream().forEach(e->{
            mapTemp.put(e.getKey(),ObjectToMap(e.getValue()));
        });
        richJson(jsonObject,"sku_id",mapTemp);
        return JSONObject.toJavaObject(jsonObject,Map.class);
    }

    public Map ObjectToMap(Object o) {
        Map map=new HashMap<>();
        Class<?> clazz = o.getClass();
        Field[] fields = clazz.getDeclaredFields();
        Arrays.stream(fields).forEach(f->{
            f.setAccessible(true);
            try {
                map.put(f.getName(),f.get(o));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        return map;
    }

    //丰富
    public  void richJson(JSONObject jsonObject, String key, Map richMap) {

        List list = findJsonGetList(jsonObject,key);
        for(int i=0;i<list.size();i++){
            Map map = (Map)list.get(i);
            String value = String.valueOf(map.get(key));

            Map findMap = (Map)richMap.get(value);
            if(findMap != null){

                map.put("goods_info_img",findMap.get("goodsInfoImg"));
                map.put("market_price",findMap.get("marketPrice"));
                map.put("sale_price",findMap.get("salePrice"));
                map.put("fix_price",null);
                String old_json = redisService.getString("ELASTIC_SAVE:GOODS_MARKING_SKU_ID" + ":" + findMap.get("goods_info_id"));
                if(null!=old_json) {
                    Map labelMap = JSONObject.parseObject(old_json, Map.class);
                    map.put("fix_price",labelMap.get("fix_price"));
                }
            }
        }

    }

    //递归查询,查询所有为key的value
    public  List<Map> findJsonGetList(JSONObject jsonObject, String key) {

        List<Map> list = new ArrayList<>();

        findValueObjectGetList(jsonObject, key, list);

        return list;
    }

    /**
     * 从json中查找对象
     *
     * @param fullResponse json对象
     * @param key          json key
     */
    private  void findValueObjectGetList(JSONObject fullResponse, String key,List list) {

        if (fullResponse == null) {
            return;
        }
        fullResponse.keySet().forEach(keyStr -> {
            Object keyvalue = fullResponse.get((String) keyStr);
            if (keyvalue instanceof JSONArray) {
                for (int i = 0; i < ((JSONArray) keyvalue).size(); i++) {
                    Object obj = ((JSONArray) keyvalue).get(i);
                    if (obj instanceof JSONObject) {
                        findValueObjectGetList(((JSONObject) obj), key,list);
                    }
                }
            } else if (keyvalue instanceof JSONObject) {
                findValueObjectGetList((JSONObject) keyvalue,key, list);
            } else {
                if (key.equals(keyStr)) {
                    list.add(fullResponse);
                }
            }
        });
    }

    public String routeIndex() {
        //获取NACOS路由配置
        JSONObject object = JSON.parseObject(refreshConfig.getV2FilterConfig());
        try {
            //获取当前用户
            CustomerVO customer = commonUtil.getCustomer();
            //用户如果未获取到跳转登陆页面
            if(Objects.isNull(customer)){
                return object.getString("loginUrl");
            }
            /*判断模式
            * 1：全部跳转老版本；
            * 2：全部跳转新版本；
            * 3：白名单跳转新版本
            * 4：白名单+根据手机号码结尾跳转新版
            * */
            String status = object.getString("status");
            //1：跳转老版本
            if(String.valueOf(Constants.ONE).equals(status)){
                return object.getString("oldUrl");
            }
            //2：跳转新版本
            if(String.valueOf(Constants.PRICETYPE).equals(status)){
                return object.getString("newUrl");
            }
            //3：白名单跳转新版；
            String userIds = object.getString("userIds");
            if(String.valueOf(Constants.GOODS_LEAF_CATE_GRADE).equals(status)&& org.apache.commons.lang3.StringUtils.isNotBlank(userIds)
                    && Arrays.asList(userIds.split(",",-1)).contains(customer.getCustomerId())){
                return object.getString("newUrl");
            }
            //4：白名单跳转新版
            String proied = object.getString("prod");//手机号结尾，1-9,支持多个配置逗号分隔，例如1,6
            if("4".equals(status)&& org.apache.commons.lang3.StringUtils.isNotBlank(userIds)
                    && Arrays.asList(userIds.split(",",-1)).contains(customer.getCustomerId())){
                return object.getString("newUrl");
            }
            //4：根据手机号码结尾
            if("4".equals(status)&& DitaUtil.isPhoneEndWith(customer.getCustomerAccount(),proied)){
                return object.getString("newUrl");
            }
            //如果都没命中直接去老版本首页
            return object.getString("oldUrl");
        }catch (Exception e){
            log.error("route page redirect error,cause:{}",e);
            return object.getString("loginUrl");
        }
    }
}
