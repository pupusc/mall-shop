package com.wanmi.sbc.booklistmodel;

import com.wanmi.sbc.booklistmodel.response.BookListModelAndGoodsListResponse;
import com.wanmi.sbc.booklistmodel.response.GoodsCustomResponse;
import com.wanmi.sbc.booklistmodel.response.GoodsExtPropertiesCustomResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsCustomQueryProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsCustomQueryProviderRequest;
import com.wanmi.sbc.elastic.bean.vo.goods.EsGoodsVO;
import com.wanmi.sbc.elastic.bean.vo.goods.GoodsInfoNestVO;
import com.wanmi.sbc.elastic.bean.vo.goods.GoodsLabelNestVO;
import com.wanmi.sbc.goods.api.provider.booklistmodel.BookListModelProvider;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListMixProviderResponse;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListModelProviderResponse;
import com.wanmi.sbc.goods.api.response.chooserulegoodslist.BookListGoodsProviderResponse;
import com.wanmi.sbc.goods.bean.vo.CouponLabelVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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


    /**
     * 根据 书单模版id 获取 商品spuId 书单Map信息
     * @param bookListModelIdSet
     * @return
     */
    public Map<String, BookListModelProviderResponse> mapGoodsIdByBookListModelList(Collection<Integer> bookListModelIdSet) {
        //根据书单id列表 获取商品列表id信息
        BaseResponse<List<BookListMixProviderResponse>> listBookListMixResponse = bookListModelProvider.listPublishGoodsByModelIds(bookListModelIdSet);
        if (CollectionUtils.isEmpty(listBookListMixResponse.getContext())) {
            return new HashMap<>();
        }

        //goodsId -> BookListModelProviderResponse
        Map<String, BookListModelProviderResponse> supId2BookListModelMap = new HashMap<>();

        //获取商品id列表
        for (BookListMixProviderResponse bookListMixProviderParam : listBookListMixResponse.getContext()) {
            if (bookListMixProviderParam.getChooseRuleMode() == null) {
                continue;
            }
            List<BookListGoodsProviderResponse> bookListGoodsList = bookListMixProviderParam.getChooseRuleMode().getBookListGoodsList();
            if (CollectionUtils.isEmpty(bookListGoodsList)) {
                continue;
            }
            for (BookListGoodsProviderResponse bookListGoodsProviderParam: bookListGoodsList) {
                //这里方便后续 房源查找模版
                supId2BookListModelMap.put(bookListGoodsProviderParam.getSpuId(), bookListMixProviderParam.getBookListModel());
            }
        }
        return supId2BookListModelMap;
    }

    /**
     * 根据 商品id 书单map 获取商品列表详细信息
     * @param supId2BookListModelMap
     * @param unSpuIdCollection
     * @return
     */
    public MicroServicePage<BookListModelAndGoodsListResponse> listGoodsBySpuIdAndBookListModel(
            Map<String, BookListModelProviderResponse> supId2BookListModelMap, Collection<String> unSpuIdCollection, int pageNum, int pageSize) {
        // supId --> BookListModelProviderResponse
        Collection<String> spuIdCollection = supId2BookListModelMap.keySet();

        List<BookListModelAndGoodsListResponse> result = new ArrayList<>();
        //根据商品id列表 获取商品列表信息
        EsGoodsCustomQueryProviderRequest esGoodsCustomRequest = new EsGoodsCustomQueryProviderRequest();
        esGoodsCustomRequest.setPageNum(pageNum);
        esGoodsCustomRequest.setPageSize(Math.min(spuIdCollection.size(), 100)); //这里主要是为啦防止书单里面的数量过分的多的情况，限制最多100个
        esGoodsCustomRequest.setGoodIdList(spuIdCollection);
        if (!CollectionUtils.isEmpty(unSpuIdCollection)) {
            esGoodsCustomRequest.setUnGoodIdList(unSpuIdCollection);
        }
//        esGoodsCustomRequest.setCpsSpecial(); TODO 知识顾问要单独处理

        // goodsId -> BookListModelAndGoodsListResponse
        Map<Integer, BookListModelAndGoodsListResponse> goodsId2BookListModelAndGoodsListMap = new HashMap<>();

        BaseResponse<MicroServicePage<EsGoodsVO>> esGoodsVOMicroServiceResponse = esGoodsCustomQueryProvider.listEsGoodsNormal(esGoodsCustomRequest);
        MicroServicePage<EsGoodsVO> esGoodsVOMicroServicePage = esGoodsVOMicroServiceResponse.getContext();
        List<EsGoodsVO> content = esGoodsVOMicroServicePage.getContent();
        if (!CollectionUtils.isEmpty(content)) {
            for (EsGoodsVO esGoodsVO : content) {
                BookListModelProviderResponse bookListModelProviderResponse = supId2BookListModelMap.get(esGoodsVO.getId());
                if (bookListModelProviderResponse == null) {
                    log.error("--->>> BookListModelAndGoodsService.recommendBookListModel goodsId:{} can't find in model supId2BookListModelMap", esGoodsVO.getId());
                    continue;
                }
                BookListModelAndGoodsListResponse bookListModelAndGoodsListResponseTmp = goodsId2BookListModelAndGoodsListMap.get(bookListModelProviderResponse.getId());
                if (bookListModelAndGoodsListResponseTmp == null) {
                    BookListModelAndGoodsListResponse bookListModelAndGoodsListResponse = new BookListModelAndGoodsListResponse();
                    bookListModelAndGoodsListResponse.setBookListModel(bookListModelProviderResponse);
                    List<GoodsCustomResponse> goodsList = new ArrayList<>();
                    goodsList.add(this.packageGoodsCustomResponse(esGoodsVO));
                    bookListModelAndGoodsListResponse.setGoodsList(goodsList);
                    goodsId2BookListModelAndGoodsListMap.put(bookListModelProviderResponse.getId(), bookListModelAndGoodsListResponse);
                    result.add(bookListModelAndGoodsListResponse);
                } else {
                    if (bookListModelAndGoodsListResponseTmp.getGoodsList().size() >= pageSize) {
                        continue;
                    }
                    bookListModelAndGoodsListResponseTmp.getGoodsList().add(this.packageGoodsCustomResponse(esGoodsVO));
                }
            }
        }
        MicroServicePage<BookListModelAndGoodsListResponse> microServicePageResult = new MicroServicePage<>();
        microServicePageResult.setNumber(esGoodsVOMicroServicePage.getNumber());
        microServicePageResult.setSize(esGoodsVOMicroServicePage.getSize());
        microServicePageResult.setTotal(esGoodsVOMicroServicePage.getTotal());
        microServicePageResult.setPageable(esGoodsVOMicroServicePage.getPageable());
        microServicePageResult.setContent(result);
        return microServicePageResult;
    }


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
        //商品市场价
        BigDecimal currentSalePrice = BigDecimal.ZERO;
        BigDecimal lineSalePrice = BigDecimal.ZERO;
        if (!CollectionUtils.isEmpty(esGoodsVO.getGoodsInfos())) {
            for (GoodsInfoNestVO goodsInfoParam : esGoodsVO.getGoodsInfos()) {
                if (goodsInfoParam.getMarketPrice() != null && currentSalePrice.compareTo(goodsInfoParam.getMarketPrice()) > 0) {
                    currentSalePrice = goodsInfoParam.getSalePrice();
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
            }
        }
        return esGoodsCustomResponse;
    }
}
