package com.soybean.mall.goods.controller;
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
import com.soybean.mall.common.CommonUtil;
import com.soybean.mall.goods.req.KeyWordBookListQueryReq;
import com.soybean.mall.goods.req.KeyWordQueryReq;
import com.soybean.mall.goods.req.KeyWordSpuQueryReq;
import com.soybean.mall.goods.response.BookListSpuResp;
import com.soybean.mall.goods.response.SearchHomeResp;
import com.soybean.mall.goods.response.SpuNewBookListResp;
import com.soybean.mall.goods.service.BookListSearchService;
import com.soybean.mall.goods.service.SpuComponentService;
import com.soybean.mall.goods.service.SpuNewSearchService;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.provider.customer.CustomerProvider;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.request.customer.CustomerGetByIdRequest;
import com.wanmi.sbc.customer.api.response.customer.CustomerGetByIdResponse;
import com.wanmi.sbc.customer.bean.dto.CounselorDto;
import com.wanmi.sbc.goods.api.enums.GoodsBlackListCategoryEnum;
import com.wanmi.sbc.goods.api.provider.blacklist.GoodsBlackListProvider;
import com.wanmi.sbc.goods.api.request.blacklist.GoodsBlackListPageProviderRequest;
import com.wanmi.sbc.goods.api.response.blacklist.GoodsBlackListPageProviderResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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

    @Autowired
    private EsBookListModelProvider esBookListModelProvider;

    @Autowired
    private EsSpuNewProvider esSpuNewProvider;

    @Autowired
    private BookListSearchService bookListSearchService;

    @Autowired
    private SpuNewSearchService spuNewSearchService;

    @Autowired
    private CustomerQueryProvider customerQueryProvider;

    @Autowired
    private SpuComponentService spuComponentService;

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
            if (!Objects.equals(aggsCategory.getCategory(), SearchSpuNewAggsCategoryEnum.AGGS_PRICE_RANGE.getCode())
                || Objects.equals(aggsCategory.getCategory(), SearchSpuNewAggsCategoryEnum.AGGS_LABEL.getCode())) {
                continue;
            }
            for (EsSpuNewAggResp.AggsResp aggsResp : aggsCategory.getAggsList()) {
                aggsResp.setHasShow(true);
            }
        }
        return BaseResponse.success(listEsSpuNewAggResp);

    }
}
