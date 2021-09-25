package com.wanmi.sbc.booklistmodel;

import com.wanmi.sbc.booklistmodel.response.BookListModelAndGoodsListResponse;
import com.wanmi.sbc.booklistmodel.response.GoodsCustomResponse;
import com.wanmi.sbc.booklistmodel.response.GoodsExtPropertiesCustomResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.customer.api.provider.level.CustomerLevelQueryProvider;
import com.wanmi.sbc.customer.api.request.level.CustomerLevelMapByCustomerIdAndStoreIdsRequest;
import com.wanmi.sbc.customer.api.response.level.CustomerLevelMapGetResponse;
import com.wanmi.sbc.customer.bean.vo.CommonLevelVO;
import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsCustomQueryProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsCustomQueryProviderRequest;
import com.wanmi.sbc.elastic.bean.vo.goods.EsGoodsVO;
import com.wanmi.sbc.elastic.bean.vo.goods.GoodsInfoNestVO;
import com.wanmi.sbc.elastic.bean.vo.goods.GoodsLabelNestVO;
import com.wanmi.sbc.goods.api.provider.booklistmodel.BookListModelProvider;
import com.wanmi.sbc.goods.api.provider.storecate.StoreCateQueryProvider;
import com.wanmi.sbc.goods.api.request.storecate.StoreCateListByGoodsRequest;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListMixProviderResponse;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListModelProviderResponse;
import com.wanmi.sbc.goods.api.response.chooserulegoodslist.BookListGoodsProviderResponse;
import com.wanmi.sbc.goods.bean.vo.CouponLabelVO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.StoreCateGoodsRelaVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
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
    private StoreCateQueryProvider storeCateQueryProvider;

    @Autowired
    private CustomerLevelQueryProvider customerLevelQueryProvider;



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
    public Map<String, BookListMixProviderResponse> supId2BookListMixMap(List<BookListMixProviderResponse> bookListMixList) {
        //根据书单id列表 获取商品列表id信息
        if (CollectionUtils.isEmpty(bookListMixList)) {
            return new HashMap<>();
        }

        //goodsId -> BookListModelProviderResponse
        Map<String, BookListMixProviderResponse> supId2BookListMixMap = new HashMap<>();

        //获取商品id列表
        for (BookListMixProviderResponse bookListMixProviderParam : bookListMixList) {
            if (bookListMixProviderParam.getChooseRuleMode() == null) {
                continue;
            }
            List<BookListGoodsProviderResponse> bookListGoodsList = bookListMixProviderParam.getChooseRuleMode().getBookListGoodsList();
            if (CollectionUtils.isEmpty(bookListGoodsList)) {
                continue;
            }
            for (BookListGoodsProviderResponse bookListGoodsProviderParam: bookListGoodsList) {
                //这里方便后续 房源查找模版
                supId2BookListMixMap.put(bookListGoodsProviderParam.getSpuId(), bookListMixProviderParam);
            }
        }
        return supId2BookListMixMap;
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
            // EsGoods -> Map
            Map<String, EsGoodsVO> spuId2EsGoodsMap = content.stream().collect(Collectors.toMap(EsGoodsVO::getId, Function.identity(), (k1, k2) -> k1));

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
                    EsGoodsVO esGoodsVO = spuId2EsGoodsMap.get(bookListGoodsTmpParam.getSpuId());
                    if (esGoodsVO == null) {
                        continue;
                    }
                    if (goodsCustomTmpList.size() >= pageSize) {
                        break;
                    }
                    goodsCustomTmpList.add(this.packageGoodsCustomResponse(esGoodsVO));
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

//    private void test() {
//        goodsInfos.forEach(item -> {
//            List<CouponCache> couponCacheList = couponCacheService.listCouponForGoodsInfos(item, request.getLevelMap(),storeCateIdMap.get(item.getGoodsId()));
//            List<CouponLabelVO> labelList = couponCacheList.stream().limit(6).map(cache ->
//                    CouponLabelVO.builder()
//                            .couponActivityId(cache.getCouponActivityId())
//                            .couponInfoId(cache.getCouponInfoId())
//                            .couponDesc(getLabelMap(cache))
//                            .build()
//            ).collect(Collectors.toList());
//        });
//    }

//    /**
//     * 获取优惠券
//     */
//    private Map<String, List<Long>> getStoreCateIdMap(List<String> goodsIds) {
//        if (CollectionUtils.isEmpty(goodsIds)) {
//            return new HashMap<>();
//        }
//        //商品-店铺分类关联实体类
//        List<StoreCateGoodsRelaVO> storeCateGoodsRelaVOS = storeCateQueryProvider.listByGoods(
//                new StoreCateListByGoodsRequest(goodsIds)).getContext().getStoreCateGoodsRelaVOList();
//
//        //商品店铺分类
//        HashMap<String,List<Long>> storeCateIdMap = new HashMap<>();
//        for(int i=0 ; i<storeCateGoodsRelaVOS.size();i++){
//            List<Long> storeCateId=new ArrayList<>();
//            storeCateId.add(storeCateGoodsRelaVOS.get(i).getStoreCateId());
//            if(i==0){
//                storeCateIdMap.put(storeCateGoodsRelaVOS.get(i).getGoodsId(),storeCateId);
//            }
//        }
//        return storeCateIdMap;
//    }
//
//    /**
//     * 获取用户等级
//     * @param goodsInfoList
//     * @param customerId
//     * @return
//     */
//    private Map<Long, CommonLevelVO> getCustomerLevelsMap(List<GoodsInfoVO> goodsInfoList, String customerId) {
//        if (CollectionUtils.isEmpty(goodsInfoList) || StringUtils.isEmpty(customerId)) {
//            return new HashMap<>();
//        }
//        List<Long> storeIds = goodsInfoList.stream().map(GoodsInfoVO::getStoreId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
//        CustomerLevelMapByCustomerIdAndStoreIdsRequest customerLevelMapByCustomerIdAndStoreIdsRequest = new CustomerLevelMapByCustomerIdAndStoreIdsRequest();
//        customerLevelMapByCustomerIdAndStoreIdsRequest.setCustomerId(customerId);
//        customerLevelMapByCustomerIdAndStoreIdsRequest.setStoreIds(storeIds);
//        BaseResponse<CustomerLevelMapGetResponse> customerLevelMapGetResponseBaseResponse = customerLevelQueryProvider.listCustomerLevelMapByCustomerIdAndIds(customerLevelMapByCustomerIdAndStoreIdsRequest);
//        return customerLevelMapGetResponseBaseResponse.getContext().getCommonLevelVOMap();
//    }



    /**
     *  esGoodsVo 转化成 可以返回给前端的对象
     * @param esGoodsVO
     * @return
     */
    public GoodsCustomResponse packageGoodsCustomResponse(EsGoodsVO esGoodsVO) {
        String goodsInfoId = "";
        String goodsInfoNo = "";
        String goodsInfoImg = "";
        List<String> couponLabelNameList = null;


        BigDecimal currentSalePriceTmp = BigDecimal.ZERO;
        BigDecimal lineSalePrice = BigDecimal.ZERO;
        if (!CollectionUtils.isEmpty(esGoodsVO.getGoodsInfos())) {
            for (GoodsInfoNestVO goodsInfoParam : esGoodsVO.getGoodsInfos()) {
                if (goodsInfoParam.getMarketPrice() != null && currentSalePriceTmp.compareTo(goodsInfoParam.getMarketPrice()) > 0) {
                    currentSalePriceTmp = goodsInfoParam.getSalePrice();
                    lineSalePrice  = goodsInfoParam.getMarketPrice();
                }
                if (!CollectionUtils.isEmpty(goodsInfoParam.getCouponLabels())) {
                    couponLabelNameList = goodsInfoParam.getCouponLabels().stream().map(CouponLabelVO::getCouponDesc).collect(Collectors.toList());
                }
                goodsInfoId = goodsInfoParam.getGoodsInfoId();
                goodsInfoNo = goodsInfoParam.getGoodsInfoNo();
                goodsInfoImg = goodsInfoParam.getGoodsInfoImg();

            }
        }

        //商品展示价格
        BigDecimal currentSalePrice = currentSalePriceTmp;


        GoodsCustomResponse esGoodsCustomResponse = new GoodsCustomResponse();
        esGoodsCustomResponse.setGoodsId(esGoodsVO.getId());
        esGoodsCustomResponse.setGoodsNo(esGoodsVO.getGoodsNo());
        //获取最小价格的 goodsInfo
        esGoodsCustomResponse.setGoodsInfoId(goodsInfoId);
        esGoodsCustomResponse.setGoodsInfoNo(goodsInfoNo);
        esGoodsCustomResponse.setGoodsName(esGoodsVO.getGoodsName());
        esGoodsCustomResponse.setGoodsSubName(esGoodsVO.getGoodsSubtitle());
        esGoodsCustomResponse.setGoodsCoverImg(goodsInfoImg);
        esGoodsCustomResponse.setGoodsUnBackImg(esGoodsVO.getGoodsUnBackImg());
        esGoodsCustomResponse.setShowPrice(currentSalePrice);
        esGoodsCustomResponse.setLinePrice(lineSalePrice);
        esGoodsCustomResponse.setCpsSpecial(esGoodsVO.getCpsSpecial());
        esGoodsCustomResponse.setCouponLabelList(CollectionUtils.isEmpty(couponLabelNameList) ? new ArrayList<>() : couponLabelNameList);
        List<GoodsLabelNestVO> goodsLabelList = esGoodsVO.getGoodsLabelList();
        if (!CollectionUtils.isEmpty(goodsLabelList)) {
            esGoodsCustomResponse.setGoodsLabelList(goodsLabelList.stream().map(GoodsLabelNestVO::getLabelName).collect(Collectors.toList()));
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
                esGoodsCustomResponse.setLinePrice(extProperties.getPrice());
            }
        }
        return esGoodsCustomResponse;
    }
}
