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
import com.wanmi.sbc.goods.api.enums.BusinessTypeEnum;
import com.wanmi.sbc.goods.api.provider.booklistmodel.BookListModelProvider;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListMixProviderResponse;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListModelAndOrderNumProviderResponse;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListModelProviderResponse;
import com.wanmi.sbc.goods.api.response.chooserulegoodslist.BookListGoodsProviderResponse;
import com.wanmi.sbc.goods.bean.vo.CouponLabelVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/10 1:40 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Api(tags = "BookListModelController", description = "mobile 查询书单信息")
@RestController
@RequestMapping("/mobile/booklistmodel")
@Slf4j
public class BookListModelController {

    @Autowired
    private BookListModelProvider bookListModelProvider;

    @Autowired
    private EsGoodsCustomQueryProvider esGoodsCustomQueryProvider;


    /**
     * 获取榜单
     *
     * @menu 商城书单和类目
     * @status undone
     * @param spuId
     * @return
     */
    @GetMapping("/ranking-book-list-model/{spuId}")
    public BaseResponse<List<BookListModelAndOrderNumProviderResponse>> RankingBookListModel(@PathVariable("spuId") String spuId){
        BaseResponse<List<BookListModelAndOrderNumProviderResponse>> listBaseResponse =
                bookListModelProvider.listBusinessTypeBookListModel(BusinessTypeEnum.RANKING_LIST.getCode(), spuId);
        return BaseResponse.success(listBaseResponse.getContext());
    }

    /**
     *
     * 获取专题
     *
     * @menu 商城书单和类目
     * @status undone
     * @param spuId
     * @return
     */
    @GetMapping("/special-book-list-model/{spuId}")
    public BaseResponse<List<BookListModelAndOrderNumProviderResponse>> SpecialBookListModel(@PathVariable("spuId") String spuId){
        BaseResponse<List<BookListModelAndOrderNumProviderResponse>> listBaseResponse =
                bookListModelProvider.listBusinessTypeBookListModel(BusinessTypeEnum.SPECIAL_SUBJECT.getCode(), spuId);
        return BaseResponse.success(listBaseResponse.getContext());
    }

    /**
     *
     * 获取推荐
     *
     * @menu 商城书单和类目
     * @status undone
     * @param spuId
     * @return
     */
    @GetMapping("/recommend-book-list-model/{spuId}")
    public BaseResponse<List<BookListModelAndGoodsListResponse>> recommendBookListModel(@PathVariable("spuId") String spuId){

        BaseResponse<List<BookListModelAndOrderNumProviderResponse>> listBaseResponse =
                bookListModelProvider.listBusinessTypeBookListModel(BusinessTypeEnum.BOOK_RECOMMEND.getCode(), spuId);
        //根据书单列表 获取商品列表信息，
        List<BookListModelAndOrderNumProviderResponse> bookListModelAndOrderNumList;
        if (CollectionUtils.isEmpty(bookListModelAndOrderNumList = listBaseResponse.getContext())) {
            return BaseResponse.success(new ArrayList<>());
        }
        //获取书单id
        Set<Integer> bookListModelSet =
                bookListModelAndOrderNumList.stream().map(BookListModelAndOrderNumProviderResponse::getBookListModelId).collect(Collectors.toSet());
        //根据书单id列表 获取商品列表id信息
        BaseResponse<List<BookListMixProviderResponse>> listBookListMixResponse = bookListModelProvider.listPublishGoodsByIds(bookListModelSet);
        if (CollectionUtils.isEmpty(listBookListMixResponse.getContext())) {
            return BaseResponse.success(new ArrayList<>());
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
                allSupIdList.add(bookListGoodsProviderParam.getSpuId());
            }
        }
        if (CollectionUtils.isEmpty(allSupIdList)) {
            return BaseResponse.success(new ArrayList<>());
        }

        List<BookListModelAndGoodsListResponse> result = new ArrayList<>();
        //根据商品id列表 获取商品列表信息
        EsGoodsCustomQueryProviderRequest esGoodsCustomRequest = new EsGoodsCustomQueryProviderRequest();
        esGoodsCustomRequest.setPageNum(0);
        esGoodsCustomRequest.setPageSize(10);
        esGoodsCustomRequest.setGoodIdList(allSupIdList);
        esGoodsCustomRequest.setUnGoodIdList(Collections.singletonList(spuId));
//        esGoodsCustomRequest.setCpsSpecial(); TODO 知识顾问要单独处理

        // goodsId -> BookListModelAndGoodsListResponse
        Map<String, BookListModelAndGoodsListResponse> goodsId2BookListModelAndGoodsListMap = new HashMap<>();

        MicroServicePage<EsGoodsVO> esGoodsVOMicroServicePage = esGoodsCustomQueryProvider.listEsGoodsNormal(esGoodsCustomRequest);
        List<EsGoodsVO> content = esGoodsVOMicroServicePage.getContent();
        if (!CollectionUtils.isEmpty(content)) {
            for (EsGoodsVO esGoodsVO : content) {
                BookListModelProviderResponse bookListModelProviderResponse = supId2BookListModelMap.get(esGoodsVO.getId());
                if (bookListModelProviderResponse == null) {
                    log.error("--->>> BookListModelController.recommendBookListModel goodsId:{} can't find in model supId2BookListModelMap", esGoodsVO.getId());
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
        //房源和模版匹配


        return BaseResponse.success(result);
    }

}
