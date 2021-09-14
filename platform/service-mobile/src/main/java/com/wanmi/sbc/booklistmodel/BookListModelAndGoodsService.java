package com.wanmi.sbc.booklistmodel;

import com.wanmi.sbc.booklistmodel.response.BookListModelAndGoodsListResponse;
import com.wanmi.sbc.booklistmodel.response.GoodsCustomResponse;
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

        List<String> allSupIdList = new ArrayList<>();
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
        if (CollectionUtils.isEmpty(allSupIdList)) {
            return new HashMap<>();
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
        esGoodsCustomRequest.setPageSize(pageSize);
        esGoodsCustomRequest.setGoodIdList(spuIdCollection);
        if (!CollectionUtils.isEmpty(unSpuIdCollection)) {
            esGoodsCustomRequest.setUnGoodIdList(unSpuIdCollection);
        }
//        esGoodsCustomRequest.setCpsSpecial(); TODO 知识顾问要单独处理

        // goodsId -> BookListModelAndGoodsListResponse
        Map<String, BookListModelAndGoodsListResponse> goodsId2BookListModelAndGoodsListMap = new HashMap<>();

        MicroServicePage<EsGoodsVO> esGoodsVOMicroServicePage = esGoodsCustomQueryProvider.listEsGoodsNormal(esGoodsCustomRequest);
        List<EsGoodsVO> content = esGoodsVOMicroServicePage.getContent();
        if (!CollectionUtils.isEmpty(content)) {
            for (EsGoodsVO esGoodsVO : content) {
                BookListModelProviderResponse bookListModelProviderResponse = supId2BookListModelMap.get(esGoodsVO.getId());
                if (bookListModelProviderResponse == null) {
                    log.error("--->>> BookListModelAndGoodsService.recommendBookListModel goodsId:{} can't find in model supId2BookListModelMap", esGoodsVO.getId());
                    continue;
                }
                BookListModelAndGoodsListResponse bookListModelAndGoodsListResponseTmp = goodsId2BookListModelAndGoodsListMap.get(esGoodsVO.getId());
                if (bookListModelAndGoodsListResponseTmp == null) {
                    BookListModelAndGoodsListResponse bookListModelAndGoodsListResponse = new BookListModelAndGoodsListResponse();
                    bookListModelAndGoodsListResponse.setBookListModel(bookListModelProviderResponse);
                    bookListModelAndGoodsListResponse.setGoodsList(new ArrayList<>());
                    goodsId2BookListModelAndGoodsListMap.put(esGoodsVO.getId(), bookListModelAndGoodsListResponse);
                    result.add(bookListModelAndGoodsListResponse);
                } else {
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

//                    esGoodsCustomResponse.setGoodsScore(esGoodsVO.getgoodsco); todo
//                    esGoodsCustomResponse.setProperties(); todo
                    bookListModelAndGoodsListResponseTmp.getGoodsList().add(esGoodsCustomResponse);
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
}
